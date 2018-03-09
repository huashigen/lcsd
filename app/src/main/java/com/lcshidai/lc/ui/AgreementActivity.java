package com.lcshidai.lc.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.AgreementImpl;
import com.lcshidai.lc.model.pub.AgreementData;
import com.lcshidai.lc.model.pub.AgreementJson;
import com.lcshidai.lc.service.HttpAgreementService;
import com.lcshidai.lc.ui.account.AccountCenterActivity;
import com.lcshidai.lc.ui.account.ManageFinanceListInfoActivity;
import com.lcshidai.lc.ui.account.MyRewardActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.more.InviteActivity;
import com.lcshidai.lc.utils.CookieUtil;
import com.lcshidai.lc.utils.GoClassUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.TestLogin;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 协议公用
 */
public class AgreementActivity extends TRJActivity implements AgreementImpl {

    private String url = "";
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    private String mTitle, mUrl, mId;
    private WebView webview;
    private View mProgressContainer;
    private String mContent;
    private int intent_flag = 0; // SDT-1
    private String fun, name, type;
    HttpAgreementService as;
    public WebViewTask mWebViewTask;
    Context mContext;
    private String final_url = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intent_flag = bundle.getInt("intent_flag", 0);
            mTitle = bundle.getString("title");
            if (mTitle.equals("")) {
                mId = bundle.getString("id");
                if (intent_flag == 1) {
                    fun = bundle.getString("fun");
                    name = bundle.getString("name");
                    type = bundle.getString("type");
                }
            } else {
                mUrl = bundle.getString("url");
                String urlSub = "";
                if (!TextUtils.isEmpty(mUrl)) {
                    urlSub = "/".equals(mUrl.substring(0, 1)) ? mUrl : ("/" + mUrl);

                }
                url = getAbsoluteUrl(urlSub, AgreementActivity.this);
            }
        }
        final_url = url;
        as = new HttpAgreementService(this, this);
        initView();
        loadData();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        mContext = this;
        webview = (WebView) findViewById(R.id.webview);
        cookieUtil = new CookieUtil(this);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText(mTitle);
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mProgressContainer = findViewById(R.id.progressContainer);
        mProgressContainer.setVisibility(View.GONE);
        mWebViewTask = new WebViewTask();
        mWebViewTask.execute();
    }

    private void loadData() {
        if (mTitle.equals("")) {
            as.gainAgreementResult(intent_flag, mId, fun, name, type);

        } else {
//			setCookies(this);
//			webview.loadUrl(url);
        }
    }

    public String getAbsoluteUrl(String relativeUrl, Context activity) {
        PackageManager manager = activity.getPackageManager();
        PackageInfo pack_info = null;
        try {
            pack_info = manager.getPackageInfo(activity.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String version = pack_info.versionName;
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        String and = relativeUrl.contains("?") ? "&" : "?";
        return LCHttpClient.BASE_API_HEAD + relativeUrl + and + "deviceId=" + deviceId +
                "&app_version=" + version;
//        return LCHttpClient.BASE_WAP_HEAD + relativeUrl + and + "deviceId=" + deviceId +
//                "&app_version=" + version;
    }

    private CookieUtil cookieUtil;

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

    @Override
    public void gainAgreementSuccess(AgreementJson response) {

        mProgressContainer.setVisibility(View.GONE);
        if (response != null) {
            String boolen = response.getBoolen();
            if (boolen.equals("1")) {
                AgreementData json = response.getData();
                mTitle = json.getTitle();
                mTvTitle.setText(mTitle);
                mContent = json.getContent();
                webview.loadDataWithBaseURL(null, mContent, "text/html", "utf-8", null);
            }
        }

    }

    @Override
    public void gainAgreementFail() {
        mProgressContainer.setVisibility(View.GONE);

    }

    public class WebViewTask extends AsyncTask<Void, Void, Boolean> {
        String sessionCookie;
        CookieManager cookieManager;

        @Override
        protected void onPreExecute() {
            CookieSyncManager.createInstance(mContext);
            cookieManager = CookieManager.getInstance();

            try {
                sessionCookie = cookieUtil.getCookie();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sessionCookie != null) {
                // delete old cookies
                cookieManager.removeSessionCookie();
            }
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... param) {
            // this is very important - THIS IS THE HACK
            SystemClock.sleep(1000);
            return false;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
        @Override
        protected void onPostExecute(Boolean result) {
            if (sessionCookie != null) {
                if (!StringUtils.isEmpty(sessionCookie)) {
                    cookieManager.setCookie(LCHttpClient.BASE_API_HEAD, sessionCookie);
                }
                cookieManager.setCookie(LCHttpClient.BASE_API_HEAD, "TRPApp=lcshidai");
                cookieManager.setCookie(LCHttpClient.BASE_API_HEAD, "TRPClient=android");
                CookieSyncManager.getInstance().sync();
            }
            webview.setScrollbarFadingEnabled(true);
            webview.getSettings().setPluginState(PluginState.ON);
            webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webview.getSettings().setBlockNetworkImage(true);
            // 不使用浏览器缓存
            webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

            // 添加LocalStorage支持
            webview.getSettings().setAllowFileAccess(true);
            webview.getSettings().setDatabaseEnabled(true);
            String dir = mContext.getApplicationContext()
                    .getDir("database", Context.MODE_PRIVATE).getPath();
            webview.getSettings().setDatabasePath(dir);
            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setGeolocationEnabled(true);

            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setDefaultTextEncodingName("UTF-8");
            // 设置可以访问文件
            webSettings.setAllowFileAccess(true);
            // 设置支持缩放
            webSettings.setBuiltInZoomControls(false);
            webSettings.setSavePassword(false);
            webSettings.setSaveFormData(false);

            // js调用java
            webview.addJavascriptInterface(new CustomNativeAccess(), "mainweb");
            webview.addJavascriptInterface(new InJavaScriptLocalObj(),
                    "local_obj");
            webview.addJavascriptInterface(new CustomNativeAccess(),
                    "recharge");
            webview.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedSslError(WebView view,
                                               SslErrorHandler handler, SslError error) {
                    handler.proceed(); // 接受证书
                }

                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

                @Override
                public void onPageStarted(WebView view, String url,
                                          Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    // history();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    webview.getSettings().setBlockNetworkImage(false);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains("tel")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                                .parse(url));
                        startActivity(intent);
                    } else {
//						view.loadUrl(url+urlsuffix);
                        view.loadUrl(url);
                    }
                    return true;
                }

            });

            webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Change
                }

            });
            ApplicationInfo app_info = null;
            PackageInfo pack_info = null;
            try {
                app_info = mContext.getPackageManager()
                        .getApplicationInfo(mContext.getPackageName(),
                                PackageManager.GET_META_DATA);
                pack_info = mContext.getPackageManager().getPackageInfo(
                        mContext.getPackageName(), 0);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            appVersion = pack_info.versionName;
            if (null == app_info || null == pack_info) {
//				urlsuffix= "&brand=" + brand + "&deviceId="
//						+ DEVICE_ID;
            } else {
                channel = app_info.metaData.getString("UMENG_CHANNEL");
            }
//			webview.loadUrl(final_url+urlsuffix);
            webview.loadUrl(final_url);

        }
    }

    String appVersion;
    private String channel = "";

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
                    intent.setClass(AgreementActivity.this, LoginActivity.class);
                    startActivity(intent);
                    AgreementActivity.this.finish();
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
            AgreementActivity.this.finish();
        }

        // 理财
        @JavascriptInterface
        public void goFinances() {
            goFinance();
        }

        // 充值页面
        @JavascriptInterface
        public void goRecharge() {
            Intent intent = new Intent();
            if (MemorySave.MS.mIsLogin) {
                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
                intent.setClass(mContext, AgreementActivity.class);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("main_web_flag", 20);
                intent.putExtras(bundle);
                intent.setClass(mContext, LoginActivity.class);
                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/addBank");
                intent.putExtra("goClass", AgreementActivity.class.getName());
            }
            startActivity(intent);
        }

        // 登陆页面（action）
        @JavascriptInterface
        public void goLuckDraw(String url) {
//			getActionUrl = url;
            if (!MemorySave.MS.mIsLogin) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("main_web_flag", 20);
                intent.putExtras(bundle);
                intent.setClass(mContext, LoginActivity.class);
                startActivityForResult(intent, 10);
            } else {
//				mWebView.loadUrl(LCHttpClient.RELEASEE_URL_WAP + web_url
//						+ getActionUrl);
            }
        }

        // 登陆页面(跳转url)
        @JavascriptInterface
        public void goLogin(String url) {
//			getActionUrl = url.replace(LCHttpClient.RELEASEE_URL_WAP, "");
//			if (!MemorySave.MS.mIsLogin) {
//				Intent intent = new Intent();
//				Bundle bundle = new Bundle();
//				bundle.putInt("main_web_flag", 20);
//				intent.putExtras(bundle);
//				intent.setClass(mContext, LoginActivity.class);
//				startActivityForResult(intent, 11);
//			} else {
//				if (getActionUrl.indexOf("?") == -1
//						&& getActionUrl.indexOf("#") == -1) {
//					mWebView.loadUrl(LCHttpClient.RELEASEE_URL_WAP + getActionUrl
//							+ "?");
//				} else {
//					mWebView.loadUrl(LCHttpClient.RELEASEE_URL_WAP + getActionUrl);
//				}
//			}
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
//			shareHandler.sendMessage(msg);
        }

        // 分享功能
        @JavascriptInterface
        public void toNewShare(String content, String imgurl, String shareurl,
                               String title) {
//			ShareAddImgModel shareModel = new ShareAddImgModel();
//			shareModel.imgUrl = imgurl;
//			shareModel.sharemes = content;
//			shareModel.title = title;
//			shareModel.shareUrl = shareurl;
//			Message msg = new Message();
//			Bundle bundle = new Bundle();
//			bundle.putString("content", content);
//			msg.setData(bundle);
//			msg.what = 4;
//			msg.obj = shareModel;
//			shareHandler.sendMessage(msg);
        }

        // 分享功能
        @JavascriptInterface
        public void toShare() {
            Message msg = new Message();
            msg.what = 3;
//			shareHandler.sendMessage(msg);
        }

        // 充值携带URL
        @JavascriptInterface
        public void goRecharge2(String url) {
//			getActionUrl = url;
//			Intent intent = new Intent();
//			if (MemorySave.MS.mIsLogin) {
//				intent.putExtra("web_url", AgreementActivity.RELEASEE_URL_WAP);
//				intent.setClass(mContext, AgreementActivity.class);
//			} else {
//				Bundle bundle = new Bundle();
//				bundle.putInt("main_web_flag", 20);
//				intent.putExtras(bundle);
//				intent.setClass(mContext, LoginActivity.class);
//				intent.putExtra("web_url", AgreementActivity.RELEASEE_URL_WAP);
//				intent.putExtra("goClass", AgreementActivity.class.getName());
//			}
//			startActivityForResult(intent, 30);
        }

        // 理财短期宝产品列表
        @JavascriptInterface
        public void goFinanceCZ() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            // 短期宝筛选
            MemorySave.MS.mGoFinancetype = 1;

            startActivity(intent);
            AgreementActivity.this.finish();
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
                AgreementActivity.this.finish();
            }
        }

        // 注册页面
        @JavascriptInterface
        public void goRegister() {
            if (!MemorySave.MS.mIsLogin) {
                Intent intent = new Intent(mContext,
                        UserRegisterFirActivity.class);
                startActivity(intent);
                AgreementActivity.this.finish();
            } else {
//				shareHandler.sendEmptyMessage(2);
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
            AgreementActivity.this.finish();
        }

        // 理财长期宝产品列表
        @JavascriptInterface
        public void goFinanceWY() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            // 长期宝筛选
            MemorySave.MS.mGoFinancetype = 2;

            startActivity(intent);
            AgreementActivity.this.finish();
        }

        // 理财超益宝产品列表
        @JavascriptInterface
        public void goFinanceJYB() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            // 超益宝筛选
            MemorySave.MS.mGoFinancetype = 3;

            startActivity(intent);
            AgreementActivity.this.finish();
        }

        // 转让市场
        @JavascriptInterface
        public void goTransfer() {
            // Intent intent = new Intent(mContext, MainActivity.class);
            // MemorySave.MS.mIsGoTransfer = true;
            // startActivity(intent);
            // AgreementActivity.this.finish();
        }

        // 财富中心
        @JavascriptInterface
        public void goAccount() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoAccount = true;
            startActivity(intent);
            AgreementActivity.this.finish();
        }

        // 理财列表
        @JavascriptInterface
        public void goFinance() {
            MemorySave.MS.isRechargeSuccessToFinance = true;
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            // hayb筛选
            MemorySave.MS.mGoFinancetype = 0;

            startActivity(intent);
            AgreementActivity.this.finish();
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
            AgreementActivity.this.finish();
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
//			getIntent().putExtra("goClass",
//					FinanceProjectDetailActivity.class.getName());
            GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
            GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
            MemorySave.MS.args = args;
            new TestLogin(AgreementActivity.this).testIt(AgreementActivity.this);
        }

        // 理财详情页面
        @JavascriptInterface
        public void goFinanceInfoLimit(String prj_id, String money) {
            Bundle args = new Bundle();
            args.putString("prj_id", prj_id);
            args.putString("limitMoney", money);
//			getIntent().putExtra("goClass",
//					FinanceProjectDetailActivity.class.getName());
            GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
            GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
            MemorySave.MS.args = args;
            new TestLogin(AgreementActivity.this).testIt(AgreementActivity.this);
        }

        // 活动页面弹出对话框判断isCanGoBack值绑定
        @JavascriptInterface
        public void bindPopJS() {
//			isDialogShow = true;
        }

        // 我的奖励改标题
        @JavascriptInterface
        public void alterTitle(String title, String flag, String isNative) {
//			mFlag = Boolean.parseBoolean(flag);
//			if (mFlag) {// 显示副标题
//				Message msg = new Message();
//				msg.what = 6;
//				msg.obj = title;
//				shareHandler.sendMessage(msg);
//
//			} else {// 不显示副标题
//				Message msg = new Message();
//				msg.what = 5;
//				msg.obj = title;
//				shareHandler.sendMessage(msg);
//
//			}

            if (Boolean.parseBoolean(isNative)) {
                AgreementActivity.this.finish();
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

    }
}
