package com.lcshidai.lc.http;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.socks.library.KLog;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoClassUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.ToastUtil;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.BuildConfig;

public abstract class BaseJsonHandler<Json_Type> extends BaseJsonHttpResponseHandler<Json_Type> {

    protected TRJActivity fa;
    protected boolean isShowMessageFlag = true;
    String Tag = BaseJsonHandler.class.getSimpleName();

    public BaseJsonHandler() {
    }

    public BaseJsonHandler(TRJActivity fa) {
        this.fa = fa;
    }

    public BaseJsonHandler(TRJActivity fa, boolean isShowMessage) {
        this.fa = fa;
        this.isShowMessageFlag = isShowMessage;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers,
                          String rawJsonResponse, Json_Type response) {
        // 接口请求成功处理
        BaseJson baseJson = (BaseJson) response;
        if (baseJson.getBoolen().equals("1")) {
            // 正确的数据处理
            KLog.i(Tag, "请求成功，接收到正确数据");
        } else {
            // 错误的数据处理
            KLog.i(Tag, "请求成功，接收到错误数据");
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBytes,
                          Throwable throwable) {
        if (BuildConfig.DEBUG) {
            // 接口请求失败处理
            KLog.i(Tag, "请求失败，接收到正确数据");
        }
    }

    @Override
    protected Json_Type parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        KLog.json(rawJsonData);
        BaseJson bj = new ObjectMapper().readValues(new JsonFactory()
                .createParser(rawJsonData), BaseJson.class).next();
        if (fa == null) {
            return null;
        }

        String down = bj.getDown();
        String url = bj.getUrl();
        if (down != null && down.equals("1")) {
            Message msg = new Message();
            msg.obj = url;
            new DownHandler(fa.getMainLooper()).sendMessage(msg);
            return null;
        }
        String logined;
        try {
            String strm = "暂无数据,请登入";
            if (bj.getBoolen() != null) {
                String boolen = bj.getBoolen();
                if (boolen.equals("1")) {
                } else {
                    logined = bj.getLogined();
                    if (logined != null && logined.trim().equals("0")) {
                        MemorySave.MS.mIsLogin = false;
                        String nameStr = GoClassUtil.goClass;
                        GoClassUtil.goClass = null;
                        Message msg = new Message();
                        msg.obj = nameStr;
                        new LoginHandler(fa.getMainLooper()).sendMessage(msg);
                    }
                }
            } else if (bj.getMessage() != null && bj.getBoolen() != null) {
                String message = bj.getMessage();
                String boolen = bj.getBoolen();
                if (!boolen.equals("1") && message != null
                        && !message.trim().equals("") && isShowMessageFlag) {
                    if (!strm.contains(message)) {
                        ToastUtil.showToast(fa, message);
                    }
                }
            }
            return (Json_Type) bj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理登录跳转
     */
    private class LoginHandler extends Handler {
        LoginHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String nameStr = (String) msg.obj;
            GoLoginUtil.ToLoginActivityForResultBase(fa, Constants.REQUEST_CODE, nameStr != null ? nameStr : "");
        }
    }

    /**
     * 处理维护页面跳转
     */
    private class DownHandler extends Handler {
        DownHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String url = (String) msg.obj;
            Intent intent = new Intent(fa, MainWebActivity.class);
            intent.putExtra("web_url", url);
            intent.putExtra("need_header", "0");
            intent.putExtra("down", "1");
            fa.startActivity(intent);
        }
    }

}
