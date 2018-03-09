package com.lcshidai.lc.ui.finance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.fragment.finance.FinanceTabItemFragment;

/**
 * Created by a on 2016/5/31.
 */
public class FinanceFilterResultActivity extends TRJActivity implements View.OnClickListener{
    private TextView tv_title;
    private TextView tv_cancel;
    private LinearLayout ll_none;
    private TextView tv_none;
    private FinanceTabItemFragment mFinanceTabItemFragment;

    private NoDataReceiver mNoDataReceiver;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_finance_filter_result);
        String search_param = getIntent().getStringExtra("param");
        String search_name = getIntent().getStringExtra("name");

        tv_title = (TextView) findViewById(R.id.tv_top_bar_title);
        tv_title.setText(search_name);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);

        ll_none = (LinearLayout) findViewById(R.id.ll_none);
        tv_none = (TextView) findViewById(R.id.tv_none);

        mFinanceTabItemFragment = new FinanceTabItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("search_param", search_param);
        mFinanceTabItemFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_view, mFinanceTabItemFragment);
        fragmentTransaction.commit();

        mNoDataReceiver = new NoDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastImpl.ACTION_FINANCE_SEARCH_NO_DATA);
        registerReceiver(mNoDataReceiver, filter);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNoDataReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        setResult(RESULT_CANCELED);
    }

    private class NoDataReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ll_none.setVisibility(View.VISIBLE);
            String none = "没有找到" + "\"" + tv_title.getText() + "\"" + "相关结果";
            SpannableStringBuilder builder = new SpannableStringBuilder(none);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_my_green));
            builder.setSpan(colorSpan, 4, none.length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_none.setText(builder);
        }
    }

}
