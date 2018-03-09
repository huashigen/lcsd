package com.lcshidai.lc.ui.account;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.AccountChangeUnameImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpAccountChangeUnameService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 修改用户名
 */
public class ChangeUsernameActivity extends TRJActivity implements AccountChangeUnameImpl {
    HttpAccountChangeUnameService hacus;
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    Dialog dialog;
    com.lcshidai.lc.widget.text.CustomEditTextLeftIcon uname;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_change_username);
        hacus = new HttpAccountChangeUnameService(this, this);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("修改用户名");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        uname = (CustomEditTextLeftIcon) findViewById(R.id.uname);
        findViewById(R.id.btn_save).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveUname();
            }
        });
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    String muname = "";

    public void saveUname() {
        if (uname.getEdtText().equals("")) {
            ToastUtil.showToast(this, "请填写用户名!");
            return;
        }
        muname = uname.getEdtText();
        dialog = TRJActivity.createLoadingDialog(this, "保存...", true);
        if (!dialog.isShowing()) {
            dialog.show();
        }

        hacus.gainAccountChangeUname(muname);
    }

    @Override
    public void gainAccountChangeUnamesuccess(BaseJson response) {
        dialog.dismiss();
        if (response.getBoolen().equals("1")) {
            ToastUtil.showToast(mContext, "用户名修改成功!");
            GoLoginUtil.autoLogin(mContext, muname);
            finish();
        }
    }


    @Override
    public void gainAccountChangeUnamefail() {
        dialog.dismiss();
        ToastUtil.showToast(mContext, "用户名修改失败!");
    }
}
