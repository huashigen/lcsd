package com.lcshidai.lc.ui.finance;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.AbsSubActivity;

/**
 * 首页
 *
 * @author
 */
public class ManageFinaceInfoCountActivity extends AbsSubActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_finance_info_count);

        findViewById(R.id.finance_info_count_rl).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

}
