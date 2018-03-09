package com.lcshidai.lc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.VerifyView;

/**
 * 注册图形验证码界面
 * <p/>
 * Created by Randy on 2016/5/12.
 */
public class ImageVerifyCodeRegisterActivity extends TRJActivity {
    private VerifyView verifyView;
    private TextView mTipView;
    private SeekBar mSeekBar;
    private ImageView mResetCode;
    private TextView tvNextStep;

    private String username;
    private int status;
    private int res_pop = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_verify_code_register);

        initView();

    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        username = getIntent().getStringExtra("mobile");
        status = getIntent().getIntExtra("status", 0);
        res_pop = getIntent().getIntExtra("res_pop", 0);

        verifyView = (VerifyView) findViewById(R.id.dy_v);
        mSeekBar = (SeekBar) findViewById(R.id.sb_dy);
        mTipView = (TextView) findViewById(R.id.tv_tip);
        mResetCode = (ImageView) findViewById(R.id.iv_dynamic_code_reset);
        tvNextStep = (TextView) findViewById(R.id.tv_next_step);
        tvNextStep.setEnabled(false);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Log.e("main", "当前位置" + i);
                verifyView.setUnitMoveDistance(verifyView.getAverageDistance(seekBar.getMax()) * i);
                if (i == 0) {
                    mTipView.setVisibility(View.VISIBLE);
                } else {
                    mTipView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                verifyView.testPuzzle();
            }

        });
        // 先设置为false，在请求完接口后，得到验证图片生成的位置后，设置验证图片的x、y坐标后，设置该flag为true；
        verifyView.setPaintUnitFlag(true);

        verifyView.setPuzzleListener(new VerifyView.onPuzzleListener() {
            @Override
            public void onSuccess() {
                // mSeekBar.setEnabled(false);
                Toast.makeText(ImageVerifyCodeRegisterActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                mSeekBar.setProgress(0);
                verifyView.reSet();
                // 进行跳转
                tvNextStep.setEnabled(true);
                tvNextStep.setBackground(getResources().getDrawable(R.drawable.bg_button_clickable_true));

            }

            @Override
            public void onFail() {
                Toast.makeText(ImageVerifyCodeRegisterActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                mSeekBar.setProgress(0);
                verifyView.reSet();
                // 恢复到初始状态，重新获取坐标，重新验证
                tvNextStep.setEnabled(false);
                tvNextStep.setBackground(getResources().getDrawable(R.drawable.bg_button_clickable_false));
            }
        });

        mResetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(0);
                verifyView.reSet();
                tvNextStep.setEnabled(false);
                tvNextStep.setBackground(getResources().getDrawable(R.drawable.bg_button_clickable_false));
            }
        });

        tvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageVerifyCodeRegisterActivity.this, UserRegisterSecActivity.class);
                intent.putExtra("mobile", username);
                intent.putExtra("status", status);
                intent.putExtra("res_pop", 1);
                startActivity(intent);
                finish();
            }
        });
    }
}