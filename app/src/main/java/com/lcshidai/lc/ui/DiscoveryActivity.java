package com.lcshidai.lc.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.os.SystemClock;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.GainCodeImpl;
import com.lcshidai.lc.model.MessageLocalData;
import com.lcshidai.lc.model.MessageTypeNew;
import com.lcshidai.lc.model.MsgNew;
import com.lcshidai.lc.ui.account.AccountCenterActivity;
import com.lcshidai.lc.ui.account.FinancialCashActivity;
import com.lcshidai.lc.ui.account.ManageFinanceListInfoActivity;
import com.lcshidai.lc.ui.account.MyRewardActivity;
import com.lcshidai.lc.ui.account.pwdmanage.UserPwdManageActivity;
import com.lcshidai.lc.ui.base.AbsSubActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.more.InviteActivity;
import com.lcshidai.lc.utils.CookieUtil;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.GoClassUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.MsgUtil;
import com.lcshidai.lc.utils.OneKeyShareUtil;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.TestLogin;
import com.lcshidai.lc.widget.ppwindow.DialogPopupWindow;
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
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQ;

/**
 * 发现页面
 */
public class DiscoveryActivity extends AbsSubActivity implements OnClickListener, CallBackShare, GainCodeImpl {

    private String web_url = "wireless/index.html#ac=";
    private String action_url = "";

    private String final_url = "";

    private Context mContext;

    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    ImageButton mBackBtn;
    private WebView mWebView;
    private CookieUtil cookieUtil;
    private Dialog loading;
    public WebViewTask mWebViewTask;

    private String channel = "";
    private String getActionUrl = "";

    private OneKeyShareUtil oneKeyShareUtil;
    private long lastTime;
    String mtitle;
    String order_id;
    RedMoneyPW redMoneyPw;

    public String shareRedUrl = "Mobile2/Share/shareSharkBonus";
    boolean isRedShow = false;
    boolean mflag = false;
    boolean isMyReword = false;

    private boolean isDialogShow = false;// 是否直接返回
    String appVersion;
    private DialogPopupWindow dialogPopupWindow;
    private View mainView;
    private RelativeLayout rl_actionbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mtitle = getIntent().getStringExtra("title");
        order_id = getIntent().getStringExtra("order_id");
        isRedShow = getIntent().getBooleanExtra("isRedShow", false);
        isMyReword = getIntent().getBooleanExtra("isMyReword", false);

        final_url = LCHttpClient.BASE_WAP_HEAD + "/#/foundApp";
        initView();
        redMoneyPw = new RedMoneyPW(DiscoveryActivity.this, this);
        MemorySave.MS.mWebOpenAppFlag = true;

        SpUtils.setParam(SpUtils.Table.DEFAULT, "webUrl", "");
        SpUtils.setParam(SpUtils.Table.DEFAULT, "webTitle", "");
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_main_web);
        mainView = findViewById(R.id.main);
        dialogPopupWindow = new DialogPopupWindow(this, mainView, null);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mBackBtn.setVisibility(View.GONE);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);

        rl_actionbar = (RelativeLayout) findViewById(R.id.actionbar);

        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("充值");

        if (mtitle != null && !mtitle.equals("")) {
            mTvTitle.setText(mtitle);
        } else {
            mTvTitle.setText("发现");
            rl_actionbar.setVisibility(View.GONE);
        }
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);

        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);

        mWebView = (WebView) findViewById(R.id.main_wb_webview);
        // 自动化测试代码
//        WebView.setWebContentsDebuggingEnabled(true);
        cookieUtil = new CookieUtil(mContext);
        loading = createLoadingDialog(mContext, "数据加载中", true);
        oneKeyShareUtil = new OneKeyShareUtil(this);
    }

    public void reloadData() {
        if (null != mWebViewTask) {
            mWebViewTask.cancel(false);
        }
        mWebViewTask = new WebViewTask();
        mWebViewTask.execute();
//		loading.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                goBack();
                break;
        }
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
                    DiscoveryActivity.this.finish();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private MessageLocalData data;

    @Override
    protected void onResume() {
        super.onResume();
        if (MemorySave.MS.mIsFinanceInfoFinish || MemorySave.MS.isBidSuccessBack || MemorySave.MS.mIsGoFinanceRecord) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        reloadData();
        data = (MessageLocalData) MsgUtil.getObj(this, MsgUtil.DISCOVERY);
    }

    @Override
    protected void onDestroy() {
        mWebViewTask.cancel(true);
        mWebView.destroy();
        super.onDestroy();
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
                cookieManager.setCookie(LCHttpClient.BASE_WAP_HEAD, sessionCookie);
                cookieManager.setCookie(LCHttpClient.BASE_WAP_HEAD, "TRPApp=lcshidai");
                cookieManager.setCookie(LCHttpClient.BASE_WAP_HEAD, "TRPClient=android");
                cookieManager.setCookie(LCHttpClient.BASE_WAP_HEAD, "TRPHeader=showHead");
                CookieSyncManager.getInstance().sync();
            }
            try {
                sessionCookie = cookieUtil.getCookie();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            mWebView.setScrollbarFadingEnabled(true);
            mWebView.getSettings().setPluginState(PluginState.ON);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            mWebView.getSettings().setBlockNetworkImage(true);
            // 不使用浏览器缓存
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            String ua = mWebView.getSettings().getUserAgentString();
            if (!ua.contains("app_trj_android")) {
                mWebView.getSettings().setUserAgentString(ua + ";app_trj_android");
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
            mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
            mWebView.addJavascriptInterface(new CustomNativeAccess(), "recharge");
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed(); // 接受证书
                    // super.onReceivedSslError(view, handler, error);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    if (null != mContext) {
                        dialogPopupWindow.showWithMessage(description, "1");
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if (!loading.isShowing()) {
                        loading.show();
                    }
                    String js = "var newscript = document.createElement(\"script\");";
                    js += "newscript.onload=function(){alert('弹框');};";  //xxx()代表js中某方法
                    js += "document.body.appendChild(newscript);";
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    // history();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (loading.isShowing()) {
                        try {
                            loading.dismiss();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                    mWebView.loadUrl(
                            "javascript:window.local_obj.showSource('<html xmlns=\"http://www.w3.org/1999/xhtml\">'+"
                                    + "document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    mWebView.loadUrl("javascript:isHasPopJS()");
                    mWebView.getSettings().setBlockNetworkImage(false);

//					https://testwap1.tourongjia.com/#/foundApp 首页   第一次加载 
//					https://testwap1.tourongjia.com/#/investor 投资者关系 
//					https://testwap1.tourongjia.com/#/investList 媒体报告
//					https://testwap1.tourongjia.com/#/notice  平台公告
//					https://testwap1.tourongjia.com/#/newInfo  最新动态
//					https://testwap1.tourongjia.com/#/sysNotice 系统通知
                    if (url.contains("foundApp")) {
                        if (data != null) {
                            Map<String, MessageTypeNew> map = data.getMap();
                            if (map.size() > 0) {
                                MessageTypeNew sysType = map.get(MsgUtil.TYPE_DISCOVERY_SYSYNOTIFY);
                                int count;
                                if (sysType != null && sysType.getMessages() != null) {
                                    count = data.getCount() - sysType.getMessages().size();
                                } else {
                                    count = data.getCount();
                                }
                                if (count > 0) {// 减去系统通知的消息数目
                                    mWebView.loadUrl("javascript:showOrhideIcon('discovery_investor', 'show');");
                                } else {
                                    mWebView.loadUrl("javascript:showOrhideIcon('discovery_investor', 'hide');");
                                }
                            }
                            return;
                        }
                    }

                    if (url.contains("investor")) {
                        if (data != null && data.getMap() != null) {
                            Map<String, MessageTypeNew> map = data.getMap();
                            if (map.size() > 0) {
                                MessageTypeNew reportType = map.get(MsgUtil.TYPE_DISCOVERY_REPORT);
                                MessageTypeNew noticeType = map.get(MsgUtil.TYPE_DISCOVERY_NOTICE);
                                MessageTypeNew informationType = map.get(MsgUtil.TYPE_DISCOVERY_INFORMATION);
                                MessageTypeNew sysType = map.get(MsgUtil.TYPE_DISCOVERY_SYSYNOTIFY);

                                if (reportType != null && reportType.getCount() > 0) {
                                    List<MsgNew> msgNews = reportType.getMessages();
                                    if (msgNews != null) {
                                        for (MsgNew msgNew : msgNews) {
                                            if (!msgNew.isDirty()) {
                                                mWebView.loadUrl("javascript:showOrhideIcon(" + "'discovery_investor_relations_report'" + ", " + "'show'" + ");");
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (noticeType != null && noticeType.getCount() > 0) {
                                    List<MsgNew> msgNews = noticeType.getMessages();
                                    if (msgNews != null) {
                                        for (MsgNew msgNew : msgNews) {
                                            if (!msgNew.isDirty()) {
                                                mWebView.loadUrl("javascript:showOrhideIcon('discovery_investor_relations_notice', 'show');");
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (informationType != null && informationType.getCount() > 0) {
                                    List<MsgNew> msgNews = informationType.getMessages();
                                    if (msgNews != null) {
                                        for (MsgNew msgNew : msgNews) {
                                            if (!msgNew.isDirty()) {
                                                mWebView.loadUrl("javascript:showOrhideIcon('discovery_investor_relations_information', 'show');");
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (sysType != null && sysType.getCount() > 0) {
                                    List<MsgNew> msgNews = sysType.getMessages();
                                    if (msgNews != null) {
                                        for (MsgNew msgNew : msgNews) {
                                            if (!msgNew.isDirty()) {
                                                mWebView.loadUrl("javascript:showOrhideIcon('discovery_investor_relations_sysnotify', 'show');");
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                        }
                        return;
                    }
                    String key = "";
                    int count = 0;
                    if (url.contains("investList")) {
                        key = MsgUtil.TYPE_DISCOVERY_REPORT;
                    } else if (url.contains("notice")) {
                        key = MsgUtil.TYPE_DISCOVERY_NOTICE;
                    } else if (url.contains("newInfo")) {
                        key = MsgUtil.TYPE_DISCOVERY_INFORMATION;
                    } else if (url.contains("sysNotice")) {
                        key = MsgUtil.TYPE_DISCOVERY_SYSYNOTIFY;
                    }

                    if (key.equals("")) return;

                    if (data != null) {
                        if (data.getMap().containsKey(key)) {
                            if (key.equals(MsgUtil.TYPE_DISCOVERY_REPORT)) {
                                mWebView.loadUrl("javascript:showOrhideIcon(" + "'discovery_investor_relations_report'" + ", " + "'hide'" + ");");
                            } else if (key.equals(MsgUtil.TYPE_DISCOVERY_NOTICE)) {
                                mWebView.loadUrl("javascript:showOrhideIcon('discovery_investor_relations_notice', 'hide');");
                            } else if (key.equals(MsgUtil.TYPE_DISCOVERY_INFORMATION)) {
                                mWebView.loadUrl("javascript:showOrhideIcon('discovery_investor_relations_information', 'hide');");
                            } else if (key.equals(MsgUtil.TYPE_DISCOVERY_SYSYNOTIFY)) {
                                mWebView.loadUrl("javascript:showOrhideIcon('discovery_investor_relations_sysnotify', 'hide');");
                            }

                            MessageTypeNew type = data.getMap().get(key);
                            List<MsgNew> msgNews = type.getMessages();
                            for (MsgNew msgNew : msgNews) {
                                msgNew.setDirty(true);
                            }
                            MsgUtil.setObj(DiscoveryActivity.this, MsgUtil.DISCOVERY, data);

                        }
                        Intent intent = new Intent();
                        intent.setAction(MsgUtil.MSG_ACTION_REFRESH);
                        intent.putExtra("flag", 2);
                        DiscoveryActivity.this.sendBroadcast(intent);

                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains("tel")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } else {
                        // view.loadUrl(url+urlsuffix);
                        view.loadUrl(url);
                    }
                    return true;
                }

            });

            mWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Change
                }

            });
            ApplicationInfo app_info = null;
            PackageInfo pack_info = null;
            try {
                app_info = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(),
                        PackageManager.GET_META_DATA);
                pack_info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            appVersion = pack_info.versionName;
            if (null != app_info) {
                channel = app_info.metaData.getString("UMENG_CHANNEL");
            }
            mWebView.loadUrl(final_url);

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
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new InputSource(new StringReader(html)));
                NodeList str = doc.getElementsByTagName("body");
                Node node = str.item(0);
                String s = node.getChildNodes().item(0).getNodeValue();
                JSONObject jobject = new JSONObject(s);
                String boolen = jobject.getString("boolen");
                String logined = jobject.getString("logined");
                if (boolen.equals("0") && logined.equals("0")) {
                    Intent intent = new Intent();
                    intent.setClass(DiscoveryActivity.this, LoginActivity.class);
                    startActivity(intent);
                    DiscoveryActivity.this.finish();
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
            DiscoveryActivity.this.finish();
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
            getActionUrl = url;
            if (!MemorySave.MS.mIsLogin) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("main_web_flag", 20);
                intent.putExtras(bundle);
                intent.setClass(mContext, LoginActivity.class);
                startActivityForResult(intent, 10);
            } else {
                mWebView.loadUrl(LCHttpClient.BASE_WAP_HEAD + web_url + getActionUrl);
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
                if (getActionUrl.indexOf("?") == -1 && getActionUrl.indexOf("#") == -1) {
                    mWebView.loadUrl(LCHttpClient.BASE_WAP_HEAD + getActionUrl + "?");
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
        public void toNewShare(String content, String imgurl, String shareurl, String title) {
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
        public void goFinanceRYS() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            // 短期宝筛选
            MemorySave.MS.mGoFinancetype = 1;

            startActivity(intent);
            DiscoveryActivity.this.finish();
        }

        // 新客专享
        @JavascriptInterface
        public void goNewGuest() {
            if (MemorySave.MS.mIsLogin
                    && (null != MemorySave.MS.userInfo.is_newbie && !"1".equals(MemorySave.MS.userInfo.is_newbie))) {
                showToast("您已经不是新客用户，感谢您的支持");
            } else {
                Intent intent = new Intent(mContext, MainActivity.class);
                MemorySave.MS.mIsGoHome = true;
                startActivity(intent);
                DiscoveryActivity.this.finish();
            }
        }

        // 注册页面
        @JavascriptInterface
        public void goRegister() {
            if (!MemorySave.MS.mIsLogin) {
                Intent intent = new Intent(mContext, UserRegisterFirActivity.class);
                startActivity(intent);
                DiscoveryActivity.this.finish();
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
                setting_intent.putExtra("goClass", AccountCenterActivity.class.getName());
            }
            startActivity(setting_intent);
            DiscoveryActivity.this.finish();
        }

        // 理财长期宝产品列表
        @JavascriptInterface
        public void goFinanceQYR() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            // 长期宝筛选
            MemorySave.MS.mGoFinancetype = 2;

            startActivity(intent);
            DiscoveryActivity.this.finish();
        }

        // 理财超益宝产品列表
        @JavascriptInterface
        public void goFinanceJYB() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            // 超益宝筛选
            MemorySave.MS.mGoFinancetype = 3;

            startActivity(intent);
            DiscoveryActivity.this.finish();
        }

        // 转让市场
        @JavascriptInterface
        public void goTransfer() {
            // Intent intent = new Intent(mContext, MainActivity.class);
            // MemorySave.MS.mIsGoTransfer = true;
            // startActivity(intent);
            // DiscoveryActivity.this.finish();
        }

        // 财富中心
        @JavascriptInterface
        public void goAccount() {
            Intent intent = new Intent(mContext, MainActivity.class);
            MemorySave.MS.mIsGoAccount = true;
            startActivity(intent);
            DiscoveryActivity.this.finish();
        }

        // 理财列表
        @JavascriptInterface
        public void goFinance() {
            // if (!StringUtils.isEmpty(loadUrl) &&
            // loadUrl.equals(RELEASEE_URL_WAP))
            // MemorySave.MS.isRechargeSuccessToFinance = true;
            Intent intent = new Intent(DiscoveryActivity.this, MainActivity.class);
            MemorySave.MS.mIsGoFinance = true;
            MemorySave.MS.mIsGoFinanceHome = true;
            MemorySave.MS.mIsGoHome = true;
            // hayb筛选
            MemorySave.MS.mGoFinancetype = 0;

            startActivity(intent);

        }

        // 投资记录
        @JavascriptInterface
        public void goFinanceRecord() {
            Intent finance_intent = new Intent();
            if (MemorySave.MS.mIsLogin) {
                finance_intent.setClass(mContext, ManageFinanceListInfoActivity.class);
            } else {
                finance_intent.setClass(mContext, LoginActivity.class);
                finance_intent.putExtra("goClass", ManageFinanceListInfoActivity.class.getName());
            }
            startActivity(finance_intent);
            DiscoveryActivity.this.finish();
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
//			getIntent().putExtra("goClass", FinanceProjectDetailActivity.class.getName());
            GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
            GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
            MemorySave.MS.args = args;
            new TestLogin(DiscoveryActivity.this).testIt(DiscoveryActivity.this);
        }

        // 理财详情页面
        @JavascriptInterface
        public void goFinanceInfoLimit(String prj_id, String money) {
            Bundle args = new Bundle();
            args.putString("prj_id", prj_id);
            args.putString("limitMoney", money);
//			getIntent().putExtra("goClass", FinanceProjectDetailActivity.class.getName());
            GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
            GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
            MemorySave.MS.args = args;
            new TestLogin(DiscoveryActivity.this).testIt(DiscoveryActivity.this);
        }

        // 活动页面弹出对话框判断isCanGoBack值绑定
        @JavascriptInterface
        public void bindPopJS() {
            isDialogShow = true;
        }

        // 我的奖励改标题
        @JavascriptInterface
        public void alterTitle(String title, String flag, String isNative) {
            mflag = Boolean.parseBoolean(flag);
            if (mflag) {// 显示副标题
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
                DiscoveryActivity.this.finish();
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
            dialogPopupWindow.showWithMessage(message, "1");
        }

        @JavascriptInterface
        public void resetPayPassword() {
            Intent intent = new Intent(mContext, UserPwdManageActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void goScoreStore(String url) {
            Intent intent = new Intent(mContext, MainWebActivity.class);
            // replace this
            intent.putExtra("web_url", url);
            intent.putExtra("title", "积分商城");
            intent.putExtra("need_header", "1");
            MemorySave.MS.isGoLast = true;
            startActivity(intent);
        }

        @JavascriptInterface
        public void goTelCheng() {
            Intent intent = new Intent(mContext, ModifyPreLeftPhoneNumberActivity.class);
            intent.putExtra(ModifyPreLeftPhoneNumberActivity.ENTRANCE, MainWebActivity.class.getSimpleName());
            startActivity(intent);
        }

        @JavascriptInterface
        public void goBack() {
            finish();
        }

        @JavascriptInterface
        public void goGoldFinance() {
            Intent intent = new Intent(mContext, FinancialCashActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public String getAppVersionName() {
            try {
                String packageName = mContext.getPackageName();
                String versionName = mContext.getPackageManager().getPackageInfo(packageName, 0).versionName;
                return versionName;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            return null;
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

    }

    Handler shareHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String content = msg.getData().getString("content");
                    if (System.currentTimeMillis() - lastTime > 500 && !"".equals(content)) {
                        lastTime = System.currentTimeMillis();
                        oneKeyShareUtil.toShare(content, new PlatformActionListener() {

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
                    break;
                case 2:
                    showToast("您已注册，谢谢参与");
                    break;
                case 3:
                    toSharered();
                    break;
                case 4:
                    ShareAddImgModel model = (ShareAddImgModel) msg.obj;
                    oneKeyShareUtil.toShareForImageUrl(model.sharemes, model.title, model.shareUrl, model.imgUrl,
                            new PlatformActionListener() {

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
                            Intent intent = new Intent(mContext, MyRewardActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 20) {
            if (!loading.isShowing()) {
                loading.show();
            }
            if (mWebViewTask.sessionCookie != null) {
                new NewTask(0).execute();
            }
        } else if (requestCode == 30 && resultCode == 40) {
            if (!loading.isShowing()) {
                loading.show();
            }
            mWebView.reload();
        } else if (requestCode == 11 && resultCode == 20) {
            if (mWebViewTask.sessionCookie != null) {
                if (!loading.isShowing()) {
                    loading.show();
                }
                new NewTask(1).execute();
            }
        } else if (requestCode == 12 && resultCode == 20) {
            if (mWebViewTask.sessionCookie != null) {
                if (!loading.isShowing()) {
                    loading.show();
                }
                new NewTask(2).execute();
            }
        } else if (requestCode == 22 && resultCode == 23) {
            if (mWebViewTask.sessionCookie != null) {
                if (!loading.isShowing()) {
                    loading.show();
                }
                new NewTask(2).execute();
            }
        }
    }

    class NewTask extends AsyncTask<Void, Void, Void> {
        int flag;

        public NewTask(int f) {
            this.flag = f;
        }

        @Override
        protected void onPreExecute() {
            CookieSyncManager.createInstance(mContext);
            mWebViewTask.cookieManager = CookieManager.getInstance();

            try {
                mWebViewTask.sessionCookie = cookieUtil.getCookie();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mWebViewTask.sessionCookie != null) {
                // delete old cookies
                mWebViewTask.cookieManager.removeSessionCookie();
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SystemClock.sleep(1000);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mWebViewTask.cookieManager.setCookie(LCHttpClient.BASE_WAP_HEAD, mWebViewTask.sessionCookie);
            CookieSyncManager.getInstance().sync();
            if (flag == 0) {
                mWebView.loadUrl(LCHttpClient.BASE_WAP_HEAD + web_url + getActionUrl);
            } else if (flag == 1) {
                if (getActionUrl.indexOf("?") == -1 && getActionUrl.indexOf("#") == -1) {
                    mWebView.loadUrl(LCHttpClient.BASE_WAP_HEAD + getActionUrl + "?");
                } else {
                    mWebView.loadUrl(LCHttpClient.BASE_WAP_HEAD + getActionUrl);
                }
            } else if (flag == 2) {
                mWebView.reload();
            }
        }

    }

    /**
     * 红包分享
     */
    private void toSharered() {
        createImageCacheDir();
        if (loading != null) {
            loading.show();
        }
        RequestParams rq = new RequestParams();
        rq.put("id", order_id);
        post(shareRedUrl, rq, new JsonHttpResponseHandler(DiscoveryActivity.this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (loading.isShowing()) {
                    loading.dismiss();
                }
            }
        });
    }

    private void hongBaoShare(String content, String title, String qrcode) {
        oneKeyShareUtil.toShare(content, title, qrcode, bannerCacheDir + FILE_NAME, new PlatformActionListener() {

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

    private void downloadImage(final String url, final String content, final String title, final String qrcode) {
        File cacheFile = new File(bannerCacheDir + FILE_NAME);
        if (cacheFile.exists()) {
            if (loading.isShowing()) {
                loading.dismiss();
            }
            hongBaoShare(content, title, qrcode);
        }
    }

    public class ShareAddImgModel {
        public String title;
        public String shareUrl;
        public String imgUrl;
        public String sharemes;
    }

    @Override
    public void gainBitmapDatasuccess(byte[] binaryData, String url, String content, String title, String qrcode) {
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
                if (loading.isShowing()) {
                    loading.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                hongBaoShare(content, title, qrcode);
            }
        }
    }

    @Override
    public void gainBitmapDatafail() {

    }
}
