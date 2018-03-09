package com.lcshidai.lc.ui.account.user;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.ResetPwdImpl;
import com.lcshidai.lc.model.account.ResetPwdJson;
import com.lcshidai.lc.service.account.HttpResetPwdService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIconPwd;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 忘记密码通用页面
 *
 * @author 000853
 */
public class ForgetPasswordGeneralActivity extends TRJActivity implements ResetPwdImpl {
    HttpResetPwdService hrps;

    private CustomEditTextLeftIconPwd edit_pwd, edit_pwd_re;
    private View rl_next, btn_option, tv_subtitle;
    private TextView tv_title;
    // direct: 直接设置(只手机号码注册的情况下)
    // identity: 通过实名信息校验
    // bank: 通过绑定银行卡信息校验
    // sqa: 通过安保问题校验
    private String setType;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_forget_pwd_third);
        if (null != getIntent()) {
            setType = getIntent().getStringExtra("set_type");
        }
        hrps = new HttpResetPwdService(this, this);

        edit_pwd = (CustomEditTextLeftIconPwd) findViewById(R.id.edit_pwd);
        edit_pwd_re = (CustomEditTextLeftIconPwd) findViewById(R.id.edit_pwd_re);
        rl_next = findViewById(R.id.rl_next);

        Drawable pwd_icon = getResources().getDrawable(R.drawable.icon_pwd);
        edit_pwd.setIcon(pwd_icon);
        edit_pwd_re.setIcon(pwd_icon);

        edit_pwd.setHint("新密码");
        edit_pwd_re.setHint("确认新密码");

        btn_option = findViewById(R.id.btn_option);
        tv_subtitle = findViewById(R.id.tv_subtitle);
        tv_title = (TextView) findViewById(R.id.tv_top_bar_title);

        btn_option.setVisibility(View.INVISIBLE);
        tv_subtitle.setVisibility(View.GONE);

        tv_title.setText("忘记密码");
        rl_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                next();
            }
        });
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!setType.equals("direct")) {
                    setResult(22);
                }
                finish();
            }
        });
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void next() {
        hrps.gainResetPwd(setType, edit_pwd.getEdtText(), edit_pwd_re.getEdtText());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!setType.equals("direct")) {
                setResult(22);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void doFinish() {
        setResult(22);
        super.doFinish();
    }

    @Override
    public void gainResetPwdsuccess(ResetPwdJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    ToastUtil.showToast(mContext, "修改成功");
                    if (!setType.equals("direct")) {
                        setResult(22);
                    }
                    finish();
                } else {
                    if ("-1".equals(response.getData())) {
                        ToastUtil.showToast(mContext, "手机确认超时，请完成手机确认");
                        if (setType.equals("direct")) {
                            Intent intent = new Intent(mContext, ForgetPasswordFirstActivity.class);
                            startActivity(intent);
                        } else {
                            setResult(21);
                        }
                        finish();
                    } else {
                        String message = response.getMessage();
                        ToastUtil.showToast(mContext, message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainResetPwdfail() {
        ToastUtil.showToast(mContext, "网络不给力");
    }

}