package com.lcshidai.lc.getui;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lcshidai.lc.ui.GestureLoginActivity;
import com.lcshidai.lc.ui.MainActivity;
import com.lcshidai.lc.ui.account.MessageDetailActivity;
import com.lcshidai.lc.utils.MemorySave;

import java.util.ArrayList;
import java.util.List;

public class NotificationClickReceiver extends BroadcastReceiver {

    private final String PACKAGE_NAME = "com.trj.hp";
    private final String LOADING_ACTIVITY_NAME = "com.trj.hp.ui.LoadingActivity";
    private final String MAIN_ACTIVITY_NAME = "com.trj.hp.ui.MainActivity";
    private final String DETAILED_ACTIVITY_NAME = "com.trj.hp.bdpush.AlarmDialogActivity";
    private final String GESTURE_LOGIN_NAME = "com.trj.hp.ui.GestureLoginActivity";

    @Override
    public void onReceive(Context context, Intent in) {

        Bundle bundle = in.getExtras();
        String notice_type = bundle.getString("noticetype");
        String content = bundle.getString("content");
        String title = bundle.getString("title");
        String ctime = bundle.getString("datetime");
        String notice_id = bundle.getString("notice_id");
        String prj_id = bundle.getString("prj_id");

        Intent intent = new Intent();
        //新标
        if ("new_bid".equals(notice_type)) {
            int flag = getRunningProcess(context);
            //程序已经运行
            if (flag == 1) {
                intent.setClass(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MemorySave.MS.mIsGoFinance = true;
                context.startActivity(intent);
            }
            //3-启动APP时手势解锁页面		4-APP已经启动过的锁屏页面
            else if (flag == 3 || flag == 4) {
                MemorySave.MS.mIsGoFinance = true;
                Intent intent2 = new Intent(context, GestureLoginActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
            } else {
                intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cn = new ComponentName(PACKAGE_NAME, LOADING_ACTIVITY_NAME);
                intent.setComponent(cn);
                intent.putExtra("alarm_to_finance", 2);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
        //公告
        else if ("notice".equals(notice_type)) {
            intent.setClass(context, MainActivity.class);
            intent.putExtra("dynamic_id", notice_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if ("launchMain".equals(notice_type)) {
            intent.setClass(context, MainActivity.class);
            intent.putExtra("dynamic_id", notice_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        //消息中心
        else {
            int flag = getRunningProcess(context);
            //程序已经运行
            if (flag == 1) {
                intent.setClass(context, MessageDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("message_centre", "message_centre");
                intent.putExtra("md_title", title);
                intent.putExtra("md_content", content);
                intent.putExtra("md_ctime", ctime);
                context.startActivity(intent);
            }
            //3-启动APP时手势解锁页面		4-APP已经启动过的锁屏页面
            else if (flag == 3 || flag == 4) {
                Intent intent2 = new Intent(context, GestureLoginActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.putExtra("message_centre", "message_centre");
                intent2.putExtra("md_title", title);
                intent2.putExtra("md_content", content);
                intent2.putExtra("md_ctime", ctime);
                context.startActivity(intent2);
            } else {
                intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cn = new ComponentName(PACKAGE_NAME, LOADING_ACTIVITY_NAME);
                intent.setComponent(cn);
                intent.putExtra("message_centre", "message_centre");
                intent.putExtra("md_title", title);
                intent.putExtra("md_content", content);
                intent.putExtra("md_ctime", ctime);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    /**
     * 判断当前程序是否登陆正运行
     *
     * @param context
     * @return 0:当前程序没有运行	1:当前程序有运行Main		2：当前程序只运行了AlarmDetailed
     */
    public int getRunningProcess(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        //获取当前系统正在运行的任务
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        //创建一个程序名和主activity的对象集合(将当前所有运行程序的此信息放入集合)
        List<ComponentName> cnList = new ArrayList<ComponentName>();
        for (RunningTaskInfo rapi : list) {
            cnList.add(rapi.baseActivity);
        }

        //创建一个MainActivity的一个信息对象
        ComponentName main = new ComponentName(PACKAGE_NAME, MAIN_ACTIVITY_NAME);
        //已经有过运行MainActivity
        if (cnList.contains(main)) {
            if (MemorySave.MS.mIsGestureLoginAlive) {
                return 4;
            }
            return 1;
        }

        //创建一个AlarmDetailedActivity的一个信息对象
        ComponentName detailed = new ComponentName(PACKAGE_NAME, DETAILED_ACTIVITY_NAME);
        //正在运行AlarmDetailedActivity
        if (cnList.contains(detailed)) {
            return 2;
        }

        //创建一个GestureLoginActivity的一个信息对象
        ComponentName gesture = new ComponentName(PACKAGE_NAME, GESTURE_LOGIN_NAME);
        //正在运行GestureLoginActivity
        if (cnList.contains(gesture)) {
            return 3;
        }
        return 0;
    }


}
