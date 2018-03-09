package com.lcshidai.lc.ui.finance;

import android.os.Bundle;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.fragment.finance.FinanceTabItemFragment;

public class ManageFinanceXXDSActivity extends TRJActivity {

    FinanceTabItemFragment itemFragment = new FinanceTabItemFragment();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_manage_finance_xxds);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.view_fragment) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.

            // Create an instance of ExampleFragment
            itemFragment.isVinvest = true;
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            itemFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_fragment, itemFragment).commit();
        }
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

}
