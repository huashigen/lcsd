package com.lcshidai.lc.ui.account;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.UserPayPwdSetImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpUserPayPwdSetService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIconPwdNew;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 支付密码设置
 */
public class UserPayPwdSetActivity extends TRJActivity implements UserPayPwdSetImpl {
    HttpUserPayPwdSetService huppss;
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    private CustomEditTextLeftIconPwdNew edit_old_pwd, edit_new_pwd, edit_again_pwd;
    private Button btn_submit;
    private int flag = 0;
    private LinearLayout ll_old_pwd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pay_pwd_updater);
        Intent args = getIntent();
        if (null != args) {
            if (args.hasExtra("flag")) {
                flag = args.getIntExtra("flag", 0);
            }
        }
        huppss = new HttpUserPayPwdSetService(this, this);
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        Drawable draw_pwd_icon = getResources().getDrawable(R.drawable.icon_pwd);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("修改手机支付密码");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        ll_old_pwd = (LinearLayout) findViewById(R.id.ll_old_pwd);
        edit_old_pwd = (CustomEditTextLeftIconPwdNew) findViewById(R.id.edit_old_pwd);
        edit_new_pwd = (CustomEditTextLeftIconPwdNew) findViewById(R.id.edit_new_pwd);
        edit_again_pwd = (CustomEditTextLeftIconPwdNew) findViewById(R.id.edit_again_pwd);
        edit_old_pwd.setIcon(draw_pwd_icon);
        edit_new_pwd.setIcon(draw_pwd_icon);
        edit_again_pwd.setIcon(draw_pwd_icon);
        edit_old_pwd.setHint("旧密码");
        edit_new_pwd.setHint("输入6位数新密码");
        edit_again_pwd.setHint("确认新密码");
        edit_old_pwd.setTextSize(14);
        edit_new_pwd.setTextSize(14);
        edit_again_pwd.setTextSize(14);
        if (flag == 1) {
            ll_old_pwd.setVisibility(View.VISIBLE);
        } else {
            ll_old_pwd.setVisibility(View.GONE);
        }
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitData();
            }
        });
    }

    private void onSubmitData() {
        String new_pwd = edit_new_pwd.getEdtText().trim();
        String again_pwd = edit_again_pwd.getEdtText().trim();
        String old_pwd = "";
        if (flag == 1) {
            old_pwd = edit_old_pwd.getEdtText().toString().trim();
            if (old_pwd.equals("")) {
                createDialogDismissAuto("请填写旧密码");
                return;
            }
        }

        if (new_pwd.equals("")) {
            createDialogDismissAuto("请填写新密码");
            return;
        }
        if (again_pwd.equals("")) {
            createDialogDismissAuto("请填写确认新密码");
            return;
        }
        btn_submit.setEnabled(false);

        huppss.gainUserPayPwdSet(old_pwd, new_pwd, again_pwd, flag);
        showLoadingDialog(mContext, "正在加载", true);
    }

    @Override
    public void gainUserPayPwdSetsuccess(BaseJson response) {
        try {
            hideLoadingDialog();
            btn_submit.setEnabled(true);
            if (response != null) {
                String boolen = response.getBoolen();
                String message = "";
                if (boolen.equals("1")) {
                    showToast("修改成功");
                } else {
                    showToast(response.getMessage());
                }
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainUserPayPwdSetfail() {
        hideLoadingDialog();
        btn_submit.setEnabled(true);
    }
}
