package com.lcshidai.lc.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.ViewFlow;
import com.lcshidai.lc.widget.ViewFlow.ViewSwitchListener;
import com.lcshidai.lc.widget.ViewFlowImageAdapter;

/**
 * 欢迎页
 */
public class WelcomeGuideActivity extends TRJActivity implements OnClickListener {

    private ViewFlow first_viewflow;
//    private int[] drawables = {R.drawable.start_a, R.drawable.start_b, R.drawable.start_c, R.drawable.start_d, R.drawable.start_e};
    private int[] drawables = {R.drawable.start_a, R.drawable.start_b, R.drawable.start_c};
    private LinearLayout data_ll, button_ll, button_ll_2;
    private ImageView ivStart;
    private TextView start_reg, start_bt;
    private int lastPosition = -1;
    private Animation dismissAnim, showAnim;

    private TextView money_tv, people_tv;
    private String totalIncome = "";    //总收入
    private String totalMembers = "";    //总人数
    private FirstDataReceiver fdr;
    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setAttributes(new WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (null != getIntent()) {
            totalIncome = getIntent().getStringExtra("totalIncome");
            totalMembers = getIntent().getStringExtra("totalMembers");
            status = getIntent().getIntExtra("status", 0);
        }

//        暂无版本更新图片，以后添加
//        if (status == 2) {
//            drawables = new int[]{R.drawable.start_1, R.drawable.start_2, R.drawable.start_3, R.drawable.start_4};
//        }

        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }




    @Override
    protected void onResume() {
        super.onResume();
        goHandler.sendEmptyMessageDelayed(100, 6500);
    }

    private void initView() {
        setContentView(R.layout.activity_welcome_guide);
        fdr = new FirstDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FIRST_ACTIVITY_DATA);
        registerReceiver(fdr, filter);

        first_viewflow = (ViewFlow) findViewById(R.id.first_viewflow);
        data_ll = (LinearLayout) findViewById(R.id.first_bottom_data);
        button_ll = (LinearLayout) findViewById(R.id.first_bottom_button);
        button_ll_2 = (LinearLayout) findViewById(R.id.first_bottom_button_2);
        ivStart = (ImageView) findViewById(R.id.iv_start);
        start_bt = (TextView) findViewById(R.id.first_bottom_button_start);
        start_reg = (TextView) findViewById(R.id.first_bottom_button_reg);
        money_tv = (TextView) findViewById(R.id.first_data_tv_money);
        people_tv = (TextView) findViewById(R.id.first_data_tv_people);
        start_bt.setOnClickListener(this);
        start_reg.setOnClickListener(this);
        button_ll_2.setOnClickListener(this);
        ivStart.setOnClickListener(this);

        if (!CommonUtil.isNullOrEmpty(totalIncome)) {
            money_tv.setText(totalIncome);
        }
        if (!CommonUtil.isNullOrEmpty(totalMembers)) {
            people_tv.setText(totalMembers);
        }

        dismissAnim = AnimationUtils.loadAnimation(mContext, R.anim.fade_dismiss);
        showAnim = AnimationUtils.loadAnimation(mContext, R.anim.fade_show);
        dismissAnim.setFillAfter(false);
        showAnim.setFillAfter(false);
        dismissAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                data_ll.setVisibility(View.GONE);
                data_ll.clearAnimation();
            }
        });
        showAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                data_ll.setVisibility(View.VISIBLE);
                data_ll.clearAnimation();

            }
        });

        first_viewflow.setAdapter(new ViewFlowImageAdapter(mContext, drawables));
        first_viewflow.setOnViewSwitchListener(new ViewSwitchListener() {

            @Override
            public void onSwitched(View view, int position) {
                if ((position + 1) == drawables.length) {
                    if (status == 2) {
                        ivStart.setVisibility(View.VISIBLE);
                    } else {
                        button_ll.setVisibility(View.VISIBLE);
                    }
                } else {
                    button_ll.setVisibility(View.GONE);
                    ivStart.setVisibility(View.GONE);
                    button_ll_2.setVisibility(View.GONE);
                }
                lastPosition = position;
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(fdr);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_bottom_button_reg:
                Intent intent = new Intent();
                intent.setClass(mContext, UserRegisterFirActivity.class);
                intent.putExtra("goClass", MainActivity.class.getName());
                intent.putExtra("is_back_to_main", true);
                startActivity(intent);
                finish();
                break;
            case R.id.first_bottom_button_start:
                clickEv();
                break;
            case R.id.first_bottom_button_2:
                clickEv();
                break;
            case R.id.iv_start:
                clickEv();
                break;
        }
    }

    private void clickEv() {
        int gestureTimes = SpUtils.getInt(SpUtils.Table.CONFIG, SpUtils.Config.TOTAL_TRY_TIMES, 5);
        Intent intent = new Intent();
        if (GoLoginUtil.isShowGestureLogin(mContext)) {
            if (gestureTimes > 0) {
                intent.setClass(mContext, GestureLoginActivity.class);
            } else if (gestureTimes == 0) {
                ToastUtil.showToast(mContext, "您已输错5次手势密码，请重新登陆。");
                SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.TOTAL_TRY_TIMES, -1);
                intent.setClass(mContext, LoginActivity.class);
                intent.putExtra("goClass", MainActivity.class.getName());
                intent.putExtra("is_back_to_main", true);
            } else {
                intent.setClass(mContext, MainActivity.class);
            }
        } else {
            intent.setClass(mContext, MainActivity.class);
            int firstStartFlag = SpUtils.getInt(SpUtils.Table.CONFIG, SpUtils.Config.IS_FIRST_OPEN, 0);
            if (firstStartFlag == 0) {
                intent.putExtra("first_start", "1");
            }
        }
        startActivity(intent);
        dealWithSpFileCompat();
        finish();
    }

    /**
     * sp 兼容处理（下个版本中将移除）
     */
    private void dealWithSpFileCompat() {
        CommonUtil.clearAllSpFiles();
        // 设置为不是第一次打开App
        SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.IS_FIRST_OPEN, 1);
        // 是否需要更新版本号
        int preVersioncode = SpUtils.getInt(SpUtils.Table.CONFIG, SpUtils.Config.VERSION_CODE, 0);
        int nowVersionCode = CommonUtil.getVersionCode(mContext, getPackageName());
        if (preVersioncode < nowVersionCode) {
            SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.VERSION_CODE, nowVersionCode);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (!CommonUtil.isNullOrEmpty(totalIncome)) {
                money_tv.setText(totalIncome);
            }
            if (!CommonUtil.isNullOrEmpty(totalMembers)) {
                people_tv.setText(totalMembers);
            }
        }

    };

    public class FirstDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_FIRST_ACTIVITY_DATA.equals(intent.getAction())) {
                totalIncome = intent.getStringExtra("totalIncome");
                totalMembers = intent.getStringExtra("totalMembers");
                mHandler.sendEmptyMessage(1);
            }
        }

    }

    GifGoneHandler goHandler = new GifGoneHandler();

    class GifGoneHandler extends Handler {

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    }
}

