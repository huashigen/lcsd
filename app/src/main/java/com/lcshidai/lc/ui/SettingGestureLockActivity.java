package com.lcshidai.lc.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.LockPatternUtils;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.widget.LockPatternView;
import com.lcshidai.lc.widget.LockPatternView.Cell;

import java.util.ArrayList;
import java.util.List;


public class SettingGestureLockActivity extends TRJActivity implements
        LockPatternView.OnPatternListener, OnClickListener {
    private Context mContext;
    private Animation shakeAnim;

    private LockPatternView lockPatternView;
    private TextView information;
    private TextView resetPattern;
    private View previewViews[][] = new View[3][3];
    protected List<LockPatternView.Cell> mChosenPattern = null;
    private Drawable dra_pre_sele, dra_pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        dra_pre_sele = getResources().getDrawable(R.drawable.gesture_preview_bg_selected_new);
        dra_pre = getResources().getDrawable(R.drawable.gesture_preview_bg_new);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        setContentView(R.layout.activity_setting_gesturelock);
        mContext = this;
        initGesturePreview();
        resetPattern = (TextView) findViewById(R.id.gesture_setting_tv_reset);
        information = (TextView) findViewById(R.id.gesture_preview_tv_infomation);
        lockPatternView = (LockPatternView) findViewById(R.id.gesture_setting_lockview);
        lockPatternView.setTactileFeedbackEnabled(true);
        lockPatternView.setOnPatternListener(this);
        resetPattern.setOnClickListener(this);

        information.setTextColor(Color.parseColor("#63AF6F"));
        information.setText("绘制解锁图案");

        shakeAnim = AnimationUtils.loadAnimation(mContext, R.anim.wrong_alert_shake);
    }

    /**
     * 初始化手势预览图控件
     */
    private void initGesturePreview() {
        previewViews[0][0] = findViewById(R.id.gesture_preview_item_0);
        previewViews[0][1] = findViewById(R.id.gesture_preview_item_1);
        previewViews[0][2] = findViewById(R.id.gesture_preview_item_2);
        previewViews[1][0] = findViewById(R.id.gesture_preview_item_3);
        previewViews[1][1] = findViewById(R.id.gesture_preview_item_4);
        previewViews[1][2] = findViewById(R.id.gesture_preview_item_5);
        previewViews[2][0] = findViewById(R.id.gesture_preview_item_6);
        previewViews[2][1] = findViewById(R.id.gesture_preview_item_7);
        previewViews[2][2] = findViewById(R.id.gesture_preview_item_8);
        setGesturePreviewBackground(dra_pre == null ? getResources().getDrawable(R.drawable.gesture_preview_bg_new) : dra_pre);
    }

    /**
     * 设置手势预览图的背景色
     */
    private void setGesturePreviewBackground(Drawable draw) {
        for (int i = 0; i < previewViews.length; i++) {
            for (int j = 0; j < previewViews[i].length; j++) {
                previewViews[i][j].setBackgroundDrawable(draw);//.setBackgroundResource(bgId);
            }
        }
    }

    // clear the wrong pattern unless they have started a new one
    // already
    private void postClearPatternRunnable() {
        lockPatternView.removeCallbacks(mClearPatternRunnable);
        lockPatternView.postDelayed(mClearPatternRunnable, 2000);
    }


    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            lockPatternView.clearPattern();
        }
    };

    @Override
    public void onPatternStart() {
        lockPatternView.removeCallbacks(mClearPatternRunnable);
    }

    @Override
    public void onPatternCleared() {
        lockPatternView.removeCallbacks(mClearPatternRunnable);
    }

    @Override
    public void onPatternCellAdded(List<Cell> pattern) {
        if (mChosenPattern == null) {
            int row = pattern.get(pattern.size() - 1).getRow();
            int column = pattern.get(pattern.size() - 1).getColumn();
            previewViews[row][column].setBackgroundDrawable(dra_pre_sele == null ?
                    getDrawable(R.drawable.gesture_preview_bg_selected_new) : dra_pre_sele);
        }
    }

    @Override
    public void onPatternDetected(List<Cell> pattern) {
        if (pattern.size() < 4 && mChosenPattern == null) {
            information.setTextColor(getResources().getColor(R.color.theme_color));
            information.setText("最少连接4个点，请重新绘制");
            information.startAnimation(shakeAnim);
            lockPatternView.removeCallbacks(mClearPatternRunnable);
            lockPatternView.clearPattern();
            setGesturePreviewBackground(dra_pre == null ? getResources().getDrawable(R.drawable.gesture_preview_bg_new) : dra_pre);
        } else {
            if (mChosenPattern == null) {
                information.setTextColor(Color.parseColor("#63AF6F"));
                information.setText("再次绘制解锁图案");
                mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                lockPatternView.clearPattern();
            } else {
                if (mChosenPattern.equals(pattern)) {
                    try {
                        new LockPatternUtils(mContext).saveLockPattern(pattern);
                        information.setTextColor(Color.parseColor("#63AF6F"));
                        information.setText("手势密码已设置成功");
                        Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                        SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.USER_NAME, MemorySave.MS.userInfo.uname);
                        SpUtils.setString(SpUtils.Table.CONFIG, SpUtils.Config.UID, MemorySave.MS.userInfo.uid);
                        SpUtils.setBoolean(SpUtils.Table.CONFIG, SpUtils.Config.IS_GESTURE_OPEN, true);
                        SpUtils.setInt(SpUtils.Table.CONFIG, SpUtils.Config.TOTAL_TRY_TIMES, 5);
                        //实名认证后进入
                        if (getIntent() != null) {
                            Class clazz;
                            String nameStr = getIntent().getStringExtra("goClass");
                            if (nameStr != null && !nameStr.equals("")) {
                                Intent intent = getIntent();
                                clazz = Class.forName(nameStr);
                                intent.setClass(this, clazz);
                                if (MemorySave.MS.args != null)
                                    intent.putExtras(MemorySave.MS.args);
                                startActivity(intent);
                            }
                            getIntent().removeExtra("goClass");
                        }
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    lockPatternView.removeCallbacks(mClearPatternRunnable);
                    lockPatternView.clearPattern();
                    information.setTextColor(getResources().getColor(R.color.theme_color));
                    information.setText("与上次绘制不一致，请重新绘制");
                    information.startAnimation(shakeAnim);
                    if (resetPattern.getVisibility() == View.INVISIBLE) {
                        resetPattern.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gesture_setting_tv_reset:
                mChosenPattern = null;
                setGesturePreviewBackground(dra_pre == null ? getResources().getDrawable(R.drawable.gesture_preview_bg_new) : dra_pre);
                information.setTextColor(Color.parseColor("#63AF6F"));
                information.setText("绘制解锁图案");
                lockPatternView.clearPattern();
                v.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        dra_pre = null;
        dra_pre_sele = null;
        if (mChosenPattern != null) {
            mChosenPattern.clear();
            mChosenPattern = null;
        }
        System.gc();
        super.onDestroy();
    }
}
