package com.lcshidai.lc.ui.account.pwdmanage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.UserMobilePayPwdImpl;
import com.lcshidai.lc.model.account.UserMobilePayPwdData;
import com.lcshidai.lc.model.account.UserMobilePayPwdJson;
import com.lcshidai.lc.service.account.HttpUserMobilePayPwdService;
import com.lcshidai.lc.ui.account.UserPayPwdSetActivity;
import com.lcshidai.lc.ui.base.TRJActivity;

/**
 * 重置手机支付密码
 */
public class UserMobilePayPwdActivity extends TRJActivity implements UserMobilePayPwdImpl {
    HttpUserMobilePayPwdService humpps;
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    private LinearLayout ll_pay_pwd, ll_real, ll_bing_bank, ll_set_sqa, ll_tele;
    //	private View mProgressContainer;
    private String TELE_NUMBER = "400-900-1000";
    private TextView telephone_tv2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mobile_pay_pwd);
        humpps = new HttpUserMobilePayPwdService(this, this);
        initView();
        loadData();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("重置手机支付密码");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        ll_pay_pwd = (LinearLayout) findViewById(R.id.ll_pay_pwd);
        ll_real = (LinearLayout) findViewById(R.id.ll_real);
        ll_bing_bank = (LinearLayout) findViewById(R.id.ll_bing_bank);
        ll_set_sqa = (LinearLayout) findViewById(R.id.ll_set_sqa);
        ll_tele = (LinearLayout) findViewById(R.id.channel_ll_tele);
        telephone_tv2 = (TextView) findViewById(R.id.channel_telenumber_tv2);
        telephone_tv2.getPaint().setFakeBoldText(true);
        telephone_tv2.setText(TELE_NUMBER);
        ll_pay_pwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, UserPayPwdSetActivity.class);
                intent.putExtra("flag", 1);
                startActivity(intent);
            }
        });
        ll_real.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, UserMobileCodeActivity.class);
                intent.putExtra("to_activity", "real");
                startActivity(intent);
            }
        });
        ll_bing_bank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, UserMobileCodeActivity.class);
                intent.putExtra("to_activity", "bank");
                startActivity(intent);
            }
        });
        ll_set_sqa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, UserMobileCodeActivity.class);
                intent.putExtra("to_activity", "sqa");
                startActivity(intent);
            }
        });
        ll_tele.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:" + TELE_NUMBER.replaceAll("-", "")));
                startActivity(phoneIntent);
            }
        });
    }

    private void loadData() {
        humpps.gainUserMobilePayPwd();
        showLoadingDialog(mContext, getString(R.string.under_loading), true);
    }

    @Override
    public void gainUserMobilePayPwdsuccess(UserMobilePayPwdJson response) {
        try {
            hideLoadingDialog();
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    UserMobilePayPwdData json = response.getData();
                    if (json != null) {
                        String is_paypwd_mobile_set = json.getIs_paypwd_mobile_set();
                        String is_id_auth = json.getIs_id_auth();
                        String is_binding_bank = json.getIs_binding_bank();
                        String is_set_sqa = json.getIs_set_sqa();
                        if (is_paypwd_mobile_set.equals("1")) {
                            ll_pay_pwd.setVisibility(View.VISIBLE);
                        } else {
                            ll_pay_pwd.setVisibility(View.GONE);
                        }
                        if (is_id_auth.equals("1")) {
                            ll_real.setVisibility(View.VISIBLE);
                        } else {
                            ll_real.setVisibility(View.GONE);
                        }
                        if (is_set_sqa.equals("1")) {
                            ll_set_sqa.setVisibility(View.VISIBLE);
                        } else {
                            ll_set_sqa.setVisibility(View.GONE);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainUserMobilePayPwdfail() {
        hideLoadingDialog();
    }
}
