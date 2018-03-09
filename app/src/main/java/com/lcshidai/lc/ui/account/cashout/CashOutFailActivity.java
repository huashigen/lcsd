package com.lcshidai.lc.ui.account.cashout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.MainActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.MemorySave;

/**
 * Created by Allin on 2016/7/18.
 */
public class CashOutFailActivity extends TRJActivity implements View.OnClickListener{
    private ImageButton top_back_btn;
    private TextView top_title_text;
    private TextView tv_error_msg;
    private TextView tv_invest;
    private TextView tv_account;

    private int error_code;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_cashout_fail);

        error_code = getIntent().getIntExtra("code", 0);
        initView();
    }

    private void initView(){
        top_back_btn = (ImageButton) findViewById(R.id.ib_top_bar_back);
        top_back_btn.setOnClickListener(this);

        top_title_text = (TextView) findViewById(R.id.tv_top_bar_title);
        top_title_text.setText("提现");

        tv_error_msg = (TextView) findViewById(R.id.tv_error_msg);
        if(error_code == 1001){
            tv_error_msg.setText("*浙商银行规定，周末和节假日不能发起≥5万的提现，您可以提现5万以下的金额。");
        }

        tv_invest = (TextView) findViewById(R.id.tv_invest);
        tv_invest.setOnClickListener(this);

        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_account.setOnClickListener(this);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_top_bar_back:
                finish();
                break;
            case R.id.tv_invest:
                Intent intent = new Intent(this, MainActivity.class);
                MemorySave.MS.mIsGoFinance = true;
                startActivity(intent);
                finish();
                break;
            case R.id.tv_account:
                Intent intent2 = new Intent(this, MainActivity.class);
                MemorySave.MS.mIsGoAccount = true;
                startActivity(intent2);
                finish();
                break;
        }
    }

}
