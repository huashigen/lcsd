package com.lcshidai.lc.http;

import android.annotation.SuppressLint;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;
import com.lcshidai.lc.utils.CommonUtil;

import org.apache.http.HttpEntity;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;

import java.util.List;

public class LCHttpClient {

    //    测试
    public static String BASE_API_HEAD = "http://uatweb.lcshidai.com/";
    public static String BASE_WAP_HEAD = "http://uatwap.lcshidai.com/";
//    public static String BASE_API_HEAD = "http://192.168.1.102:9393/";
//    public static String BASE_WAP_HEAD = "http://192.168.1.102:9393";
//  生产
//    public static String BASE_API_HEAD = "https://www.lcshidai.com/";
//    public static String BASE_WAP_HEAD = "https://m.lcshidai.com/";

    private static AsyncHttpClient ASYNC_HTTP_CLIENT = new AsyncHttpClient();

    private static AsyncHttpClient ANOTHER_ASYNC_HTTP_CLIENT = new AsyncHttpClient();

    static {
        ASYNC_HTTP_CLIENT.setTimeout(200000);
    }

    public static void getWithFullUrl(String absoluteUrl, RequestParams params,
                                      AsyncHttpResponseHandler responseHandler) {
        KLog.d("request", String.format("%s&%s", absoluteUrl, params));
        ANOTHER_ASYNC_HTTP_CLIENT.get(absoluteUrl, params, responseHandler);
    }

    public static void get(Context context, String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        String absoluteUrl = getAbsoluteUrl(url, context);
        ASYNC_HTTP_CLIENT.get(absoluteUrl, params, responseHandler);
    }

    public static void get(AsyncHttpResponseHandler responseHandler, RequestParams params,
                           Context context, String url) {
        get(context, url, params, responseHandler);
    }

    public static void get(Context context, String url, BinaryHttpResponseHandler responseHandler) {
        String absoluteUrl = getAbsoluteUrl(url, context);
        ASYNC_HTTP_CLIENT.get(absoluteUrl, responseHandler);
    }

    public static void getWithFullUrl(String url, BinaryHttpResponseHandler responseHandler) {
        ASYNC_HTTP_CLIENT.get(url, responseHandler);
    }

    public static void clearCookie() {
//    	ASYNC_HTTP_CLIENT.clearCookie();
        ASYNC_HTTP_CLIENT.clearCookieNew();
    }

    public static List<Cookie> getCookie() {
        return ASYNC_HTTP_CLIENT.getCookie();
    }

    public static void post(Context context, String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        String absoluteUrl = getAbsoluteUrl(url, context);
        KLog.d("request", String.format("%s&%s", absoluteUrl, params));
        ASYNC_HTTP_CLIENT.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        ASYNC_HTTP_CLIENT.post(context, absoluteUrl, params, responseHandler);
    }

    public static void post(AsyncHttpResponseHandler responseHandler, RequestParams params,
                            Context context, String url) {
        post(context, url, params, responseHandler);
    }

    public static void postWithFullUrl(Context context, String absoluteUrl, RequestParams params,
                                       AsyncHttpResponseHandler responseHandler) {
        KLog.d("request", String.format("%s&%s", absoluteUrl, params));
        ANOTHER_ASYNC_HTTP_CLIENT.post(context, absoluteUrl, params, responseHandler);
    }

    public static void postWithFullUrl(AsyncHttpResponseHandler responseHandler,
                                       RequestParams params, Context context,
                                       String absoluteUrl) {
        postWithFullUrl(context, absoluteUrl, params, responseHandler);
    }

    public static void post(String url, BinaryHttpResponseHandler responseHandler) {
        String absoluteUrl = url;
        KLog.d("request", String.format("%s", absoluteUrl));
        ASYNC_HTTP_CLIENT.post(absoluteUrl, responseHandler);
    }

    public static void post(BinaryHttpResponseHandler responseHandler, String url) {
        post(url, responseHandler);
    }

    public static void post(Context context, String url, HttpEntity params,
                            AsyncHttpResponseHandler responseHandler) {
        String absoluteUrl = getAbsoluteUrl(url, context);
        KLog.d("request", String.format("%s&%s", absoluteUrl, params));
        ASYNC_HTTP_CLIENT.post(context, absoluteUrl, params, "", responseHandler);
    }

    public static void post(AsyncHttpResponseHandler responseHandler, HttpEntity params,
                            Context context, String url) {
        post(context, url, params, responseHandler);
    }

    @SuppressLint("HardwareIds")
    public static String getAbsoluteUrl(String relativeUrl, Context activity) {
        String version = CommonUtil.getVersionName(activity, activity.getPackageName());
        String deviceId = CommonUtil.getDeviceId(activity);
        String and = relativeUrl.contains("?") ? "&" : "?";
        return BASE_API_HEAD + relativeUrl + and + "app_version=" + version + "&deviceId=" + deviceId;
    }

    /**
     * 取消所有请求
     */
    public static void cancelRequest() {
        if (ASYNC_HTTP_CLIENT != null) {
            ASYNC_HTTP_CLIENT.cancelAllRequests(true);
        }
        if (ANOTHER_ASYNC_HTTP_CLIENT != null) {
            ANOTHER_ASYNC_HTTP_CLIENT.cancelAllRequests(true);
        }
    }
}
