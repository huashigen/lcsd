package com.lcshidai.lc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.lcshidai.lc.http.LCHttpClient;

import org.apache.http.cookie.Cookie;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 存取cookie的公共类
 *
 * @author 000853
 */
public class CookieUtil {
    private SharedPreferences sp = null;
    private static final String Domain = ";domain=.tourongjia.com";
    private static final String Path = ";path=/";

    public CookieUtil(Context context) {
        sp = context.getSharedPreferences("cookie_cache", Context.MODE_PRIVATE);
    }

    public void saveCookie(String strCookie) throws Exception {
        if (MemorySave.MS.userInfo != null) {
            String saveCookie = getCookie();
            if (!saveCookie.equals(strCookie)) {
                sp.edit().putString(LCHttpClient.BASE_WAP_HEAD + "_" + MemorySave.MS.userInfo.uname, strCookie).apply();
            }
        }
    }

    public String getCookie() throws Exception {
        if (MemorySave.MS.userInfo != null) {
            return sp.getString(LCHttpClient.BASE_WAP_HEAD + "_" + MemorySave.MS.userInfo.uname, "");
        } else {
            return "";
        }
    }

    public void clearCookie() throws Exception {
        if (MemorySave.MS.userInfo != null) {
            sp.edit().putString(LCHttpClient.BASE_WAP_HEAD + "_" + MemorySave.MS.userInfo.uname, "").apply();
        }
    }

    public static void syncCookiesToWeb(final String url, boolean showHead) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        List<Cookie> cookies = LCHttpClient.getCookie();
        final String Domain = ";domain=" + getDomain(url);
        for (Cookie cookie : cookies) {
            cookieManager.setCookie(url, cookie.getName() + "=" + cookie.getValue() + Domain + Path);
        }
        cookieManager.setCookie(url, "TRPApp=lcshidai" + Domain + Path);
        cookieManager.setCookie(url, "TRPClient=android" + Domain + Path);
        if (showHead) {
            cookieManager.setCookie(url, "TRPHeader=showHead" + Domain + Path);
        }
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }

    private static String getDomain(final String url) {
        String domain = null;
        try {
            //获取完整的域名
            Pattern p = Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(url);
            matcher.find();
            domain = matcher.group();
            return domain;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return domain;
    }

}
