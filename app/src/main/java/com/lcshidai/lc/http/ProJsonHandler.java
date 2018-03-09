package com.lcshidai.lc.http;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.socks.library.KLog;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoClassUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;

import org.apache.http.Header;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ProJsonHandler<Json_Type> extends BaseJsonHttpResponseHandler<Json_Type> {

    protected Context context;
    String Tag = ProJsonHandler.class.getSimpleName();

    private BaseCallback<Json_Type> callback;

    public ProJsonHandler(BaseCallback<Json_Type> callback, Context context) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers,
                          String rawJsonResponse, Json_Type response) {

        KLog.json(rawJsonResponse);
        // 接口请求成功处理
        BaseJson baseJson = (BaseJson) response;
        if (baseJson.getBoolen().equals("1")) {
            // 正确的数据处理
//            getRequestURI().toString();
            KLog.i(Tag, "请求成功，接收到正确数据");
            if (null != callback) {
                callback.onRightData(response);
            }
        } else {
            // 错误的数据处理
            KLog.i(Tag, "请求成功，接收到错误数据");
            if (null != callback) {
                callback.onWrongData(response);
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                          String rawJsonData, Json_Type errorResponse) {
        processErrorMsg(statusCode);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBytes,
                          Throwable throwable) {
        processErrorMsg(statusCode);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Json_Type parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        // 重置时间

        Type genType = callback.getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType) genType).getActualTypeArguments();
        final Type finalNeedType = types[0];
        KLog.d(Tag, "FinalNeedType:" + types[0]);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = new JsonFactory().createParser(rawJsonData);
        BaseJson bj = (BaseJson) objectMapper.readValues(jsonParser, new TypeReference<Object>() {
            @Override
            public Type getType() {
                return finalNeedType;
            }
        }).next();

        String down = bj.getDown();
        String url = bj.getUrl();
        if (down != null && down.equals("1")) {
            Message msg = new Message();
            msg.obj = url;
            new DownHandler(context.getMainLooper()).sendMessage(msg);
            return null;
        }
        String isSuccess = bj.getBoolen();
        String isLogin = bj.getLogined();
        try {
            if (isSuccess != null && !isSuccess.equals("1")
                    && isLogin != null && isLogin.equals("0")) {
                MemorySave.MS.mIsLogin = false;
                String nameStr = GoClassUtil.goClass;
                GoClassUtil.goClass = null;
                Message msg = new Message();
                msg.obj = nameStr;
                new LoginHandler(context.getMainLooper()).sendMessage(msg);
            }
            return (Json_Type) bj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Json_Type) bj;
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
            GoLoginUtil.ToLoginActivityForResultBase(context, Constants.REQUEST_CODE, nameStr != null ? nameStr : "");
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
            Intent intent = new Intent(context, MainWebActivity.class);
            intent.putExtra("web_url", url);
            intent.putExtra("need_header", "0");
            intent.putExtra("down", "1");
            context.startActivity(intent);
        }
    }

    private void processErrorMsg(int statusCode) {
        String message;
        // 接口请求失败处理
        switch (statusCode) {
            case 404:
                message = "页面不存在";
                break;
            case 502:
                message = "服务区开小差了";
                break;
            default:
                message = "未知错误";
                break;
        }
        KLog.i(Tag, message);
        if (null != callback) {
            callback.onError(message);
        }
    }
}
