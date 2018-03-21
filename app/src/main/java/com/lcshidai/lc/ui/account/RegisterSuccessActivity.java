package com.lcshidai.lc.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.MainActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.DataBuriedManager;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.SpUtils;

/**
 * 注册成功界面
 *
 * @author Administrator
 */
public class RegisterSuccessActivity extends TRJActivity implements OnClickListener {

    private ImageView mBackImg;
    private TextView mOption, mSubmit, mTime, redMoney;
    private String money = "", comment = "", rule = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            money = bundle.getString("money");
            comment = bundle.getString("comment");
            rule = bundle.getString("rule");
        }
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_SUCCESS_IN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_SUCCESS_OUT);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    public void initView() {
        setContentView(R.layout.activity_register_success);
        mBackImg = (ImageView) findViewById(R.id.withdraw_succ_back);
        mOption = (TextView) findViewById(R.id.withdraw_succ_option);
        mSubmit = (TextView) findViewById(R.id.withdraw_succ_submit);
        mTime = (TextView) findViewById(R.id.withdraw_succ_time);
        redMoney = (TextView) findViewById(R.id.red_money);
        redMoney.setText(money + "理财金");
        mBackImg.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdraw_succ_back:
            case R.id.withdraw_succ_submit:
                // 埋点
                DataBuriedManager.onEventTag(mContext, SpUtils.getString(SpUtils.Table.CONFIG,
                        SpUtils.Config.REG_FLOW), Constants.DataBuried.REG_SUCCESS_I_KNOW);
                Intent i = new Intent();
//                i.putExtra("flag", true);
//                i.setClass(mContext, FinancialCashActivity.class);
                i.putExtra("tempId", 1);
                //跳转到投资页面
                MemorySave.MS.mIsGoFinance = true;
                i.setClass(mContext, MainActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.withdraw_succ_option:
                break;
            default:
                break;
        }
    }


}
