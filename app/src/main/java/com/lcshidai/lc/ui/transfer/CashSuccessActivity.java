package com.lcshidai.lc.ui.transfer;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.OneKeyShareUtil;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class CashSuccessActivity extends TRJActivity {

    private TextView mTvTitle, mSubTitle;
    private ImageButton mBackBtn;
    private Button btn_contuine_c;

    private Dialog loading;
    private OneKeyShareUtil oneKeyShareUtil;

    private String msg;
    private String title;
    private String subTitle;
    private String prj_order_id;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            msg = args.getString("msg");
            title = args.getString("title");
            subTitle = args.getString("subTitle");
            prj_order_id = args.getString("prj_order_id");
        }
        MemorySave.MS.goToFinanceAll = true;
        setContentView(R.layout.activity_cash_success);
        mBackBtn = (ImageButton) this.findViewById(R.id.btn_back);
        findViewById(R.id.btn_option).setVisibility(View.INVISIBLE);
        mTvTitle = (TextView) this.findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText(title);
        mSubTitle = (TextView) this.findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setText(subTitle);

        oneKeyShareUtil = new OneKeyShareUtil(CashSuccessActivity.this);
        btn_contuine_c = (Button) findViewById(R.id.btn_contuine_c);
        loading = createLoadingDialog(CashSuccessActivity.this, "加载中", true);

        ((TextView) findViewById(R.id.tv_successinfo)).setText(msg);
        ((TextView) findViewById(R.id.tv_date_incoming)).setText("正在等待其他出借人投资.");
        ((TextView) findViewById(R.id.tv_date_first_repayment)).setText("资金一旦募集完成，将立即转入您的资金账户。");

        mBackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MemorySave.MS.isCashSuccessBack = true;
                finish();
            }
        });

        findViewById(R.id.btn_contuine).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MemorySave.MS.mIsGoFinanceRecord = true;
                finish();
            }
        });

        btn_contuine_c.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MemorySave.MS.isCashSuccessBack = true;
                finish();
            }
        });

        sendBroadcast(MY_RESON_FINANCE_SUCCESS_URL);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}