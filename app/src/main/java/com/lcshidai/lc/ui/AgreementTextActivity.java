package com.lcshidai.lc.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;

public class AgreementTextActivity extends FragmentActivity {
    private TextView mTvTitle;
    private ImageButton mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_text);
        mTvTitle = findViewById(R.id.tv_top_bar_title);
        mBackBtn = findViewById(R.id.ib_top_bar_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String title = getIntent().getStringExtra("title");
        mTvTitle.setText(title);
        TextView tvAgreement = findViewById(R.id.tv_agreement);
        String txtId = getIntent().getStringExtra("txtId");
        tvAgreement.setText(txtId);
    }
}
