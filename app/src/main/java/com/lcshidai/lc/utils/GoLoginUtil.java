package com.lcshidai.lc.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.alarm.AlarmInfo;
import com.lcshidai.lc.alarm.AlarmXmlManager;
import com.lcshidai.lc.db.UserInfoModel;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.model.account.BaseLogin;
import com.lcshidai.lc.model.account.BaseLoginData;
import com.lcshidai.lc.model.account.BaseLoginJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.ui.GestureLoginActivity;
import com.lcshidai.lc.ui.LoginActivity;
import com.lcshidai.lc.ui.base.TRJActivity;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 登录过滤
 *
 * @author 000814
 */
public class GoLoginUtil {

    /**
     * 无跳转至登录界面
     *
     * @param context
     */
    public static void BaseToLoginActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    /**
     * 底层访问
     *
     * @param context
     * @param requestCode
     * @param className
     * @return
     */
    public synchronized static boolean ToLoginActivityForResultBase(TRJActivity context, int requestCode,
                                                                    String className) {
        String u = getU(context);
        boolean isShowGestureLogin = isShowGestureLogin(context);
        boolean isNetworkConnected = NetUtils.isNetworkConnected(context);
        if (!u.equals("") && isShowGestureLogin && isNetworkConnected) {// 判断是否记住有密码并且，以及网络状况，条件不通过转至第一次登录状态
            goGestureLoginActivity(context, className);
            return false;
        }
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("fromSubActivity", context.getClass().getName());
        intent.putExtra("goClass", className);// 主动跳至某activity 需要传递activity 名称
        context.startActivityForResult(intent, requestCode);
        return false;
    }

    /**
     * 底层访问
     *
     * @param context
     * @param requestCode
     * @param className
     * @return
     */
    public synchronized static boolean ToLoginActivityForResultBase(Context context, int requestCode,
                                                                    String className) {
        ToLoginActivityForResultBase((TRJActivity) context, requestCode, className);
        return false;
    }

    public synchronized static void goGestureLoginActivity(TRJActivity context,
                                                           String className) {
        Intent intent = new Intent(context, GestureLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("goClass", className);
        context.startActivity(intent);
    }

    public synchronized static void goGestureLoginActivityForResult(TRJActivity context,
                                                                    String className, int requestCode) {
        Intent intent = new Intent(context, GestureLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("goClass", className);
        context.startActivityForResult(intent, requestCode);
    }

    public static void gestureAutoLogin(TRJActivity context, int requestCode, String className) {
        LCHttpClient.clearCookie();
        if (!getU(context).equals("")) {
            String p = getP(context);
            login(getU(context), p, context, className);
            try {
                if (context.getIntent() != null) {
                    Class clazz = null;
                    String nameStr = className;
                    if (nameStr != null && !nameStr.equals("")) {
                        Intent intent = context.getIntent();
                        clazz = Class.forName(className);
                        intent.setClass(context, clazz);
                        if (MemorySave.MS.args != null)
                            intent.putExtras(MemorySave.MS.args);
                        context.startActivity(intent);
                    }
                    context.getIntent().removeExtra("goClass");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("fromSubActivity", context.getClass().getName());
            intent.putExtra("goClass", className);// 主动跳至某activity 需要传递activity
            // 名称
            context.startActivityForResult(intent, requestCode);
        }
    }

    public static void autoLogin(final TRJActivity activity) {
        LCHttpClient.clearCookie();
        if (!getU(activity).equals("")) {
            String p = getP(activity);
            login(getU(activity), p, activity, "");
        }
    }

    public static void autoLogin(final TRJActivity activity, String uname) {
        LCHttpClient.clearCookie();
        if (!getP(activity).equals("")) {
            String p = getP(activity);
            login(uname, p, activity, "");
        }
    }

    public static void autoLogin(final Context context, String uname) {
        autoLogin((TRJActivity) context, uname);
    }


    public static void login(final String account, final String pwd,
                             final TRJActivity content, final String className) {
        // 清除之前的cookie
        LCHttpClient.clearCookie();
        MemorySave.MS.mIsLogining = true;
        RequestParams rp = new RequestParams();
        rp.put("account", account);
        try {
            rp.put("password", AesUtil.getInstance().encrypt(pwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rp.put("is_hand", "1");
        rp.put("deviceid", CommonUtil.getDeviceId(content));
        content.post(HttpServiceURL.LOGIN_URL, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response != null) {
                        String boolen = response.getString("boolen");
                        if (boolen.equals("1")) {
                            if (!response.isNull("data")) {
                                MemorySave.MS.mGestureAudoLoginFlag = false;
                                String uid = response.optJSONObject("data").optString("uid");
                                String uc_id = response.optJSONObject("data").optString("uc_id");
                                SpUtils.setString(SpUtils.Table.USER, SpUtils.User.UID, uid);
                                saveUcId(uc_id, content);
                                GoLoginUtil.saveUserModel(response, content);
                                GoLoginUtil.loginSuccess();
                                try {
                                    // 保存cookie
                                    CookieUtil cookieUtil = new CookieUtil(content);
                                    List<Cookie> cookies = LCHttpClient.getCookie();
                                    if (!cookies.isEmpty()) {
                                        String strCookie = "";
                                        Cookie cookie = null;
                                        for (int i = 0; i < cookies.size(); i++) {
                                            cookie = cookies.get(i);
                                            if ("PHPSESSID".equals(cookie.getName())) {
                                                strCookie = cookie.getName() + "=" + cookie.getValue();
                                            }
                                        }
                                        cookieUtil.saveCookie(strCookie);
                                        Log.i("save_cookie", "COOKIE_SAVE—" + strCookie);
                                    }

                                } catch (Exception e) {
                                }
                            }
                        } else {
                            String strm = "暂无数据,请登入";
                            String message = response.getString("message");
                            if (!boolen.equals("1") && message != null && !message.trim().equals("")) {
                                if (strm.indexOf(message) == -1)
                                    ToastUtil.showToast(content, message);
                            }
                            clearInfoToLoginNoCookie(content);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                content.sendBroadcast(TRJActivity.MY_RESON_LOGIN_STATUS);
                MemorySave.MS.isLoginSendBroadcast = true;

                Intent intent = new Intent();
                intent.setAction(BroadCastImpl.ACTION_LOGIN_SUCCEED_OR_FAILED);
                intent.putExtra("loginSucceed", true);
                content.sendBroadcast(intent);
                MemorySave.MS.mIsLogining = false;
                LoginStatusHelper.LoginStatusChange = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                content.sendBroadcast(TRJActivity.MY_RESON_LOGIN_STATUS);
                MemorySave.MS.isLoginSendBroadcast = true;
                Intent intent = new Intent();
                intent.setAction(BroadCastImpl.ACTION_LOGIN_SUCCEED_OR_FAILED);
                intent.putExtra("loginSucceed", false);
                content.sendBroadcast(intent);
                MemorySave.MS.mIsLogining = false;

                clearInfoToLoginCookieState(content);
            }

        });
    }

    public static void upP(String uid, String username, String pwd, Activity activity) {
        try {
            SharedPreferences user = activity.getSharedPreferences("apd", 0);
            Editor editor = user.edit();
            editor.putString("uid", uid);
            editor.putString("un", username);
            editor.putString("pd", AesUtil.parseByte2HexStr((AesUtil.encrypt(pwd, "HOO@OOK"))));
            editor.apply();
        } catch (Exception e) {
        }
    }

    public static void clearUser(Activity context) {
        try {
            SharedPreferences user = context.getSharedPreferences("apd", 0);
            Editor editor = user.edit();
            editor.clear();
            editor.apply();
        } catch (Exception e) {
        }
    }

    public static String getU(Activity context) {
        String strName = "";
        try {
            SharedPreferences user = context.getSharedPreferences("apd", 0);
            strName = user.getString("un", "");
        } catch (Exception e) {
            return "";
        }
        return strName;
    }

    public static String getI(Activity context) {
        String strId = "";
        try {
            SharedPreferences user = context.getSharedPreferences("apd", 0);
            strId = user.getString("uid", "");
        } catch (Exception e) {
            return "";
        }
        return strId;
    }

    public static String getP(Activity context) {
        String strP = "";
        try {
            SharedPreferences user = context.getSharedPreferences("apd", 0);
            strP = user.getString("pd", "");
            if (!strP.equals("")) {
                String p = new String(AesUtil.decrypt(AesUtil.parseHexStr2Byte(strP), "HOO@OOK"));
                return p;
            }
        } catch (Exception e) {
            return "";
        }
        return strP;
    }

    /**
     * 保存uc_id到sp文件
     *
     * @param uc_id   uc_id
     * @param content 上下文对象
     */
    public static void saveUcId(String uc_id, Activity content) {
        try {
            SharedPreferences user = content.getSharedPreferences("config_setting", 0);
            Editor editor = user.edit();
            editor.putString("uc_id", uc_id);
            editor.apply();
        } catch (Exception e) {
        }
    }

    /**
     * 获取sp中的uc_id
     *
     * @param activity 上下文对象
     * @return uc_id
     */
    public static String getUcId(Activity activity) {
        String uc_id = "";
        try {
            SharedPreferences user = activity.getSharedPreferences("config_setting", 0);
            uc_id = user.getString("uc_id", "");
        } catch (Exception e) {
            return "";
        }
        return uc_id;
    }

    /**
     * 保存access_token到sp文件
     *
     * @param access_token access_token
     * @param content      上下文对象
     */
    public static void saveAccessToken(String access_token, Activity content) {
        try {
            SharedPreferences user = content.getSharedPreferences("config_setting", 0);
            Editor editor = user.edit();
            editor.putString("access_token", access_token);
            editor.apply();
        } catch (Exception e) {
        }
    }

    /**
     * 获取sp中的access_token
     *
     * @param activity 上下文对象
     * @return access_token
     */
    public static String getAccessToken(Activity activity) {
        String access_token = "";
        try {
            SharedPreferences user = activity.getSharedPreferences("config_setting", 0);
            access_token = user.getString("access_token", "");
        } catch (Exception e) {
            return "";
        }
        return access_token;
    }

    /**
     * 保存user_token到sp文件
     *
     * @param user_token user_token
     * @param content    上下文对象
     */
    public static void saveUserToken(String user_token, Activity content) {
        try {
            SharedPreferences user = content.getSharedPreferences("config_setting", 0);
            Editor editor = user.edit();
            editor.putString("user_token", user_token);
            editor.apply();
        } catch (Exception e) {
        }
    }

    /**
     * 获取sp中的user_token
     *
     * @param activity 上下文对象
     * @return user_token
     */
    public static String getUserToken(Activity activity) {
        String access_token = "";
        try {
            SharedPreferences user = activity.getSharedPreferences("config_setting", 0);
            access_token = user.getString("user_token", "");
        } catch (Exception e) {
            return "";
        }
        return access_token;
    }

    public static void saveManagerPhone(String manager_phone, Activity content) {
        try {
            SharedPreferences user = content.getSharedPreferences("config_setting", 0);
            Editor editor = user.edit();
            editor.putString("manager_phone", manager_phone);
            editor.apply();
        } catch (Exception e) {
        }
    }

    public static String getManagerPhone(Activity activity) {
        String manager_phone = "";
        try {
            SharedPreferences user = activity.getSharedPreferences("config_setting", 0);
            manager_phone = user.getString("manager_phone", "");
        } catch (Exception e) {
            return "";
        }
        return manager_phone;
    }

    /**
     * 保存风险测试有效期
     *
     * @param date    date
     * @param content content
     */
    public static void saveRiskValidateTime(String date, Activity content) {
        try {
            SharedPreferences user = content.getSharedPreferences("config_setting", 0);
            Editor editor = user.edit();
            editor.putString("date", date);
            editor.apply();
        } catch (Exception e) {
        }
    }

    public static String getRiskValidateTime(Activity activity) {
        String date = "";
        try {
            SharedPreferences user = activity.getSharedPreferences("config_setting", 0);
            date = user.getString("date", "");
        } catch (Exception e) {
            return "";
        }
        return date;
    }

    public static void saveManagerName(String manager_name, Activity content) {
        try {
            SharedPreferences user = content.getSharedPreferences("config_setting", 0);
            Editor editor = user.edit();
            editor.putString("manager_name", manager_name);
            editor.apply();
        } catch (Exception e) {
        }
    }

    public static String getManagerName(Activity activity) {
        String manager_name = "";
        try {
            SharedPreferences user = activity.getSharedPreferences("config_setting", 0);
            manager_name = user.getString("manager_name", "");
        } catch (Exception e) {
            return "";
        }
        return manager_name;
    }

    public static void saveUserModel(BaseLoginJson response, Activity content) {
        BaseLoginData dataJson = response.getData();
        MemorySave.MS.userInfo = new UserInfoModel();
        MemorySave.MS.userInfo.is_paypwd_edit = dataJson
                .getIs_paypwd_edit();// 1,
        MemorySave.MS.userInfo.uid = dataJson.getUid();// 119,
        MemorySave.MS.userInfo.is_change_uname = dataJson
                .getIs_change_uname();// 0,
        MemorySave.MS.userInfo.sex = dataJson.getSex();// 0,
        // ava=dataJson.opt("");// {
        if (dataJson.getAva() != null) {
            MemorySave.MS.userInfo.url_s100 = dataJson.getAva().getUrl_s100();// http=dataJson.opt("");////test.yrz.cn/data/uploads/img/2014/02/17/s100_5301c55535fab.jpg,
            MemorySave.MS.userInfo.url_s50 = dataJson.getAva().getUrl_s50();// http=dataJson.opt("");////test.yrz.cn/data/uploads/img/2014/02/17/s50_5301c55535fab.jpg,
            MemorySave.MS.userInfo.url_s300 = dataJson.getAva().getUrl_s300();// http=dataJson.opt("");////test.yrz.cn/data/uploads/img/2014/02/17/s300_5301c55535fab.jpg,
            MemorySave.MS.userInfo.url = dataJson.getAva().getUrl();// http=dataJson.opt("");////test.yrz.cn/data/uploads/img/2014/02/17/5301c55535fab.jpg,
            MemorySave.MS.userInfo.url_s700 = dataJson.getAva().getUrl_s700();// http=dataJson.opt("");////test.yrz.cn/data/uploads/img/2014/02/17/s700_5301c55535fab.jpg
        }
        MemorySave.MS.userInfo.person_id = dataJson.getPerson_id();// 321322198912165665,
        MemorySave.MS.userInfo.is_active = dataJson.getIs_active();// 1,
        MemorySave.MS.userInfo.uname = dataJson.getUname();// mao,
        MemorySave.MS.userInfo.mi_id = dataJson.getMi_id();// 1,
        MemorySave.MS.userInfo.is_id_auth = dataJson.getIs_id_auth();// 1,
        MemorySave.MS.userInfo.safe_level = dataJson.getSafe_level();// 80,
        MemorySave.MS.userInfo.dept_id = dataJson.getDept_id();// 43,
        MemorySave.MS.userInfo.identity_no = dataJson.getIdentity_no();// I101,I102,I103,I104,
        MemorySave.MS.userInfo.is_email_auth = dataJson
                .getIs_email_auth();// 0,
        MemorySave.MS.userInfo.vip_group_id = dataJson
                .getVip_group_id();// ,1,,
        MemorySave.MS.userInfo.real_name = dataJson.getReal_name();// 毛迎兰,
        MemorySave.MS.userInfo.is_mobile_auth = dataJson
                .getIs_mobile_auth();// 1,
        MemorySave.MS.userInfo.before_logintime = dataJson
                .getBefore_logintime();// 1392693365,
        MemorySave.MS.userInfo.mobile = dataJson.getMobile();// 18994374723
        MemorySave.MS.userInfo.is_set_uname = dataJson
                .getIs_set_uname();// 18994374723
        MemorySave.MS.userInfo.is_all = dataJson.getIs_all();// 18994374723
        MemorySave.MS.userInfo.is_newbie = dataJson.getIs_newbie();
        MemorySave.MS.userInfo.is_paypwd_mobile_set = dataJson
                .getIs_paypwd_mobile_set();
        MemorySave.MS.userInfo.is_recharged = dataJson
                .getIs_recharged();
        MemorySave.MS.userInfo.user_is_qfx = dataJson.isUser_is_qfx();
        MemorySave.MS.userInfo.uc_id = dataJson.getUc_id();
        MemorySave.MS.userInfo.is_binding_bank = dataJson.getIs_binding_bank();
        try {
            AlarmXmlManager.clearAllAlarm(content);
            List<BaseLogin> alarmArrayInfo = response.getData().getRemindList();
            if (alarmArrayInfo != null && alarmArrayInfo.size() > 0) {
                BaseLogin jitem;
                AlarmInfo alarmInfo;
                for (int i = 0; i < alarmArrayInfo.size(); i++) {
                    jitem = alarmArrayInfo.get(i);
                    if (jitem != null) {
                        alarmInfo = new AlarmInfo();
                        alarmInfo.obj_id = jitem.getObj_id();
                        alarmInfo.remind_time = jitem.getRemind_time();
                        alarmInfo.prj_type_name = jitem
                                .getPrj_type_name();
                        alarmInfo.start_bid_time = jitem
                                .getStart_bid_time();
                        AlarmXmlManager.addAlarm(alarmInfo, content);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static void saveUserModel(JSONObject response, Activity content) {
        JSONObject dataJson = response.optJSONObject("data");
        MemorySave.MS.userInfo = new UserInfoModel();
        MemorySave.MS.userInfo.is_paypwd_edit = dataJson
                .optString("is_paypwd_edit");// 1,
        MemorySave.MS.userInfo.uid = dataJson.optString("uid");// 119,
        MemorySave.MS.userInfo.is_change_uname = dataJson
                .optString("is_change_uname");// 0,
        MemorySave.MS.userInfo.sex = dataJson.optString("sex");// 0,
        // ava=dataJson.opt("");// {
        if (dataJson.optJSONObject("ava") != null) {
            MemorySave.MS.userInfo.url_s100 = dataJson.optJSONObject("ava")
                    .optString("url_s100");// http=dataJson.opt("");////test.yrz.cn/data/uploads/img/2014/02/17/s100_5301c55535fab.jpg,
            MemorySave.MS.userInfo.url_s50 = dataJson.optJSONObject("ava")
                    .optString("url_s50");// http=dataJson.opt("");////test.yrz.cn/data/uploads/img/2014/02/17/s50_5301c55535fab.jpg,
            MemorySave.MS.userInfo.url_s300 = dataJson.optJSONObject("ava")
                    .optString("url_s300");// http=dataJson.opt("");////test.yrz.cn/data/uploads/img/2014/02/17/s300_5301c55535fab.jpg,
            MemorySave.MS.userInfo.url = dataJson.optJSONObject("ava").optString(
                    "url");// http=dataJson.opt("");////test.yrz.cn/data/uploads/img/2014/02/17/5301c55535fab.jpg,
            MemorySave.MS.userInfo.url_s700 = dataJson.optJSONObject("ava")
                    .optString("url_s700");// http=dataJson.opt("");////test.yrz.cn/data/uploads/img/2014/02/17/s700_5301c55535fab.jpg
        }// },
        MemorySave.MS.userInfo.person_id = dataJson.optString("person_id");// 321322198912165665,
        MemorySave.MS.userInfo.is_active = dataJson.optString("is_active");// 1,
        MemorySave.MS.userInfo.uname = dataJson.optString("uname");// mao,
        MemorySave.MS.userInfo.mi_id = dataJson.optString("mi_id");// 1,
        MemorySave.MS.userInfo.is_id_auth = dataJson.optString("is_id_auth");// 1,
        MemorySave.MS.userInfo.safe_level = dataJson.optString("safe_level");// 80,
        MemorySave.MS.userInfo.dept_id = dataJson.optString("dept_id");// 43,
        MemorySave.MS.userInfo.identity_no = dataJson.optString("identity_no");// I101,I102,I103,I104,
        MemorySave.MS.userInfo.is_email_auth = dataJson
                .optString("is_email_auth");// 0,
        MemorySave.MS.userInfo.vip_group_id = dataJson
                .optString("vip_group_id");// ,1,,
        MemorySave.MS.userInfo.real_name = dataJson.optString("real_name");// 毛迎兰,
        MemorySave.MS.userInfo.is_mobile_auth = dataJson
                .optString("is_mobile_auth");// 1,
        MemorySave.MS.userInfo.before_logintime = dataJson
                .optString("before_logintime");// 1392693365,
        MemorySave.MS.userInfo.mobile = dataJson.optString("mobile");// 18994374723
        MemorySave.MS.userInfo.is_set_uname = dataJson
                .optString("is_set_uname");// 18994374723
        MemorySave.MS.userInfo.is_all = dataJson.optString("is_all");// 18994374723
        MemorySave.MS.userInfo.is_newbie = dataJson.optString("is_newbie");
        MemorySave.MS.userInfo.is_paypwd_mobile_set = dataJson
                .optString("is_paypwd_mobile_set");
        MemorySave.MS.userInfo.is_recharged = dataJson
                .optString("is_recharged");
        MemorySave.MS.userInfo.user_is_qfx = dataJson.optBoolean("user_is_qfx");
        MemorySave.MS.userInfo.uc_id = dataJson.optString("uc_id");
        try {
            AlarmXmlManager.clearAllAlarm(content);
            JSONArray alarmArrayInfo = response.optJSONObject("data")
                    .optJSONArray("remindList");
            if (alarmArrayInfo != null && alarmArrayInfo.length() > 0) {
                JSONObject jitem;
                AlarmInfo alarmInfo;
                for (int i = 0; i < alarmArrayInfo.length(); i++) {
                    jitem = alarmArrayInfo.optJSONObject(i);
                    if (jitem != null) {
                        alarmInfo = new AlarmInfo();
                        alarmInfo.obj_id = jitem.optString("obj_id");
                        alarmInfo.remind_time = jitem.optString("remind_time");
                        alarmInfo.prj_type_name = jitem
                                .optString("prj_type_name");
                        alarmInfo.start_bid_time = jitem
                                .optString("start_bid_time");
                        AlarmXmlManager.addAlarm(alarmInfo, content);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 判断是否要显示锁屏页面
     *
     * @param context
     * @return true：显示 flase：不显示
     */
    public static boolean isShowGestureLogin(Context context) {
        boolean isGestureLockOpen = SpUtils.getBoolean(SpUtils.Table.CONFIG,
                SpUtils.Config.IS_GESTURE_OPEN, false);
        // 当前手势密码对应的用户名
        String gestureLockUserId = SpUtils.getString(SpUtils.Table.CONFIG, SpUtils.Config.UID);
        // 当前登陆的用户名
        String currentUserId = getI((Activity) context);
        return isGestureLockOpen
                && LockPatternUtils.getInstance(context).savedPatternExists()
                && (!gestureLockUserId.equals("") && gestureLockUserId
                .equals(currentUserId));

    }

    /**
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("backProcess", String.format("Background App:", appProcess.processName));
                    return true;
                } else {
                    Log.i("backProcess", String.format("Foreground App:", appProcess.processName));
                    return false;
                }
            }
        }
        return false;
    }

    public static void loginSuccess() {
        MemorySave.MS.mIsLoginout = false;
        MemorySave.MS.mIsLogin = true;
        MemorySave.MS.mIsLogined = true;
    }

    public static void clearInfoToLoginCookieState(Activity activity) {
        LCHttpClient.clearCookie();
        // GoLoginUtil.BaseToLoginActivity(activity);
        // 解除当前用户推送绑定
//		new BDPushUitl(activity).deletePushInfoToServer();
        MemorySave.MS.mIsLogin = false;
        MemorySave.MS.mIsLogined = false;
        // GoLoginUtil.clearUser(activity);
        MemorySave.MS.userInfo = null;
    }

    public static void clearInfoToLoginNoCookie(Activity activity) {
        // LCHttpClient.clearCookie();
        // GoLoginUtil.BaseToLoginActivity(activity);
        // 解除当前用户推送绑定
//		new BDPushUitl(activity).deletePushInfoToServer();
        MemorySave.MS.mIsLogin = false;
        MemorySave.MS.mIsLogined = false;
        GoLoginUtil.clearUser(activity);
        MemorySave.MS.userInfo = null;
    }

    public static void clearInfoToLogin(Activity activity) {
        LCHttpClient.clearCookie();
        // 解除当前用户推送绑定
//		new BDPushUitl(activity).deletePushInfoToServer();
        MemorySave.MS.mIsLogin = false;
        GoLoginUtil.clearUser(activity);
        MemorySave.MS.userInfo = null;
        // MemorySave.MS.mgoHomeLoginOut=true;
    }

    public static void saveUserName(String strName, Context context) {
        try {
            SharedPreferences user = context.getSharedPreferences("user_info", 0);
            Editor editor = user.edit();
            editor.putString("USER_NAME", strName);
            editor.apply();
        } catch (Exception e) {
        }
    }

    public static String getUserName(Context context) {
        String strName = "";
        try {
            SharedPreferences user = context.getSharedPreferences("user_info", 0);
            strName = user.getString("USER_NAME", "");
        } catch (Exception e) {
            return "";
        }
        return strName;
    }

    /**
     * 保存银行预留手机号
     *
     * @param leftPhone 预留手机号
     * @param context
     */
    public static void saveBankLeftPhone(String leftPhone, Context context) {
        try {
            SharedPreferences user = context.getSharedPreferences("user_info", 0);
            Editor editor = user.edit();
            editor.putString("BANK_LEFT_PHONE", leftPhone);
            editor.apply();
        } catch (Exception e) {

        }
    }

    /**
     * 获取银行预留手机号
     *
     * @param context context
     * @return 预留手机号
     */
    public static String getBankLeftPhone(Context context) {
        String strName = "";
        try {
            SharedPreferences user = context.getSharedPreferences("user_info", 0);
            strName = user.getString("BANK_LEFT_PHONE", "");
        } catch (Exception e) {
            return "";
        }
        return strName;
    }

    /**
     * 保存存管标志
     *
     * @param flag    0->未设置;1->已开户;2->未开户;
     * @param context context
     */
    public static void saveCunGuanFlag(int flag, Context context) {
        try {
            SharedPreferences user = context.getSharedPreferences("user_info", 0);
            Editor editor = user.edit();
            editor.putInt("CUN_GUAN_FLAG", flag);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取存管标志
     *
     * @param context context
     * @return 存管标志
     */
    public static int getCunGuanFlag(Context context) {
        int cunGuanFlag = 0;
        try {
            SharedPreferences user = context.getSharedPreferences("user_info", 0);
            cunGuanFlag = user.getInt("CUN_GUAN_FLAG", 0);
        } catch (Exception e) {
            return 0;
        }
        return cunGuanFlag;
    }

    /**
     * 保存是否浙商卡标志
     *
     * @param flag    0->未设置;1->浙商卡;2->他行卡;
     * @param context context
     */
    public static void saveZheShangCardFlag(int flag, Context context) {
        try {
            SharedPreferences user = context.getSharedPreferences("user_info", 0);
            Editor editor = user.edit();
            editor.putInt("ZHE_SHANG_CARD_FLAG", flag);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取浙商卡标志
     *
     * @param context context
     * @return 浙商卡标志
     */
    public static int getZsCardFlag(Context context) {
        int zheShangCardFlag = 0;
        try {
            SharedPreferences user = context.getSharedPreferences("user_info", 0);
            zheShangCardFlag = user.getInt("ZHE_SHANG_CARD_FLAG", 0);
        } catch (Exception e) {
            return 0;
        }
        return zheShangCardFlag;
    }

    /**
     * 保存是否设置手机支付密码标志
     *
     * @param flag    0->未设置;1->已设置;－1->未访问接口;
     * @param context context
     */
    public static void setPayPswSetFlag(int flag, Context context) {
        try {
            SharedPreferences user = context.getSharedPreferences("user_info", 0);
            Editor editor = user.edit();
            editor.putInt("PAY_PSW_SET_FLAG", flag);
            editor.apply();
        } catch (Exception e) {

        }
    }

    /**
     * 获取是否设置手机支付密码标志
     *
     * @param context context
     * @return 浙商卡标志
     */
    public static int getPayPswSetFlag(Context context) {
        int zheShangCardFlag = 0;
        try {
            SharedPreferences user = context.getSharedPreferences("user_info", 0);
            zheShangCardFlag = user.getInt("PAY_PSW_SET_FLAG", -1);
        } catch (Exception e) {
            return 0;
        }
        return zheShangCardFlag;
    }


}
