package com.lcshidai.lc.ui.finance;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.fragment.finance.FinaceMovementChildFragment;

/**
 * 热门活动页面
 * Created by a on 2016/5/27.
 */
public class FinaceMovementActivity extends TRJActivity implements View.OnClickListener {
    private int nowFragment = FIR_FRAGMENT;
    private static final int FIR_FRAGMENT = 101;
    private static final int SEC_FRAGMENT = 102;
    private static final int THI_FRAGMENT = 103;

    private Resources resources;

//    private TextView tv_tab_1, tv_tab_2, tv_tab_3;

    private ImageButton top_back_btn;
    private TextView top_title_text;

    FinaceMovementChildFragment mfisrtFragment;
//    FinaceMovementChildFragment mSecondFragment;
//    FinaceMovementChildFragment mThirdFragment;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fragment_finane_movement);
        resources = getResources();
        InitTextView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void InitTextView() {
        top_back_btn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        top_back_btn.setOnClickListener(this);
        top_title_text = (TextView) findViewById(R.id.tv_top_bar_title);
        top_title_text.setText("热门活动");
        mfisrtFragment = new FinaceMovementChildFragment();
        Bundle B1 = new Bundle();
        B1.putString("type", "2");
        mfisrtFragment.setArguments(B1);
        switchFragment(FIR_FRAGMENT, true);
    }

    public void switchFragment(int code, boolean isFirst) {
        try {

            Fragment fragment = null;
            nowFragment = code;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            switch (code) {
                case FIR_FRAGMENT:
                    fragment = mfisrtFragment;
                    break;
//                case SEC_FRAGMENT:
//                    fragment = mSecondFragment;
//                    break;
//                case THI_FRAGMENT:
//                    fragment = mThirdFragment;
//                    break;

                default:
                    return;
            }

            if (isFirst) {
                fragmentTransaction.add(R.id.ll_fragment, fragment);
            } else {
                fragmentTransaction.replace(R.id.ll_fragment, fragment);
            }
            fragmentTransaction.commit();
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                finish();
                break;
            default:
                break;
        }

    }

}
