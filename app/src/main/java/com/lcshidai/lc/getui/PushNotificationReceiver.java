package com.lcshidai.lc.getui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.socks.library.KLog;
import com.lcshidai.lc.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PushNotificationReceiver extends BroadcastReceiver {
    private final String PUSH_NOTIFICATION_CLICK_ACTION = "com.yrz.trj.action.PushNotificationClickAction";
    private final String TAG = PushNotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    // 处理json数据
                    dealWithMsgData(context, data);
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String clientId = bundle.getString("clientid");
                KLog.d(TAG, clientId);
                break;
            case PushConsts.GET_SDKONLINESTATE:
                boolean online = bundle.getBoolean("onlineState");
                KLog.d(TAG, "online = " + online);
                break;

            case PushConsts.SET_TAG_RESULT:
                String sn = bundle.getString("sn");
                String code = bundle.getString("code");
                String text = "设置标签失败, 未知异常";
                switch (Integer.valueOf(code)) {
                    case PushConsts.SETTAG_SUCCESS:
                        text = "设置标签成功";
                        break;

                    case PushConsts.SETTAG_ERROR_COUNT:
                        text = "设置标签失败, tag数量过大, 最大不能超过200个";
                        break;

                    case PushConsts.SETTAG_ERROR_FREQUENCY:
                        text = "设置标签失败, 频率过快, 两次间隔应大于1s";
                        break;

                    case PushConsts.SETTAG_ERROR_REPEAT:
                        text = "设置标签失败, 标签重复";
                        break;

                    case PushConsts.SETTAG_ERROR_UNBIND:
                        text = "设置标签失败, 服务未初始化成功";
                        break;

                    case PushConsts.SETTAG_ERROR_EXCEPTION:
                        text = "设置标签失败, 未知异常";
                        break;

                    case PushConsts.SETTAG_ERROR_NULL:
                        text = "设置标签失败, tag 为空";
                        break;

                    case PushConsts.SETTAG_NOTONLINE:
                        text = "还未登陆成功";
                        break;

                    case PushConsts.SETTAG_IN_BLACKLIST:
                        text = "该应用已经在黑名单中,请联系售后支持!";
                        break;

                    case PushConsts.SETTAG_NUM_EXCEED:
                        text = "已存 tag 超过限制";
                        break;

                    default:
                        break;
                }

                KLog.d(TAG, "set tag result sn = " + sn + ", code = " + code);
                KLog.d(TAG, "set tag result sn = " + text);
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 *
                 * KLog.d("GetuiSdkDemo", "appid = " + appid); KLog.d("GetuiSdkDemo", "taskid = " +
                 * taskid); KLog.d("GetuiSdkDemo", "actionid = " + actionid); KLog.d("GetuiSdkDemo",
                 * "result = " + result); KLog.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }

    private void dealWithMsgData(Context context, String message) {
        String notifyTime = DateFormat.format("kk:mm", System.currentTimeMillis()).toString();
        try {
            JSONObject messageObj = new JSONObject(message);
            // 数据获取
            if (!messageObj.isNull("custom_content")) {
                JSONObject ccObj = messageObj.getJSONObject("custom_content");

                JSONObject keyObj = ccObj.getJSONObject("key");
                String title = keyObj.getString("title");
                String content = keyObj.getString("content");
                String ctime = keyObj.getString("ctime");
                String notice_type = !keyObj.isNull("notice_type") ? keyObj.getString("notice_type") : "";
                String notice_id = !keyObj.isNull("id") ? keyObj.getString("id") : "";
                String prj_id = !keyObj.isNull("prj_id") ? keyObj.getString("prj_id") : "";
                String now_time = !keyObj.isNull("now_time") ? keyObj.getString("now_time") : "";
                //IOS闹钟推送标识
                if ("system_alarm_push".equals(notice_type)) {
                    return;
                }
                //闹钟
                if ("alarm".equals(notice_type)) {
                    return;
                }

                if (!"activity".equals(notice_type)) {
                    Intent intent = new Intent(PUSH_NOTIFICATION_CLICK_ACTION);
                    Bundle bundle = new Bundle();
                    bundle.putString("noticetype", notice_type);
                    bundle.putString("content", content);
                    bundle.putString("title", title);
                    bundle.putString("datetime", ctime);
                    bundle.putString("notice_id", notice_id);
                    bundle.putString("prj_id", prj_id);
                    intent.putExtras(bundle);

                    createNotification(context, title, content, notifyTime, intent);
                }
            }

        } catch (JSONException e) {
            Intent intent = new Intent(PUSH_NOTIFICATION_CLICK_ACTION);
            Bundle bundle = new Bundle();
            String title = context.getResources().getString(R.string.app_name);
            bundle.putString("noticetype", "launchMain");
            bundle.putString("content", message);
            bundle.putString("title", title);
//            bundle.putString("datetime", ctime);
//            bundle.putString("notice_id", notice_id);
//            bundle.putString("prj_id", prj_id);
            intent.putExtras(bundle);
            createNotification(context, title, message, notifyTime, intent);
            e.printStackTrace();
        }
    }

    private void createNotification(Context context, String title, String content, String notifyTime, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.notification_custom_builder);
        rv.setImageViewBitmap(R.id.notification_icon, BitmapFactory.decodeResource(context.getResources(), R.drawable.icon));
        rv.setTextViewText(R.id.notification_title, title);
        rv.setTextViewText(R.id.notification_text, content);
        rv.setTextViewText(R.id.notification_time, notifyTime);

        //4.2以下版本
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            Notification.Builder mBuilder = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setStyle(new Notification.BigTextStyle().bigText(content)
                            .setBigContentTitle(title))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setTicker(title)
                    .setWhen(System.currentTimeMillis());
            nManager.notify(0, mBuilder.build());
        } else {
            NotificationCompat.Builder ncBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setTicker(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(content).setBigContentTitle(title))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setWhen(System.currentTimeMillis());
            nManager.notify((int) System.currentTimeMillis(), ncBuilder.build());
        }
    }
}
