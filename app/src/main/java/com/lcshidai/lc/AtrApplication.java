package com.lcshidai.lc;

import android.support.multidex.MultiDexApplication;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.lcshidai.lc.getui.GtPushService;
import com.socks.library.KLog;
import com.tencent.bugly.crashreport.CrashReport;
import com.tendcloud.tenddata.TCAgent;
import com.lcshidai.lc.db.UserInfo;
import com.lcshidai.lc.utils.CrashHandler;

public class AtrApplication extends MultiDexApplication {
    private static AtrApplication atrApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        atrApplication = this;
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
        crashHandler.init(getApplicationContext(), this);
        // 初始化
        TCAgent.init(this.getApplicationContext());
        TCAgent.setReportUncaughtExceptions(true);
        KLog.init(false);

        CrashReport.initCrashReport(getApplicationContext(), "e820650418", false);
    }

    public static AtrApplication getInstance() {
        return atrApplication;
    }

    public UserInfo mUserInfo;

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}