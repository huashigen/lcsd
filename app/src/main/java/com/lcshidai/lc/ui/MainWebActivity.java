package com.lcshidai.lc.ui;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.GainCodeImpl;
import com.lcshidai.lc.service.account.HttpGainCodeService;
import com.lcshidai.lc.ui.account.AccountCenterActivity;
import com.lcshidai.lc.ui.account.FinancialCashActivity;
import com.lcshidai.lc.ui.account.ManageFinanceListInfoActivity;
import com.lcshidai.lc.ui.account.MyRewardActivity;
import com.lcshidai.lc.ui.account.pwdmanage.UserPwdManageActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.more.InviteActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.CookieUtil;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.GoClassUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.NetUtils;
import com.lcshidai.lc.utils.OneKeyShareUtil;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.TestLogin;
import com.lcshidai.lc.widget.ppwindow.RedMoneyPW;
import com.lcshidai.lc.widget.ppwindow.RedMoneyPW.CallBackShare;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQ;

public class MainWebActivity extends TRJActivity implements OnClickListener,
        CallBackShare, GainCodeImpl {
    private String web_url = "wireless/index.html#ac=";
    private String action_url = "";
    private String final_url = "";
    public String shareRedUrl = "Mobile2/Share/shareSharkBonus";
    private String getActionUrl = "";
    private String loadTitle;           // webView 加载的标题
    private String loadUrl;             // webView 加载的URL地址
    private String loadActionBtnName;   // webView 底部导航栏右边按钮标题

    // 控件定义
    private View mainView;
    private View actionbar;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    private ImageButton mBackBtn;
    private WebView mWebView;

    public WebViewTask mWebViewTask;
    private OneKeyShareUtil oneKeyShareUtil;
    private HttpGainCodeService hgcs;
    private RedMoneyPW redMoneyPw;

    private long lastTime;
    private String appVersion;          // app版本号
    private String channel = "";        // 渠道名
    private String brand;               // 手机品牌
    private String deviceId = "";       // 设备ID

    private boolean isRedShow = false;
    private boolean mFlag = false;
    private boolean isMyReword = false;
    private boolean isDialogShow = false;// 是否直接返回
    private String order_id;
    private String need_header = "1";
    private boolean showHead;
    private boolean goBack;//是否返回投资标的页面

    private String down = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_web);
        // web 打开
        MemorySave.MS.mWebOpenAppFlag = true;
        appVersion = CommonUtil.getVersionName(mContext, mContext.getPackageName());
        channel = CommonUtil.getTDChannel(mContext, mContext.getPackageName());
        deviceId = CommonUtil.getDeviceId(mContext);
        brand = CommonUtil.getDeviceBrand();

        down = getIntent().getStringExtra("down");
        need_header = getIntent().getStringExtra("need_header");
        loadUrl = getIntent().getStringExtra("web_url");
        loadTitle = getIntent().getStringExtra("title");
        order_id = getIntent().getStringExtra("order_id");
        isRedShow = getIntent().getBooleanExtra("isRedShow", false);
        isMyReword = getIntent().getBooleanExtra("isMyReword", false);
        goBack = getIntent().getBooleanExtra("goBack", false);
        if (null != loadUrl) {
            if (loadUrl.indexOf("http") == -1) {
                final_url = LCHttpClient.BASE_WAP_HEAD + web_url + loadUrl;
            } else {
                final_url = loadUrl;
            }
            if (final_url.indexOf("?") == -1 && final_url.indexOf("#") == -1) {
                final_url = final_url + "?";
            }
        }

        initView();

        hgcs = new HttpGainCodeService(this, this);
        redMoneyPw = new RedMoneyPW(MainWebActivity.this, this);
        oneKeyShareUtil = new OneKeyShareUtil(this);
        mWebViewTask = new WebViewTask();
        mWebViewTask.execute();

        SpUtils.setParam(SpUtils.Table.DEFAULT, "webUrl", "");
        SpUtils.setParam(SpUtils.Table.DEFAULT, "webTitle", "");
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        // view and event bind
        mainView = findViewById(R.id.main);
        actionbar = findViewById(R.id.actionbar);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mWebView = (WebView) findViewById(R.id.main_wb_webview);
        // 自动化测试代码
//        WebView.setWebContentsDebuggingEnabled(true);

        mBackBtn.setOnClickListener(this);

        if (need_header != null && need_header.equals("0")) {
            actionbar.setVisibility(View.GONE);
            showHead = true;
        }
        // 根据加载的地址来设置右边按钮的具体显示
        if (loadUrl != null) {
            if (loadUrl.contains("addBank")) {
                // 充值
                mSaveBtn.setBackgroundColor(Color.TRANSPARENT);
                mSaveBtn.setPadding(0, 0, DensityUtil.dip2px(mContext, 10), 0);
                loadActionBtnName = "";//去掉明细
                mSaveBtn.setTextColor(getResources().getColor(R.color.black));
                mSaveBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_recharge = new Intent();
                        intent_recharge.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/moneyRecord/50");
                        intent_recharge.putExtra("title", "充值明细");
                        intent_recharge.setClass(MainWebActivity.this, MainWebActivity.class);
                        startActivity(intent_recharge);
                    }
                });
                mTvTitle.setText("充值");
            } else if (loadUrl.contains("shop")) {
                mSaveBtn.setBackgroundColor(Color.TRANSPARENT);
                mSaveBtn.setPadding(0, 0, DensityUtil.dip2px(mContext, 10), 0);
                mSaveBtn.setTextColor(getResources().getColor(R.color.black));
                loadActionBtnName = "关闭";
                mSaveBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                if (!CommonUtil.isNullOrEmpty(loadTitle)) {
                    mTvTitle.setText(loadTitle);
                } else {
                    mTvTitle.setText("理财时代");
                }
            } else {
                if (!CommonUtil.isNullOrEmpty(loadTitle)) {
                    mTvTitle.setText(loadTitle);
                } else {
                    mTvTitle.setText("理财时代");
                }
            }
        } else {
            if (CommonUtil.isNullOrEmpty(loadTitle)) {
                mTvTitle.setText("理财时代");
            }
        }
        if (!CommonUtil.isNullOrEmpty(loadActionBtnName)) {
            mSaveBtn.setVisibility(View.VISIBLE);
            mSaveBtn.setText(loadActionBtnName);
        } else {
            mSaveBtn.setVisibility(View.GONE);
        }

        mSubTitle.setVisibility(View.GONE);

//        cookieUtil = new CookieUtil(mContext);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (final_url.contains("addBank")) {
                    mWebView.loadUrl("javascript:clearFormData()");
                }
//                finish();
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (down != null && down.equals("1")) {
//        		exit();
        } else {
            goBack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (down != null && down.equals("1")) {
//        		exit();
            } else {
                goBack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goBack() {
        try {
            if (mWebView.canGoBack()) {
                if (isDialogShow) {// 判断wap弹出对话框
                    mWebView.loadUrl("javascript:appCallPopJS()");
                } else {
                    if (isMyReword) {// 判断从我的奖励入口
                        mWebView.loadUrl("javascript:goBackRefresh()");
                        mWebView.goBack();
                    } else {
                        mWebView.goBack();
                    }
                }
            } else {
                if (isRedShow) {
                    redMoneyPw.show(findViewById(R.id.main));
                } else {
                    MainWebActivity.this.finish();
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MemorySave.MS.mIsFinanceInfoFinish
                || MemorySave.MS.isBidSuccessBack
                || MemorySave.MS.mIsGoFinanceRecord) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        mWebViewTask.cancel(true);
        mWebView.destroy();
        super.onDestroy();
    }

    public class WebViewTask extends AsyncTask<Void, Void, Boolean> {
//        String sessionCookie;
//        CookieManager cookieManager;

        @Override
        protected void onPreExecute() {
            CookieSyncManager.createInstance(mContext);
//            cookieManager = CookieManager.getInstance();
//
//            try {
//                sessionCookie = cookieUtil.getCookie();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (sessionCookie != null) {
//                // delete old cookies
//                cookieManager.removeSessionCookie();
//            }
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... param) {
            // this is very important - THIS IS THE HACK
//            SystemClock.sleep(1000);
            return false;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
        @Override
        protected void onPostExecute(Boolean result) {
            CookieUtil.syncCookiesToWeb(final_url, showHead);
            mWebView.setScrollbarFadingEnabled(true);
            mWebView.getSettings().setPluginState(PluginState.ON);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mWebView.getSettings().setBlockNetworkImage(true);
            // 不使用浏览器缓存
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            String ua = mWebView.getSettings().getUserAgentString();
            if (!ua.contains("app_trj_android")) {
                mWebView.getSettings().setUserAgentString(ua + ";ua_lcsd_android");
            }
            // 添加LocalStorage支持
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setDatabaseEnabled(true);
            String dir = mContext.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
            mWebView.getSettings().setDatabasePath(dir);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setGeolocationEnabled(true);

            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setDefaultTextEncodingName("UTF-8");
            // 设置可以访问文件
            webSettings.setAllowFileAccess(true);
            // 设置支持缩放
            webSettings.setBuiltInZoomControls(false);
            webSettings.setSavePassword(false);
            webSettings.setSaveFormData(false);

            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            try {
                if (Build.VERSION.SDK_INT >= 21) {
                    //允许混合内容，5.0之后的api
                    webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // js调用java
            mWebView.addJavascriptInterface(new CustomNativeAccess(), "mainweb");
            mWebView.addJavascriptInterface(new InJavaScriptLocalObj(),
                    "local_obj");
            mWebView.addJavascriptInterface(new CustomNativeAccess(),
                    "recharge");
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed(); // 接受证书
//					super.onReceivedSslError(view, handler, error);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description,
                            failingUrl);
//					dialogPopupWindow.showWithMessage(description, "1");
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//					if (!loading.isShowing()) {
//						loading.show();
//					}
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    // history();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
//					if (loading.isShowing()) {
//						try {
//							loading.dismiss();
//						} catch (IllegalArgumentException e) {
//							e.printStackTrace();
//						}
//					}
//					mWebView.loadUrl("javascript:window.local_obj.showSource('<html xmlns=\"http://www.w3.org/1999/xhtml\">'+"
//							+ "document.getElementsByTagName('html')[0].innerHTML+'</html>');");
//					mWebView.loadUrl("javascript:isHasPopJS()");
                    mWebView.getSettings().setBlockNetworkImage(false);
                    CookieManager cookieManager = CookieManager.getInstance();
                    String strCookie = cookieManager.getCookie(url);
                    KLog.e("cookie", strCookie);

                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        CookieUtil.syncCookiesToWeb(url, showHead);
                        return false;
                    }
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                    CookieUtil.syncCookiesToWeb(url, showHead);
                    return true;
                }

            });
            mWebView.setWebChromeClient(new MyWebChromeClient());
            mWebView.setDownloadListener(new MyDownLoadListener());

            mWebView.loadUrl(final_url);
        }
    }

    class MyDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            if (url.endsWith(".apk")) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }
    }

    /**
     * android sdk api >= 17 时需要加
     *
     * @author fei
     */
    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            try {
                Document doc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .parse(new InputSource(new StringReader(html)));
                NodeList str = doc.getElementsByTagName("body");
                Node node = str.item(0);
                String s = node.getChildNodes().item(0).getNodeValue();
                JSONObject jobject = new JSONObject(s);
                String boolen = jobject.getString("boolen");
                String logined = jobject.getString("logined");
                if (boolen.equals("0") && logined.equals("0")) {
                    Intent intent = new Intent();
                    intent.setClass(MainWebActivity.this, LoginActivity.class);
                    startActivity(intent);
                    MainWebActivity.this.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Js回调类
     *
     * @author 000853
     */
    public class CustomNativeAccess {

        // 回退
        @JavascriptInterface
        public void appGoBack() {
            MainWebActivity.this.finish();
        }

        // 理财
        @JavascriptInterface
        public void goFinances() {
            //如果是从项目页面过来的直接关闭充值页面就返回到了项目页面
            if (goBack) {
                finish();
            } else {
                goFinance();
            }
        }

        // 充值页面
        @JavascriptInterface
        public void goRecharge() {
            Intent intent = new Intent();
            if (MemorySave.MS.mIsLogin) {
                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
                intent.setClass(mContext, MainWebActivity.class);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("main_web_flag", 20);
                intent.putExtras(bundle);
                intent.setClass(mContext, LoginActivity.class);
                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
                intent.putExtra("goClass", MainWebActivity.class.getName());
            }
            startActivity(intent);
        }

        // 登陆页面（action）
        @JavascriptInterface
        public void goLuckDraw(String url) {
            String u = GoLoginUtil.getU(MainWebActivity.this);
            boolean isShowGestureLogin = GoLoginUtil.isShowGestureLogin(MainWebActivity.this);
            boolean isNetworkConnected = NetUtils.isNetworkConnected(mContext);
            if (!u.equals("") && isShowGestureLogin && isNetworkConnected) {
                GoLoginUtil.goGestureLoginActivityForResult(MainWebActivity.this, null, 10);
            } else {
                //			getActionUrl = url;
//    			getActionUrl = url.replace(LCHttpClient.RELEASEE_URL_WAP, "");
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("main_web_flag", 20);
                intent.putExtras(bundle);
                intent.setClass(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, 10);
//    			if (!MemorySave.MS.mIsLogin) {
//    			} else {
//    				mWebView.loadUrl(LCHttpClient.RELEASEE_URL_WAP + web_url
//    						+ getActionUrl);
//    			}
            }
        }

        // 登陆页面（action）
        @JavascriptInterface
        public void goLuckDraw() {
            String u = GoLoginUtil.getU(MainWebActivity.this);
            boolean isShowGestureLogin = GoLoginUtil.isShowGestureLogin(MainWebActivity.this);
            boolean isNetworkConnected = NetUtils.isNetworkConnected(mContext);
            if (!u.equals("") && isShowGestureLogin && isNetworkConnected) {
                GoLoginUtil.goGestureLoginActivityForResult(MainWebActivity.this, null, 10);
            } else {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("main_web_flag", 20);
                intent.putExtras(bundle);
                intent.setClass(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, 10);
            }
        }

        // 登陆页面(跳转url)
        @JavascriptInterface
        public void goLogin(String url) {
            getActionUrl = url.replace(LCHttpClient.BASE_WAP_HEAD, "");
            if (!MemorySave.MS.mIsLogin) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("main_web_flag", 20);
                intent.putExtras(bundle);
                intent.setClass(mContext, LoginActivity.class);
                startActivityForResult(intent, 11);
            } else {
                if (getActionUrl.indexOf("?") == -1
                        && getActionUrl.indexOf("#") == -1) {
                    mWebView.loadUrl(LCHttpClient.BASE_WAP_HEAD + getActionUrl
                            + "?");
                } else {
                    mWebView.loadUrl(LCHttpClient.BASE_WAP_HEAD + getActionUrl);
                }
            }
        }

        // 登陆页面
        @JavascriptInterface
        public void goLoginRefresh() {
            if (!MemorySave.MS.mIsLogin) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("main_web_flag", 20);
                intent.putExtras(bundle);
                intent.setClass(mContext, LoginActivity.class);
                startActivityForResult(intent, 12);
            }
        }

        // 分享功能
        @JavascriptInterface
        public void toShare(String content) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("content", content);
            msg.setData(bundle);
            msg.what = 1;
            shareHandler.sendMessage(msg);
        }

        // 分享功能
        @JavascriptInterface
        public void toNewShare(String content, String imgurl, String shareurl,
                               String title) {
            ShareAddImgModel shareModel = new ShareAddImgModel();
            shareModel.imgUrl = imgurl;
            shareModel.sharemes = content;
            shareModel.title = title;
            shareModel.shareUrl = shareurl;
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("content", content);
            msg.setData(bundle);
            msg.what = 4;
            msg.obj = shareModel;
            shareHandler.sendMessage(msg);
        }

        // 分享功能
        @JavascriptInterface
        public void toShare() {
            Message msg = new Message();
            msg.what = 3;
            shareHandler.sendMessage(msg);
        }

        // 充值携带URL
        @JavascriptInterface
        public void goRecharge2(String url) {
            getActionUrl = url;
            Intent intent = new Intent();
            if (MemorySave.MS.mIsLogin) {
                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
                intent.setClass(mContext, MainWebActivity.class);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("main_web_flag", 20);
                intent.putExtras(bundle);
                intent.setClass(mContext, LoginActivity.class);
                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
                intent.putExtra("goClass", MainWebActivity.class.getName());
            }
            startActivityForResult(intent, 30);
        }

        // 理财短期宝产品列表
        @JavascriptInterface
        public void goFinanceCZ() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            // 短期宝筛选
            MemorySave.MS.mGoFinancetype = 1;

            startActivity(intent);
            MainWebActivity.this.finish();
        }

        // 新客专享
        @JavascriptInterface
        public void goNewGuest() {
            if (MemorySave.MS.mIsLogin
                    && (null != MemorySave.MS.userInfo.is_newbie && !"1"
                    .equals(MemorySave.MS.userInfo.is_newbie))) {
                showToast("您已经不是新客用户，感谢您的支持");
            } else {
                Intent intent = new Intent(mContext, MainActivity.class);
                MemorySave.MS.mIsGoHome = true;
                startActivity(intent);
                MainWebActivity.this.finish();
            }
        }

        // 注册页面
        @JavascriptInterface
        public void goRegister() {
            if (!MemorySave.MS.mIsLogin) {
                Intent intent = new Intent(mContext,
                        UserRegisterFirActivity.class);
                startActivity(intent);
                MainWebActivity.this.finish();
            } else {
                shareHandler.sendEmptyMessage(2);
            }
        }

        // 账户设置界面 实名认证 绑定邮箱 添加银行卡
        @JavascriptInterface
        public void goUserSetting() {
            Intent setting_intent = new Intent();
            if (MemorySave.MS.mIsLogin) {
                setting_intent.setClass(mContext, AccountCenterActivity.class);
            } else {
                setting_intent.setClass(mContext, LoginActivity.class);
                setting_intent.putExtra("goClass",
                        AccountCenterActivity.class.getName());
            }
            startActivity(setting_intent);
            MainWebActivity.this.finish();
        }

        // 理财长期宝产品列表
        @JavascriptInterface
        public void goFinanceWY() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            // 长期宝筛选
            MemorySave.MS.mGoFinancetype = 2;

            startActivity(intent);
            MainWebActivity.this.finish();
        }

        // 理财超益宝产品列表
        @JavascriptInterface
        public void goFinanceJYB() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            // 超益宝筛选
            MemorySave.MS.mGoFinancetype = 3;

            startActivity(intent);
            MainWebActivity.this.finish();
        }

        // 转让市场
        @JavascriptInterface
        public void goTransfer() {
            // Intent intent = new Intent(mContext, MainActivity.class);
            // MemorySave.MS.mIsGoTransfer = true;
            // startActivity(intent);
            // MainWebActivity.this.finish();
        }

        // 财富中心
        @JavascriptInterface
        public void goAccount() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoAccount = true;
            startActivity(intent);
            MainWebActivity.this.finish();
        }

        // 理财列表
        @JavascriptInterface
        public void goFinance() {
            if (loadUrl.equals(LCHttpClient.BASE_WAP_HEAD + "/#/addBank"))
                MemorySave.MS.isRechargeSuccessToFinance = true;
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            MemorySave.MS.mIsGoFinanceHome = true;
            // hayb筛选
            MemorySave.MS.mGoFinancetype = 0;

            startActivity(intent);
            MainWebActivity.this.finish();
        }

        // 投资记录
        @JavascriptInterface
        public void goFinanceRecord() {
            Intent finance_intent = new Intent();
            if (MemorySave.MS.mIsLogin) {
                finance_intent.setClass(mContext,
                        ManageFinanceListInfoActivity.class);
            } else {
                finance_intent.setClass(mContext, LoginActivity.class);
                finance_intent.putExtra("goClass",
                        ManageFinanceListInfoActivity.class.getName());
            }
            startActivity(finance_intent);
            MainWebActivity.this.finish();
        }

        // 邀请好友
        @JavascriptInterface
        public void goInvite() {
            Intent invite_intent = new Intent();
            if (!MemorySave.MS.mIsLogin) {
                Bundle bundle = new Bundle();
                bundle.putInt("main_web_flag", 20);
                invite_intent.putExtras(bundle);
                invite_intent.setClass(mContext, LoginActivity.class);
                startActivityForResult(invite_intent, 12);
            } else {
                invite_intent.setClass(mContext, InviteActivity.class);
                invite_intent.putExtra("main_web_flag", 22);
                startActivityForResult(invite_intent, 22);
            }
        }

        // 理财详情页面
        @JavascriptInterface
        public void goFinanceInfo(String prj_id) {
            Bundle args = new Bundle();
            args.putString("prj_id", prj_id);
//            getIntent().putExtra("goClass",
//                    ManageFinanceInfoActivity_2.class.getName());
            GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
            GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
            MemorySave.MS.args = args;
            new TestLogin(MainWebActivity.this).testIt(MainWebActivity.this);
        }

        // 理财详情页面
        @JavascriptInterface
        public void goFinanceInfoLimit(String prj_id, String money) {
            Bundle args = new Bundle();
            args.putString("prj_id", prj_id);
            args.putString("limitMoney", money);
//            getIntent().putExtra("goClass",
//            		FinanceProjectDetailActivity.class.getName());
            GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
            GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
            MemorySave.MS.args = args;
            new TestLogin(MainWebActivity.this).testIt(MainWebActivity.this);
        }

        // 活动页面弹出对话框判断isCanGoBack值绑定
        @JavascriptInterface
        public void bindPopJS() {
            isDialogShow = true;
        }

        // 我的奖励改标题
        @JavascriptInterface
        public void alterTitle(String title, String flag, String isNative) {
            mFlag = Boolean.parseBoolean(flag);
            if (mFlag) {// 显示副标题
                Message msg = new Message();
                msg.what = 6;
                msg.obj = title;
                shareHandler.sendMessage(msg);

            } else {// 不显示副标题
                Message msg = new Message();
                msg.what = 5;
                msg.obj = title;
                shareHandler.sendMessage(msg);

            }

            if (Boolean.parseBoolean(isNative)) {
                MainWebActivity.this.finish();
            }
        }

        // 历史流水
        @JavascriptInterface
        public void rewardHistory() {
            Intent intent = new Intent(mContext, MyRewardActivity.class);
            intent.putExtra("switch_title", true);
            intent.putExtra("switch_wap", "2");
            startActivity(intent);
        }

        // 打印错误提示
        @JavascriptInterface
        public void showError(String message) {
//			dialogPopupWindow.showWithMessage(message, "1");
        }

        @JavascriptInterface
        public void resetPayPassword() {
            Intent intent = new Intent(MainWebActivity.this, UserPwdManageActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void goTelCheng() {
            Intent intent = new Intent(MainWebActivity.this, ModifyPreLeftPhoneNumberActivity.class);
            intent.putExtra(ModifyPreLeftPhoneNumberActivity.ENTRANCE, MainWebActivity.class.getSimpleName());
            startActivity(intent);
        }

        @JavascriptInterface
        public void goBack() {
            finish();
        }

        @JavascriptInterface
        public void goGoldFinance() {
            Intent intent = new Intent(MainWebActivity.this, FinancialCashActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public String getAppVersionName() {
            return CommonUtil.getVersionName(mContext, mContext.getPackageName());
        }

        @JavascriptInterface
        public void goScoreStore(String url) {
            Intent intent = new Intent(mContext, MainWebActivity.class);
            intent.putExtra("web_url", url);
            intent.putExtra("title", "积分商城");
            intent.putExtra("need_header", "1");
            MemorySave.MS.isGoLast = true;
            startActivity(intent);
        }

        /**
         * web 跳转到App的web页面
         *
         * @param absoluteUrl 页面的绝对url路径
         * @param title       页面的标题
         */
        @JavascriptInterface
        public void goWebPageInApp(String absoluteUrl, String title) {
            Intent intent = new Intent(mContext, MainWebActivity.class);
            intent.putExtra("web_url", absoluteUrl);
            intent.putExtra("title", title);
            intent.putExtra("need_header", "1");
            startActivity(intent);
        }

        @JavascriptInterface
        public void goAccountScrow() {
            Intent intent = new Intent(mContext, CunGuanAccountActivity.class);
            startActivity(intent);
            finish();
        }

    }

    Handler shareHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String content = msg.getData().getString("content");
                    if (System.currentTimeMillis() - lastTime > 500
                            && !"".equals(content)) {
                        lastTime = System.currentTimeMillis();
                        oneKeyShareUtil.toShare(content,
                                new PlatformActionListener() {

                                    @Override
                                    public void onError(Platform arg0, int arg1,
                                                        Throwable arg2) {
                                        showToast("分享失败");
                                    }

                                    @Override
                                    public void onComplete(Platform arg0, int arg1,
                                                           HashMap<String, Object> arg2) {
                                        if (!arg0.getName().equals(QQ.NAME)) {
                                            showToast("分享成功");
                                        }
                                    }

                                    @Override
                                    public void onCancel(Platform arg0, int arg1) {

                                    }
                                });
                    }
                    break;
                case 2:
                    showToast("您已注册，谢谢参与");
                    break;
                case 3:
                    toSharered();
                    break;
                case 4:
                    ShareAddImgModel model = (ShareAddImgModel) msg.obj;
                    oneKeyShareUtil.toShareForImageUrlMSG("", model.sharemes, model.title,
                            model.shareUrl, model.imgUrl,
                            new PlatformActionListener() {

                                @Override
                                public void onError(Platform arg0, int arg1,
                                                    Throwable arg2) {
                                    showToast("分享失败");
                                }

                                @Override
                                public void onComplete(Platform arg0, int arg1,
                                                       HashMap<String, Object> arg2) {
                                    if (!arg0.getName().equals(QQ.NAME)) {
                                        showToast("分享成功");
                                    }
                                }

                                @Override
                                public void onCancel(Platform arg0, int arg1) {

                                }
                            });
                    break;
                case 5:
                    mTvTitle.setText((String) msg.obj);
                    mSaveBtn.setVisibility(View.INVISIBLE);
                    break;
                case 6:
                    mTvTitle.setText((String) msg.obj);
                    mSaveBtn = (Button) findViewById(R.id.btn_option);
                    mSaveBtn.setBackgroundColor(Color.TRANSPARENT);
                    mSaveBtn.setPadding(0, 0, DensityUtil.dip2px(mContext, 10), 0);
                    mSaveBtn.setVisibility(View.VISIBLE);
                    mSaveBtn.setText("历史流水记录");
                    mSaveBtn.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,
                                    MyRewardActivity.class);
                            intent.putExtra("switch_title", true);
                            intent.putExtra("switch_wap", "2");
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_SELECT_FILE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else {
            String url = mWebView.getUrl();
            CookieUtil.syncCookiesToWeb(url, showHead);
            mWebView.reload();
        }
    }

    /**
     * 红包分享
     */
    private void toSharered() {
        createImageCacheDir();
        RequestParams rq = new RequestParams();
        rq.put("id", order_id);
        post(shareRedUrl, rq, new JsonHttpResponseHandler(MainWebActivity.this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response != null) {
                        if (response.getInt("boolen") == 1) {
                            JSONObject data = response.optJSONObject("data");
                            String qrcode = data.optString("qrcode");// 图片地址
                            String title = data.optString("title");// 标题
                            String content = data.optString("content");// 分享内容
                            String url = data.optString("url");// 链接
                            if (qrcode.contains("https://"))
                                FILE_NAME = qrcode.replace("https://", "");
                            if (qrcode.contains("http://"))
                                FILE_NAME = qrcode.replace("http://", "");
                            FILE_NAME = FILE_NAME.replace("/", "_");
                            downloadImage(qrcode, content, title, url);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
            }
        });
    }

    private void hongBaoShare(String content, String title, String qrcode) {
        oneKeyShareUtil.toShare(content, title, qrcode, bannerCacheDir
                + FILE_NAME, new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                showToast("分享失败");
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                if (!arg0.getName().equals(QQ.NAME)) {
                    showToast("分享成功");
                }
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {

            }

        });
    }

    @Override
    public void share() {
        toSharered();
    }

    private String bannerCacheDir = "";
    private String FILE_NAME = "";

    /**
     * 创建图片缓存的目录
     */
    private void createImageCacheDir() {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                // 创建一个文件夹对象，赋值为外部存储器的目录
                File sdcardDir = Environment.getExternalStorageDirectory();
                // 得到一个路径，内容是sdcard的文件夹路径和名字
                bannerCacheDir = sdcardDir.getPath() + "/trj/temp/";
                File file_path = new File(bannerCacheDir);
                if (!file_path.exists()) {
                    // 若不存在，创建目录，可以在应用启动的时候创建
                    file_path.mkdirs();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadImage(final String url, final String content,
                               final String title, final String qrcode) {
        File cacheFile = new File(bannerCacheDir + FILE_NAME);
        if (cacheFile.exists()) {
            hongBaoShare(content, title, qrcode);
        } else {
            hgcs.getBitmapData(url, content, title, qrcode);
        }
    }

    public class ShareAddImgModel {
        public String title;
        public String shareUrl;
        public String imgUrl;
        public String sharemes;
    }

    @Override
    public void gainBitmapDatasuccess(byte[] binaryData, String url,
                                      String content, String title, String qrcode) {
        if (null != binaryData && binaryData.length > 0) {
            try {
                File file = new File(bannerCacheDir, FILE_NAME);
                if (!file.exists()) {
                    file.createNewFile();

                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(binaryData, 0, binaryData.length);
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                hongBaoShare(content, title, qrcode);
            }
        }
    }

    @Override
    public void gainBitmapDatafail() {

    }

    /**
     * webView 上传图片
     */
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 101;

    class MyWebChromeClient extends WebChromeClient {
        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
        }


        // For Lollipop 5.0+ Devices
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;

            Intent intent = fileChooserParams.createIntent();
            try {
                startActivityForResult(intent, REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e) {
                uploadMessage = null;
                Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        //For Android 4.1 only
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
        }

        protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }
    }

}
