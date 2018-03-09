package com.lcshidai.lc.service;

import android.content.Context;

import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.ProJsonHandler;
import com.lcshidai.lc.model.AdsJson;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.MessageJson;
import com.lcshidai.lc.model.OpenAccountJson;
import com.lcshidai.lc.model.account.AccountHasEscrowedJson;
import com.lcshidai.lc.model.account.AccountIsZheshangCardJson;
import com.lcshidai.lc.model.account.AccountJson;
import com.lcshidai.lc.model.account.AccountSettingJson;
import com.lcshidai.lc.model.account.BindUidCidJson;
import com.lcshidai.lc.model.account.GetEscrowRemindJson;
import com.lcshidai.lc.model.account.MobileCheckJson;
import com.lcshidai.lc.model.account.SignJson;
import com.lcshidai.lc.model.account.SignUserInfoJson;
import com.lcshidai.lc.model.finance.FinanceInfoJson;
import com.lcshidai.lc.model.licai.AccessTokenJson;
import com.lcshidai.lc.model.licai.CheckFundRegisterJson;
import com.lcshidai.lc.model.licai.IsShowCfyJson;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.StringUtils;

/**
 * Api请求类
 * Created by RandyZhang on 2017/5/11.
 */

public class ApiService {

    /**
     * 获取小红点消息
     */
    public static void getAllMsg(ProJsonHandler<MessageJson> proJsonHandler,
                                 Context context, String uid,
                                 int invest_sequence,
                                 int discovery_sequence,
                                 int account_sequence) {
        if ("".equals(uid)) {
            uid = "0";
        }
        if (invest_sequence == -1) {
            invest_sequence = 0;
        }
        if (account_sequence == -1) {
            account_sequence = 0;
        }
        if (discovery_sequence == -1) {
            discovery_sequence = 0;
        }
        if (StringUtils.isEmpty(uid)) {
            uid = "0";
        }
        String imei = CommonUtil.getDeviceId(context);
        String conts;
//		[
//          {"type":"invest","sequence":100},
//          {"type":"discovery","sequence":100},
//          {"type":"account""uid":"10","sequence":101}
//      ]
        if (uid.equals("0")) {
            conts = "[{\"type\":\"invest\" , \"sequence\":" + invest_sequence + "}," +
                    "{\"type\":\"discovery\" , \"sequence\":" + discovery_sequence + "}]";
        } else {
            conts = "[{\"type\":\"invest\" , \"sequence\":" + invest_sequence + "}," +
                    "{\"type\":\"discovery\" , \"sequence\":" + discovery_sequence + "}," +
                    "{\"type\":\"account\" , \"uid\":\"" + uid + "\",\"sequence\":" +
                    account_sequence + "}]";
        }
        RequestParams param = new RequestParams();
        param.put("deviceno", imei);
        param.put("uids", "[" + uid + "]");
        param.put("conts", conts);

        LCHttpClient.post(proJsonHandler, param, context, HttpServiceURL.GET_ALL_NEW_MSG_LIST);
    }

    /**
     * 获取账户信息
     */
    public static void getAccountInfo(ProJsonHandler<AccountJson> proJsonHandler,
                                      Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.GET_ACCOUNT_INFO);
    }

    /**
     * 获取账户设置
     *
     * @param uid uid
     */
    public static void getAccountSetting(ProJsonHandler<AccountSettingJson> proJsonHandler,
                                         Context context,
                                         String uid) {
        RequestParams rp = new RequestParams();
        rp.put("uid", uid);
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.GET_ACCOUNT_SETTING);
    }

    /**
     * 是否开通存管
     */
    public static void getIsOpenCg(ProJsonHandler<AccountHasEscrowedJson> proJsonHandler,
                                   Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.HAS_ESCROWED);
    }

    /**
     * 判断是否浙商卡片
     */
    public static void getIsZsCard(ProJsonHandler<AccountIsZheshangCardJson> proJsonHandler,
                                   Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.IS_ZHESHANG_CARD);
    }

    /**
     * 获取准备开通存管所需要的信息
     */
    public static void getPreOpenCgInfo(ProJsonHandler<OpenAccountJson> proJsonHandler,
                                        Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.GET_OPEN_ACCOUNT_INFO);
    }

    /**
     * 获取是否弹窗提醒
     */
    public static void getIsCgRemind(ProJsonHandler<GetEscrowRemindJson> proJsonHandler,
                                     Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.GET_ESCROW_REMIND);
    }

    /**
     * 设置已弹窗提醒
     */
    public static void setIsCgRemind(ProJsonHandler<BaseJson> proJsonHandler,
                                     Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.SET_ESCROW_REMIND);
    }

    /**
     * 签到
     */
    public static void sign(ProJsonHandler<SignJson> proJsonHandler,
                            Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.SIGN);
    }

    /**
     * 获取Ads
     */
    public static void getAdsByUid(ProJsonHandler<AdsJson> proJsonHandler,
                                   Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.ADS_BY_UID);
    }

    /**
     * 获取签到积分信息
     */
    public static void getSignUserInfo(ProJsonHandler<SignUserInfoJson> proJsonHandler,
                                       Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.get(proJsonHandler, rp, context, HttpServiceURL.SIGN_SCORE_INFO);
    }

    /**
     * 获取是否显示长富云（理财师）模块
     */
    public static void getIsShowCfyModule(ProJsonHandler<IsShowCfyJson> proJsonHandler,
                                          Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.get(proJsonHandler, rp, context, HttpServiceURL.IS_CFY_SHOW);
    }

    /**
     * 检查长富云基金账户是否注册
     */
    public static void isCfyFundAccountRegister(
            ProJsonHandler<CheckFundRegisterJson> proJsonHandler,
            Context context,
            String access_token,
            String c_uid) {
        RequestParams rp = new RequestParams();
        rp.put("access_token", access_token);
        rp.put("c_uid", c_uid);
        LCHttpClient.postWithFullUrl(proJsonHandler, rp, context, HttpServiceURL.CHECK_FUND_REGISTER);
    }

    /**
     * 获取长富云accessToken
     */
    public static void getAccessToken(ProJsonHandler<AccessTokenJson> proJsonHandler,
                                      Context context, String client, String device_number) {
        RequestParams rp = new RequestParams();
        rp.put("client", client);
        rp.put("device_number", device_number);
        LCHttpClient.postWithFullUrl(proJsonHandler, rp, context, HttpServiceURL.GET_ACCESS_TOKEN);
    }

//-------------------------------
//    用户相关接口
//-------------------------------

    /**
     * 手机号是否注册
     *
     * @param mobile 用户手机号
     */
    public static void isMobileRegister(ProJsonHandler<MobileCheckJson> proJsonHandler,
                                        Context context, String mobile) {
        RequestParams rp = new RequestParams();
        rp.put("mobile", mobile);
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.IS_MOBILE_REGISTER);
    }

    /**
     * Uid Cid 绑定
     *
     * @param uid         uid
     * @param cid         cid
     * @param client_type 设备类型
     */
    public static void bindUidCid(ProJsonHandler<BindUidCidJson> proJsonHandler,
                                  Context context, String uid, String cid, String client_type) {
        RequestParams rp = new RequestParams();
        rp.put("uid", uid);
        rp.put("cid", cid);
        rp.put("client_type", client_type);
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.BIND_UID_CID);
    }

    /**
     * 获取bitmap二进制数据
     *
     * @param fullUrl 完整url路径
     */
    public static void getBitmapDataWithFullUrl(BinaryHttpResponseHandler binaryHttpResponseHandler,
                                                String fullUrl) {
        LCHttpClient.getWithFullUrl(fullUrl, binaryHttpResponseHandler);

    }

    /**
     * 获取bitmap二进制数据
     *
     * @param url 相对url路径
     */
    public static void getBitmapData(BinaryHttpResponseHandler binaryHttpResponseHandler,
                                     Context context,
                                     String url) {
        LCHttpClient.get(context, url, binaryHttpResponseHandler);
    }

    /**
     * 判断是否登录
     *
     * @param proJsonHandler proJsonHandler
     * @param context        context
     */
    public static void isLogin(ProJsonHandler<BaseJson> proJsonHandler, Context context) {
        RequestParams rp = new RequestParams();
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.IS_LOGIN);
    }

    /**
     * @param proJsonHandler proJsonHandler
     * @param context        context
     * @param tag            tag
     * @param type           埋点代码
     * @param actName        特殊动作
     */
    public static void dataBuriedPoint(ProJsonHandler<BaseJson> proJsonHandler,
                                       Context context,
                                       String tag,
                                       String type,
                                       String actName) {
        RequestParams rp = new RequestParams();
        rp.put("tag", tag);
        rp.put("type", type);
        rp.put("deviceId", CommonUtil.getDeviceId(context));
        if (!CommonUtil.isNullOrEmpty(actName)) {
            rp.put("act_name", actName);
        }
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.DATA_BURIED_POINT);
    }
//-------------------------------
//    项目相关接口
//-------------------------------

    /**
     * 获取项目详情
     *
     * @param proJsonHandler 处理
     * @param context        context
     * @param prjId          项目id
     * @param isCollection   是否集合标
     */
    public static void getProjectDetail(ProJsonHandler<FinanceInfoJson> proJsonHandler,
                                        Context context,
                                        String prjId,
                                        int isCollection) {
        RequestParams rp = new RequestParams();
        rp.put("prj_id", prjId);
        rp.put("is_collection", isCollection);
        LCHttpClient.post(proJsonHandler, rp, context, HttpServiceURL.GET_PROJECT_DETAIL);
    }


//-------------------------------
//    现相关接口
//-------------------------------

    /**
     * 提现
     */
//    public static void withdraw(ProJsonHandler)

    /**
     * 预约提现
     */
//    public static void

    /**
     * 获取开户行所在城市信息（省、市、区）
     */

    /**
     * 获取对应城市的开户行
     */

}
