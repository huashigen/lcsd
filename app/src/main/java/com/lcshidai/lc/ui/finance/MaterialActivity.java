package com.lcshidai.lc.ui.finance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.fragment.finance.MaterialFragment;

/**
 * 点击看大图
 */
public class MaterialActivity extends TRJActivity {

    private String mPrjId;
    private int mIndex = 0;
    private boolean isFirst = true;
    private ImageView mBack;
    MaterialFragment mmeterialFragment = new MaterialFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mPrjId = args.getString("prj_id");
            mIndex = args.getInt("index");
        }
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    public void initView() {
        setContentView(R.layout.activity_material);
        mBack = (ImageView) findViewById(R.id.material_back);
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MaterialActivity.this.finish();
            }
        });
        switchFragment(mIndex);
    }

    public void switchFragment(int index) {
        try {
            Bundle args = new Bundle();
            args.putString("prj_id", mPrjId);
            Fragment fragment = null;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragment = mmeterialFragment;
            args.putInt("index", index);
//			fragmentTransaction.setCustomAnimations(R.anim.hold, R.anim.hold);
            fragmentTransaction.setCustomAnimations(R.anim.hold, R.anim.left_out, R.anim.hold,
                    R.anim.hold);
            fragment.setArguments(args);
            if (isFirst) {
                fragmentTransaction.add(R.id.fl_material, fragment);
                isFirst = false;
            } else {
                fragmentTransaction.replace(R.id.fl_material, fragment);
            }
            fragmentTransaction.commit();
        } catch (Exception e) {
        }
    }

}
