package com.lcshidai.lc.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;

import com.lcshidai.lc.AtrApplication;
import com.lcshidai.lc.impl.CrashHandlerImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpCrashHandlerService;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author Stay 在Application中统一捕获异常，保存到文件中下次再打开时上传
 */
public class CrashHandler implements UncaughtExceptionHandler, CrashHandlerImpl {
    /**
     * 是否开启日志输出,在Debug状态下开启, 在Release状态下关闭以提示程序性能
     */
    public static final boolean DEBUG = true;
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * CrashHandler实例
     */
    @SuppressLint("StaticFieldLeak")
    private static CrashHandler INSTANCE;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    HttpCrashHandlerService hchs;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx, AtrApplication app) {
        mContext = ctx;
        hchs = new HttpCrashHandlerService(mContext, this);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex != null) {
            String msg = ex.getLocalizedMessage();
            if (null != msg && msg.indexOf("com.baidu.android.pushservice.PushService") != -1) {
                return;
            }
        }
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
            System.exit(10);
        } else { // 如果自己处理了异常，则不会弹出错误对话框，则需要手动退出app
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @return true代表处理该异常，不再向上抛异常，
     * false代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理，
     * 简单来说就是true不会弹出那个错误提示框，false就会弹出
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        final String msg = ex.getLocalizedMessage();
        final StackTraceElement[] stack = ex.getStackTrace();
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    StringBuilder str_ex = new StringBuilder();
                    if (msg != null && msg.length() > 0) {
                        str_ex.append(msg);
                    }
                    for (int i = 0; i < stack.length; i++) {
                        str_ex.append(stack[i].toString());
                    }
//                    FileUtils.writeFileWithBugLog(mContext, ex.getMessage());
                    postReport(str_ex.toString());
//					}
                } catch (Exception e) {
                    e.getStackTrace();
                }
                Looper.loop();
            }

        }.start();
        return false;
    }

    // 使用HTTP Post 发送错误报告到服务器 这里不再赘述
    private void postReport(String message) {
        String model = android.os.Build.MODEL;
        String release = android.os.Build.VERSION.RELEASE;
        String versionName = CommonUtil.getVersionName(mContext, mContext.getPackageName());
        hchs.gainCrashHandler(model, release, versionName, message);
    }

    @Override
    public void gainCrashHandlersuccess(BaseJson response) {
        try {
            if (response != null) {
                if (response.getBoolen() != null) {
                    String result = response.getBoolen();
                    if (result.equals("1")) {
                        if (mContext != null)
                            ToastUtil.showLongToast(mContext, "程序出错,反馈日志已经发送到服务端.");
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void gainCrashHandlerfail() {

    }
}
