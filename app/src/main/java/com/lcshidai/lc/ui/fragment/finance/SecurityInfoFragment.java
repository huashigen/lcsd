package com.lcshidai.lc.ui.fragment.finance;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.CookieUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.StringUtils;

/**
 * 安全保障
 */
public class SecurityInfoFragment extends TRJFragment {

    private String mPrjId;
    private int isCollection;
    private String final_url = "";
    private CookieUtil cookieUtil;

    private WebView mWebView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPrjId = args.getString(Constants.Project.PROJECT_ID);
            isCollection = args.getInt(Constants.Project.IS_COLLECTION);
            final_url = LCHttpClient.BASE_WAP_HEAD + "/#/insurance" + "/" + mPrjId +
                    "?is_collection=" + isCollection;
        }
    }

    @SuppressLint("InlinedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_web, container, false);
        mWebView = (WebView) contentView.findViewById(R.id.main_wb_webview);
        initView();
        MemorySave.MS.mWebOpenAppFlag = true;
        return contentView;

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        cookieUtil = new CookieUtil(getActivity());
        mWebView.setScrollbarFadingEnabled(true);
        // 不使用浏览器缓存
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // mWebView.getSettings().setPluginsEnabled(true);
        mWebView.getSettings().setPluginState(PluginState.ON);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // history();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//				if (loading.isShowing()) {
//					loading.dismiss();
//				}
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
            }
        });

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.clearCache(true);
        mWebView.clearHistory();
        setCookies(mContext);
        mWebView.loadUrl(final_url);
    }

    public void setCookies(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        CookieSyncManager.getInstance().startSync();
        cookieManager.removeSessionCookie();// 移除
        String sessionCookie = "";
        try {
            sessionCookie = cookieUtil.getCookie();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(sessionCookie)) {
            cookieManager.setCookie(LCHttpClient.BASE_WAP_HEAD, sessionCookie);
        }

        cookieManager.setCookie(LCHttpClient.BASE_WAP_HEAD, "TRPApp=lcshidai");
        cookieManager.setCookie(LCHttpClient.BASE_WAP_HEAD, "TRPClient=android");
        CookieSyncManager.getInstance().sync();
    }

    public static Dialog createLoadingDialog(Context context, String msg, boolean cancelAble) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_custmor_loading, null);// 得到加载view
        TextView tipTextView = (TextView) view.findViewById(R.id.custmor_loadding_tv_message);// 提示文字
        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.style_loading_dialog);// 创建自定义样式dialog
        loadingDialog.setContentView(view);
        loadingDialog.setCancelable(cancelAble);
        return loadingDialog;
    }
}