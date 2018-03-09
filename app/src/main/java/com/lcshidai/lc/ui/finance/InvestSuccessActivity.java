package com.lcshidai.lc.ui.finance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.AdsImpl;
import com.lcshidai.lc.impl.account.GetShareImpl;
import com.lcshidai.lc.impl.account.GetSharkImpl;
import com.lcshidai.lc.model.AdsData;
import com.lcshidai.lc.model.AdsJson;
import com.lcshidai.lc.model.account.GetShareData;
import com.lcshidai.lc.model.account.GetShareJson;
import com.lcshidai.lc.model.account.GetSharkData;
import com.lcshidai.lc.model.account.GetSharkJson;
import com.lcshidai.lc.model.finance.FinancePayResultButton;
import com.lcshidai.lc.model.finance.FinancePayResultData;
import com.lcshidai.lc.service.HttpAdsService;
import com.lcshidai.lc.service.account.HttpGetShareService;
import com.lcshidai.lc.service.account.HttpGetSharkService;
import com.lcshidai.lc.ui.MainActivity;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.account.bespeak.BespeakAreaActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.OneKeyShareUtil;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.ppwindow.RedShakePopupWindow;
import com.lcshidai.lc.widget.ppwindow.RedShakePopupWindow.RedShakeBack;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQ;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 投资成功页面
 */
public class InvestSuccessActivity extends TRJActivity implements SensorEventListener, RedShakeBack,
        OnClickListener, GetShareImpl, GetSharkImpl, AdsImpl {
    // 获取分享信息
    HttpGetShareService hgss;
    HttpGetSharkService hggss;
    private TextView mTvTitle, mSubTitle;
    private Button mbtn_auto_contuine, mbtn_auto_contuined;
    ImageButton mBackBtn;
    private LinearLayout ll_auto_contuine;
    private TextView tvOpenAutoInvestTips;

    View ll_info_free;
    TextView tv_free_money;
    private Dialog loading;
    private OneKeyShareUtil oneKeyShareUtil;

    RedShakePopupWindow redSpw;

    boolean mShowpw = true;

    View ll_repay;
    TextView tv_date_repay;
    boolean isfirstGoActivity = false;

    FinancePayResultData mData;
    private View v;

    private PopupWindow mHongBaoPopupWindow;
    private Dialog mExitDialog;

    private ImageView iv_ads;
    private HttpAdsService mHttpAdsService;
    private boolean isAutoInvestOpen = false;
    private boolean isCg = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            //  获取传递过来的投资结果数据
            mData = (FinancePayResultData) args.getSerializable("FinancePayResultData");
            isAutoInvestOpen = args.getBoolean(Constants.Project.IS_AUTO_INVEST_OPEN);
            isCg = args.getBoolean(Constants.Project.IS_CG);
        }
        oneKeyShareUtil = new OneKeyShareUtil(mContext);
        hgss = new HttpGetShareService(this, this);
        hggss = new HttpGetSharkService(this, this);
        mHttpAdsService = new HttpAdsService(this, this);
        setContentView(R.layout.activity_invest_success);
        iv_ads = (ImageView) findViewById(R.id.iv_ad);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mbtn_auto_contuine = (Button) findViewById(R.id.btn_auto_contuine);
        mbtn_auto_contuined = (Button) findViewById(R.id.btn_auto_contuined);
        ll_auto_contuine = (LinearLayout) findViewById(R.id.ll_auto_contuine);
        if (mData.getIs_view_auto_icon() != null && mData.getIs_view_auto_icon().equals("1")) {
            ll_auto_contuine.setVisibility(View.GONE);
            if (mData.getIs_has_appoint() != null && mData.getIs_has_appoint().equals("0")) {
                mbtn_auto_contuine.setVisibility(View.VISIBLE);
                mbtn_auto_contuined.setVisibility(View.GONE);
            } else {
                mbtn_auto_contuine.setVisibility(View.GONE);
                mbtn_auto_contuined.setVisibility(View.VISIBLE);
            }
        } else {
            ll_auto_contuine.setVisibility(View.GONE);
        }

        tvOpenAutoInvestTips = (TextView) findViewById(R.id.tv_open_auto_invest_tips);
        tvOpenAutoInvestTips.setOnClickListener(this);
        if (isCg) {
            if (isAutoInvestOpen) {
                tvOpenAutoInvestTips.setVisibility(View.GONE);
            } else {
                tvOpenAutoInvestTips.setVisibility(View.VISIBLE);
            }
        } else {
            tvOpenAutoInvestTips.setVisibility(View.GONE);
        }
        mbtn_auto_contuine.setOnClickListener(this);
        findViewById(R.id.btn_option).setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) this.findViewById(R.id.tv_top_bar_title);
        if (mData.getPrj_business_type_name() != null && !mData.getPrj_business_type_name().equals("")) {
            mTvTitle.setText(mData.getPrj_type_display() + "-" + mData.getPrj_name());
        } else {
            mTvTitle.setText(mData.getPrj_type_display());
        }
        mTvTitle.setTextSize(18);
        mSubTitle = (TextView) this.findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        ll_repay = findViewById(R.id.ll_repay);
        tv_date_repay = (TextView) findViewById(R.id.tv_date_repay);
        ll_info_free = findViewById(R.id.ll_info_free);
        tv_free_money = (TextView) findViewById(R.id.tv_free_money);

        if (CommonUtil.isNullOrEmpty(mData.getFree_money())) {
            ll_info_free.setVisibility(View.GONE);
        } else {
            tv_free_money.setText(mData.getFree_money());
            ll_info_free.setVisibility(View.GONE);
        }

        loading = createLoadingDialog(mContext, "加载中", true);
        ((TextView) findViewById(R.id.tv_date_incoming)).setText(mData.getDate_incoming());
        ((TextView) findViewById(R.id.tv_date_first_repayment)).setText(mData.getDate_first_repayment());
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_contuine).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_view_account).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MemorySave.MS.mIsGoFinanceRecord = true;
                        Intent intent = new Intent(mContext, MainActivity.class);
                        MemorySave.MS.mIsGoAccount = true;
                        startActivity(intent);
                        finish();
                    }
                });

        sendBroadcast(MY_RESON_FINANCE_SUCCESS_URL);
        if (null == mData.getCan_shake() || !mData.getCan_shake().equals("1")) {
            mShowpw = false;
        } else {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            initShake();
        }
        if (mData.getIs_pre_sale() != null && mData.getIs_pre_sale().equals("1")) {
            ll_repay.setVisibility(View.VISIBLE);
            tv_date_repay.setText(mData.getStart_bid_date());
        } else {
            ll_repay.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHttpAdsService.getAdsByUid();
        toShare(true);
        if (mShowpw) {
            sm = (SensorManager) getSystemService(SENSOR_SERVICE);
            List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (sensors.size() > 0) {
                sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
            }
            new SPWHandler().sendEmptyMessageDelayed(100, 50);
        } else if (!isfirstGoActivity && !CommonUtil.isNullOrEmpty(mData.getIf_open())
                && mData.getIf_open().equals("1")) {
            isfirstGoActivity = true;
            Intent intent_activity = new Intent(mContext, MainWebActivity.class);
            intent_activity.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + mData.getOpen_url());
            intent_activity.putExtra("title", mData.getOpen_url_title());
            startActivity(intent_activity);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showHongbao();
    }

    class SPWHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (redSpw == null) {
                redSpw = new RedShakePopupWindow((Activity) mContext, InvestSuccessActivity.this);
            }
            redSpw.showAtLocation(findViewById(R.id.ll_top), Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 分享
     */
    private void toShare(final boolean isInit) {
        hgss.gainGetShare(isInit);
    }

    void initShake() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Inicializamos los atributos
        sonido = MediaPlayer.create(this, R.raw.touch);
        sonifinish = MediaPlayer.create(this, R.raw.touchfinish);
        // fonido = MediaPlayer.create(this, R.raw.shake_match);
        cont = 0;
        mAccel = 0;
        last_update = 0;
        curX = curY = curZ = 0;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        history_size = 40; // Tama�o del vector
        umbral_shake = 6; // Valor de gravedad para considerar una agitaci�n
        umbral_cont = 3; // n� de agitaciones necesarias para que sea una
        // agitaci�n completa
        umbral_pos = 7f; // Valor necesario para detectar que el dispositivo
        // est� en una posici�n

        // Ponemos el vector a cero
        for (int i = 0; i < history_size; i++)
            mov_history.add(0f);
    }

    int contn, contp, cont, tmp;
    private long last_update, current_time, time_difference; // Para controlar
    // el tiempo
    private float delta;
    private float mAccel;
    private float mAccelLast;
    private float mAccelCurrent;
    // Para controlar y detectar las agitaciones
    private int history_size, umbral_shake, umbral_cont;
    private ArrayList<Float> mov_history = new ArrayList<Float>();
    private float curX, curY, curZ; // Para almacenar el valor de cada eje
    private float umbral_pos; // Para detectar la posici�n
    public boolean isRun = false;
    private MediaPlayer sonido = null;// ,fonido=null; //Reproductor de sonidos
    private MediaPlayer sonifinish = null;// ,fonido=null; //Reproductor de
    // sonidos
    SensorManager sm;

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            if (isRun)
                return;
            // Monitoreamos la frecuencia de muestreo del acelerometro
            current_time = event.timestamp;
            time_difference = current_time - last_update;
            if (time_difference > 10E8 && cont != 0) {
                last_update = current_time;
                cont = 0;
            }
            cont++;

            // Obtenemos las lecturas de la aceleracion separada por ejes
            curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];

            // Comparamos la lectura de aceleracion anterior con la actual para
            // detectar movimiento de
            // aceleracion/desaceleracion. Como aceleracion se obtiene una
            // medida global que es la
            // ra�z cuadrada de la suma de los cuadrados de la aceleracion en
            // cada eje
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (curX * curX + curY
                    * curY + curZ * curZ));
            delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            mAccelLast = mAccelCurrent;
            // Almacenamos en una cola las history_size ultimas lecturas para la
            // deteccion del gesto shake
            mov_history.add(mAccel);
            mov_history.remove(0);

            // Contamos las lecturas mayores y menores que umbral_shake y
            // -umbral_shake
            // en mov_history
            contp = contn = 0;
            for (int i = 0; i < history_size; i++) {
                if (mov_history.get(i) > umbral_shake)
                    contp++;
                if (mov_history.get(i) < -umbral_shake)
                    contn++;
            }

            // Si hemos contado los suficientes registros por encima del valor
            // umbral de
            // aceleracion en mov_history entonces hemos detectado un SHAKE
            if (contp > umbral_cont && contn > umbral_cont) {
                // DO IT
                // sonido.start();
                // toshake();
                loadData(true);
                for (int i = 0; i < history_size; i++)
                    mov_history.set(i, 0f);
            }

        }
    }

    public boolean isStart = false;

    MyThread mLoadThread = new MyThread();

    class MyThread extends Thread {

        public boolean msonido = true;

        public void run() {
            if (isStart)
                return;
            isRun = true;
            final boolean tmsonido = msonido;
            if (tmsonido)
                sonido.start();
            isStart = true;
            hggss.gainGetShark(mData.getOrder_id(), tmsonido);

        }
    }

    private synchronized void loadData(boolean ms) {
        if (!mLoadThread.isDaemon()) {
            mLoadThread.msonido = ms;
            mLoadThread.run();
        }
    }

    @Override
    public void enter() {
        loadData(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (redSpw != null && redSpw.isShowing()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auto_contuine:
                Intent intent = new Intent(this, BespeakAreaActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_open_auto_invest_tips:
                String loadUrl;
                if (!isAutoInvestOpen) {
                    //  去开通
                    loadUrl = LCHttpClient.BASE_WAP_HEAD + "/#/openAutoBiding?phase=one ";
                    intent = new Intent(mContext, MainWebActivity.class);
                    intent.putExtra("web_url", loadUrl);
                    intent.putExtra("title", "自动投标授权");
                    startActivity(intent);
                } else {
                    ToastUtil.showLongToast(mContext, "您已经开通一键投资");
                }
                break;
        }
    }

    @Override
    public void gainGetSharksuccess(GetSharkJson response, boolean tmsonido) {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        try {
            if (response != null) {
                if (response.getBoolen() == 1) {
                    if (tmsonido)
                        sonifinish.start();
                    GetSharkData data = response.getData();
                    if (loading.isShowing()) {
                        loading.dismiss();
                    }
                    String num = data.getNum();// 红包的个数
                    String url = data.getUrl();// webview的地址
                    Intent intent = new Intent();
                    intent.setClass(mContext, MainWebActivity.class);
                    intent.putExtra("web_url", url);
                    intent.putExtra("order_id", mData.getOrder_id());
                    intent.putExtra("isRedShow", true);
                    startActivity(intent);
                    if (redSpw.isShowing()) {
                        redSpw.dismiss();
                        mShowpw = false;
                    }
                    // InvestSuccessActivity.this.finish();
                } else {
                    // showToast(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainGetSharkfail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        isStart = false;
    }

    @Override
    public void gainGetSharesuccess(GetShareJson response, boolean isInit) {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        try {
            if (response != null) {
                if (response.getBoolen() == 1) {
                    GetShareData data = response.getData();
                    if (loading.isShowing()) {
                        loading.dismiss();
                    }
                    String action = data.getAction();// 执行动作
                    // 1-跳转网页|3-跳转其他客户端
                    String img_url = data.getImg_url();// 图片地址
                    String title = data.getTitle();// 标题
                    String content = data.getContent();// 分享内容
                    String url = data.getUrl();// 链接
                    String btn_name = data.getBtn_name();//

                    if (isInit) {
                        return;
                    }
                    if (action.equals("1")) {
                        Intent intent = new Intent(mContext, MainWebActivity.class);
                        intent.putExtra("web_url", url);
                        intent.putExtra("title", title);
                        startActivity(intent);
                        InvestSuccessActivity.this.finish();
                        return;
                    } else if (action.equals("2")) {
                    } else if (action.equals("3")) {
                        oneKeyShareUtil.toShareForImageUrl(content, title, url,
                                img_url, new PlatformActionListener() {
                                    @Override
                                    public void onError(Platform arg0, int arg1, Throwable arg2) {
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainGetSharefail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
    }

    /**
     * 显示投资成功之后的弹窗，目前包括两种情况：红包和活动
     */
    public void showHongbao() {
        final String bonustitle = mData.getBonustitle();
        final String shareurl = mData.getShareurl();
        final String status = mData.getStatus();
        final String sharecontent = mData.getSharecontent();
        final String sharelogoimg = mData.getSharelogoimg();
        final String sharelogoico = mData.getSharelogoico();

        final String pic = mData.getPic();
        final String pic_link = mData.getPic_link();
        final List<FinancePayResultButton> button = mData.getButton();
        if (CommonUtil.isNullOrEmpty(status))
            return;
        if (!status.equals("1") && !status.equals("2")) {
            return;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.finance_success_hongbao, null);
        ImageView iv_center = (ImageView) view.findViewById(R.id.iv_center);
        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        TextView tv_share = (TextView) view.findViewById(R.id.tv_share);
        LinearLayout ll_button_contaner = (LinearLayout) view.findViewById(R.id.ll_button_container);
        View divider = view.findViewById(R.id.v_divider);
        mHongBaoPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mHongBaoPopupWindow.setBackgroundDrawable(dw);
        mHongBaoPopupWindow.setOutsideTouchable(true);

        // 如果为分享红包
        if (status.equals("1")) {
            tv_share.setVisibility(View.VISIBLE);
            ll_button_contaner.setVisibility(View.GONE);
            Glide.with(this).load(sharelogoimg)
                    .bitmapTransform(new RoundedCornersTransformation(this, 10, 0,
                            RoundedCornersTransformation.CornerType.ALL))
                    .into(iv_center);
            iv_close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit();
                }
            });
            tv_share.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    oneKeyShareUtil.toShareForImageUrlMSG("", sharecontent, bonustitle, shareurl, sharelogoico,
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
                }
            });
        } else if (status.equals("2")) { //  如果为活动窗口
            tv_share.setVisibility(View.GONE);
            ll_button_contaner.setVisibility(View.VISIBLE);
            iv_close.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mHongBaoPopupWindow.dismiss();
                }
            });
            Glide.with(this).load(pic)
                    .bitmapTransform(new RoundedCornersTransformation(this, 10, 0,
                            RoundedCornersTransformation.CornerType.TOP))
                    .into(iv_center);
            iv_center.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtil.isNullOrEmpty(pic_link)) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, MainWebActivity.class);
                        intent.putExtra("title", "幸运刮刮卡");
                        if (pic_link.startsWith("http")) {
                            intent.putExtra("web_url", pic_link);
                        } else {
                            intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/" + pic_link);
                        }
                        intent.putExtra("need_header", 1);
                        startActivity(intent);
                    }
                }
            });
            tv_share.setVisibility(View.GONE);
            if (button.size() == 1) {
                divider.setVisibility(View.GONE);
            } else {
                divider.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < button.size(); i++) {
                FinancePayResultButton financePayResultButton = button.get(i);
                final String name = financePayResultButton.getName();
                final String hrefType = financePayResultButton.getHrefType();
                final String btnPic = financePayResultButton.getPic();
                final String href = financePayResultButton.getHref();
                final String need_header = financePayResultButton.getNeed_header();

                TextView textView = null;
                if (i == 0) {
                    textView = (TextView) ll_button_contaner.findViewById(R.id.tv_action_first);
                } else if (i == 1) {
                    textView = (TextView) ll_button_contaner.findViewById(R.id.tv_action_second);
                }
                if (textView != null) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(name);
//                    ImageLoader.getInstances().displayImage(btnPic, imageView);
                    textView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (hrefType != null) {
                                if (hrefType.equals("wap")) {
                                    if (href != null && !href.isEmpty()) {
                                        Intent intent = new Intent();
                                        intent.setClass(mContext, MainWebActivity.class);
                                        intent.putExtra("title", name);
                                        if (href.startsWith("http")) {
                                            intent.putExtra("web_url", href);
                                        } else {
                                            intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/" + href);
                                        }
                                        intent.putExtra("need_header", need_header);
                                        startActivity(intent);
                                    }
                                } else {
                                    if ("jxtz".equals(hrefType)) {
                                        finish();
                                    }
                                }
                            }
                        }
                    });
                }

            }
        }

        v = findViewById(R.id.main);
        mHongBaoPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    private void exit() {
        mExitDialog = createDialog("关闭后本次分享机会将会流失哦", "确定", "取消",
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mExitDialog.isShowing()) {
                            mExitDialog.dismiss();
                            mHongBaoPopupWindow.dismiss();
                        }
                    }
                },
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mExitDialog.isShowing()) {
                            mExitDialog.dismiss();
                        }
                    }
                }
        );

        if (!mExitDialog.isShowing()) {
            mExitDialog.show();
        }
    }

    @Override
    public void getAdsSuccess(AdsJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                AdsData data = response.getData();
                if (data != null) {
                    final String id = data.getId();
                    final String image_url = data.getImg_url();
                    final String link_url = data.getLink_url();
                    if (!CommonUtil.isNullOrEmpty(image_url)) {
                        Glide.with(mContext).load(image_url).into(iv_ads);
                        iv_ads.setVisibility(View.VISIBLE);
                        iv_ads.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("web_url", link_url);
                                intent.putExtra("title", "");
                                if (link_url.contains("com.toubaojia.tbj") || link_url.contains("toubaojia.com")) {
                                    intent.putExtra("need_header", "0");
                                }
                                intent.setClass(mContext, MainWebActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        iv_ads.setVisibility(View.GONE);
                    }
                } else {
                    iv_ads.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void getAdsFail() {
    }
}