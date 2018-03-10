package com.lcshidai.lc.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.igexin.sdk.PushManager;
import com.lcshidai.lc.AppConfig;
import com.lcshidai.lc.R;
import com.lcshidai.lc.getui.GtPushService;
import com.lcshidai.lc.impl.LoadingImpl;
import com.lcshidai.lc.impl.MessageImpl;
import com.lcshidai.lc.impl.account.AppStatsImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.Loading.LoadingAttach;
import com.lcshidai.lc.model.Loading.LoadingData;
import com.lcshidai.lc.model.Loading.LoadingImg;
import com.lcshidai.lc.model.Loading.LoadingListJson;
import com.lcshidai.lc.model.MessageJson;
import com.lcshidai.lc.model.MessageLocalData;
import com.lcshidai.lc.service.HttpLoadingService;
import com.lcshidai.lc.service.HttpMsgService;
import com.lcshidai.lc.service.HttpYouMiService;
import com.lcshidai.lc.service.account.HttpAppStatsService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CacheUtil;
import com.lcshidai.lc.utils.CacheUtil.CacheDeleteCallback;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.MsgUtil;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.utils.UpdateManager;

import java.util.List;

/**
 * 启动页面
 */
public class LoadingActivity extends TRJActivity implements CacheDeleteCallback, LoadingImpl, AppStatsImpl, MessageImpl {
    HttpAppStatsService hass;
    private int firstStartFlag = 0; // 0：第一次启动 else非第一次启动
    private volatile boolean isResponse;// 判断启动页面是否有处理（响应）
    private String spVersionName = ""; // 记录的版本号（判断更新或第一次安装）

    private int spVersionCode;
    private int mNowVersionCode;
    private boolean cacheFinishFlag = false;
    private boolean animFinishFlag = false;

    private String totalIncome = ""; // 总收入
    private String totalMembers = ""; // 总人数
    private String message_centre = "", md_title = "", md_content = "", md_ctime = "";
    private int screenWidth;
    private ImageView ivLogo;

    private String webUrl;
    private String webTitle;
    HttpLoadingService hls;
    HttpYouMiService hyms;
    HttpMsgService hms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setAttributes(new LayoutParams(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            Thread.sleep(1500);     //启动延时1.5秒，避免启动页一闪而过
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_loading);
        hls = new HttpLoadingService(this, this);
        hass = new HttpAppStatsService(this, this);
        hyms = new HttpYouMiService(this);
        hms = new HttpMsgService(this, this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;

        deleteCacheThread.start();

        if (null != getIntent()) {
            if (getIntent().getIntExtra("alarm_to_finance", 0) == 1) {
                MemorySave.MS.mIsGoFinanceInfo = true;
                MemorySave.MS.mAlarmPrjId = getIntent().getStringExtra("prj_id");
            } else if (getIntent().getIntExtra("alarm_to_finance", 0) == 2) {
                MemorySave.MS.mIsGoFinance = true;
            } else if (!TextUtils.isEmpty(getIntent().getStringExtra("message_centre"))) {
                message_centre = getIntent().getStringExtra("message_centre");
                md_title = getIntent().getStringExtra("md_title");
                md_content = getIntent().getStringExtra("md_content");
                md_ctime = getIntent().getStringExtra("md_ctime");
            }
        }

        firstStartFlag = SpUtils.getInt(SpUtils.Table.CONFIG, SpUtils.Config.IS_FIRST_OPEN, 0);
        spVersionName = SpUtils.getString(SpUtils.Table.CONFIG, SpUtils.Config.VERSION_NAME);
        spVersionCode = SpUtils.getInt(SpUtils.Table.CONFIG, SpUtils.Config.VERSION_CODE, 0);

//        String cid = PushManager.getInstance().getClientid(getApplicationContext());
        // 判断推送消息开关是否开启
        int isPushOpen = SpUtils.getInt(SpUtils.Table.CONFIG, SpUtils.Config.IS_PUSH_OPEN, 1);
        if (isPushOpen == 1) {
            com.igexin.sdk.PushManager.getInstance().turnOnPush(mContext);
        } else {
            com.igexin.sdk.PushManager.getInstance().turnOffPush(mContext);
        }

        ivLogo = (ImageView) findViewById(R.id.ivLogo);

        loadSplashPic();
        MemorySave.MS.mWebOpenAppFlag = false;
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();
        if (null != action && Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                webUrl = uri.getQueryParameter("weburl");
                webTitle = uri.getQueryParameter("title");
                if (!StringUtils.isEmpty(webUrl))
                    SpUtils.setParam(SpUtils.Table.DEFAULT, "webUrl", webUrl);
                if (!StringUtils.isEmpty(webTitle))
                    SpUtils.setParam(SpUtils.Table.DEFAULT, "webTitle", webTitle);
            }
        }

        int invest_sequence = MsgUtil.getInt(this, MsgUtil.INVEST_SEQUENCE);
//		int account_sequence = Util.getInt(this, Util.ACCOUNT_SEQUENCE);
        int discovery_sequence = MsgUtil.getInt(this, MsgUtil.DISCOVERY_SEQUENCE);
        if (invest_sequence == -1) {
            invest_sequence = 0;
        }
        if (discovery_sequence == -1) {
            discovery_sequence = 0;
        }
        hms.getAllMsg("0", invest_sequence, discovery_sequence, 0);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    /**
     * 加载启动页图片
     */
    private void loadSplashPic() {
        isResponse = false;
        hls.gainAccountBankSearch();
        if (!isResponse) {
            isResponse = true;
        }
        loadingImgFromUrl();
    }

    /**
     * Splash图片加载动画执行完毕后跳转逻辑
     */
    public void navigationTo() {
        String nowVersionName = UpdateManager.getVerName(mContext, getPackageName());
        int nowVersionCode = CommonUtil.getVersionCode(mContext, getPackageName());
        mNowVersionCode = nowVersionCode;
        // 更新信息
        if (!spVersionName.equals(nowVersionName)) {
            SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.VERSION_NAME, nowVersionName);
        }
        if (spVersionCode < nowVersionCode) {
            SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.VERSION_CODE, nowVersionCode);
        }
        // 获取保存的手势登陆次数信息
        int gestureTimes = SpUtils.getInt(SpUtils.Table.CONFIG, SpUtils.Config.TOTAL_TRY_TIMES, 5);

        Intent intent = new Intent();
        int status = isShowWelcomeGuideActivityWithCode();
        if (status > 0) {
            //首次安装和版本升级时都要显示欢迎引导页
            hyms.sendYouMi();
            intent.setClass(mContext, WelcomeGuideActivity.class);
            intent.putExtra("totalIncome", totalIncome);
            intent.putExtra("totalMembers", totalMembers);
            intent.putExtra("status", status);
        } else {
            if (GoLoginUtil.isShowGestureLogin(mContext) && gestureTimes > 0) {
                // 如果手势登陆开关打开且手势登陆密码输入次数小于5，则进入手势登陆页面
                intent.setClass(mContext, GestureLoginActivity.class);
                intent.putExtra("message_centre", message_centre);
                intent.putExtra("md_title", md_title);
                intent.putExtra("md_content", md_content);
                intent.putExtra("md_ctime", md_ctime);
            } else {
                if (gestureTimes == 0) {
                    // 如果手势密码输入错了5次，则要重新登录
                    ToastUtil.showLongToast(mContext, "您已输错5次手势密码，请重新登陆。");
                    SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.TOTAL_TRY_TIMES, -1);
                    intent.setClass(mContext, LoginActivity.class);
                    intent.putExtra("goClass", MainActivity.class.getName());
                    intent.putExtra("is_back_to_main", true);
                } else {
                    // 否则直接进入主界面
                    intent.setClass(mContext, MainActivity.class);

                }
            }
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtil.crcCheck(this);
        MemorySave.MS.showCountRl = true;
        if (firstStartFlag == 0) {// 第一次启动
            try {
                onStatsA1();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            onStatsA2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载Url启动页（分两种情况：如果LoadUrl对应的之不为空，则加载LoadUrl对应的图片，反之加载默认图片)
     * <p>
     * 该函数有两个地方调用，一个是加载gainLoadingSuccess，一个是handler
     */
    public void loadingImgFromUrl() {
        String urlImg = SpUtils.getString(SpUtils.Table.SPLASH, SpUtils.Splash.IMG_URL);
        AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
        if (urlImg != null && !urlImg.equals("")) {
            String url;
            if (AppConfig.PRODUCT) {
                url = "https:" + urlImg;
            } else {
                url = "http:" + urlImg;
            }
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.loading)
                    .into(ivLogo);
            aa.setDuration(4000);
        }
        ivLogo.startAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                animFinishFlag = true;
                if (cacheFinishFlag) {
                    animFinishFlag = false;
                    navigationTo();
                }
            }
        });
        ivLogo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                navigationTo();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ivLogo.setBackgroundDrawable(null);
        ivLogo.setBackgroundResource(0);
        System.gc();
    }

    // 新增用户（下载后首次打开客户端总数）
    private void onStatsA1() throws Exception {
        hass.gainAppStats(true);
    }

    // 活跃用户（当天打开客户端总数）
    private void onStatsA2() throws Exception {
        hass.gainAppStats(false);
    }

    /**
     * 判断是否显示引导页面(versionCode)
     */
    private int isShowWelcomeGuideActivityWithCode() {
        if (firstStartFlag == 0) {
            SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.IS_FIRST_OPEN, 1);
            return 1; //首次打开应用
        }
        if (spVersionCode < mNowVersionCode) {
            return 2; //版本升级
        }
        return 0; //不显示引导页
    }

    Thread deleteCacheThread = new Thread() {

        @Override
        public void run() {
            try {
                CacheUtil.getCacheSize(mContext, LoadingActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
                deleteCacheEnd();
            }
        }

    };

    @Override
    public void deleteCacheEnd() {
        cacheFinishFlag = true;
        if (animFinishFlag) {
            cacheFinishFlag = false;
            navigationTo();
        }
    }

    @Override
    public void gainLoadingSuccess(LoadingListJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                String imgUrl = null;
                if ("1".equals(boolen)) {
                    List<LoadingData> list = response.getList();
                    if (null != list && list.size() > 0) {
                        if (list.size() >= 2 && screenWidth < 720) {
                            LoadingImg imgObj = list.get(1).getImg();
                            LoadingAttach attach = imgObj.getAttach();
                            imgUrl = attach.getUrl();
                        } else {
                            LoadingImg imgObj = list.get(0).getImg();
                            LoadingAttach attach = imgObj.getAttach();
                            imgUrl = attach.getUrl();
                        }
                        SpUtils.setString(SpUtils.Table.SPLASH, SpUtils.Splash.IMG_URL, imgUrl);
                    }
                }
                if (!isResponse) {    // 如果handler 里面已经处理了 这里就不执行，
                    isResponse = true;
                    // 加载启动页
                    loadingImgFromUrl();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void gainMessageSuccess(MessageJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                List<MessageLocalData> list = MsgUtil.convertList(response.getData());
                if (list != null && list.size() > 0) {
                    for (MessageLocalData data : list) {
                        if (MsgUtil.INVEST.equals(data.getType())) {
                            MsgUtil.setObj(this, MsgUtil.INVEST, data);
                            MsgUtil.setInt(this, MsgUtil.INVEST_SEQUENCE, data.getSequence());
                        } else if (MsgUtil.ACCOUNT.equals(data.getType())) {
                            MsgUtil.setObj(this, MsgUtil.ACCOUNT, data);
                            MsgUtil.setInt(this, MsgUtil.ACCOUNT_SEQUENCE, data.getSequence());
                        } else if (MsgUtil.DISCOVERY.equals(data.getType())) {
                            MsgUtil.setObj(this, MsgUtil.DISCOVERY, data);
                            MsgUtil.setInt(this, MsgUtil.DISCOVERY_SEQUENCE, data.getSequence());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void gainMessageFail() {
        Log.i("lfq", "gain message fail ");
    }

    @Override
    public void gainLoadingFailed() {
//		loadingImgFromUrl();
        SpUtils.setString(SpUtils.Table.SPLASH, SpUtils.Splash.IMG_URL, null);
    }

    @Override
    public void gainAppStatssuccess(BaseJson response) {

    }

    @Override
    public void gainAppStatsfail() {

    }

}
