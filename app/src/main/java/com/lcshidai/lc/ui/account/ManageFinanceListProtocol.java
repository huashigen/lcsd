package com.lcshidai.lc.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.AgreementActivity;
import com.lcshidai.lc.ui.base.TRJActivity;

public class ManageFinanceListProtocol extends TRJActivity {

    public String pre_protocol;// - 借款合同 链接
    public String server_protocol;//-服务协议链接
    public boolean isOrderID;
    public String orderID;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            pre_protocol = args.getString("pre_protocol");
            server_protocol = args.getString("server_protocol");
            isOrderID = args.getBoolean("isOrderID", false);
            orderID = args.getString("id");
        }
        setContentView(R.layout.activity_managefinance_protocol);
        findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        TextView mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("查看合同");
        TextView mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        findViewById(R.id.pp_btn_right1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageFinanceListProtocol.this, AgreementActivity.class);
                intent.putExtra("title", "预约服务协议");
                intent.putExtra("url", server_protocol);
                startActivity(intent);
            }
        });
        findViewById(R.id.pp_btn_right2).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageFinanceListProtocol.this, AgreementActivity.class);
                if (isOrderID) {
                    intent.putExtra("title", "");
                    intent.putExtra("id", orderID);
                    startActivity(intent);
                } else {
                    intent.putExtra("title", "借款合同");
                    intent.putExtra("url", pre_protocol);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }
}
