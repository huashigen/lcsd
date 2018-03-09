package com.lcshidai.lc.ui.account;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.ManageFinanceListInfoImpl;
import com.lcshidai.lc.utils.ListViewUtility;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.widget.ppwindow.ChooseTPopupWindow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/**
 * 理财记录详细信息
 *
 * @author
 */
public class ManageFinanceListInfoActivity extends ManageFinanceListInfoServiceActivity implements
        ManageFinanceListInfoImpl, ChooseTPopupWindow.ChooseListener {

    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;

    public int mCurrentP = 0;

    boolean isFromHome;
    ChooseTPopupWindow sppw;
    ImageView iv_top_content_flag, iv_biding0, iv_biding1, iv_biding2,
            iv_biding3, iv_biding00;

    // MyScrollView sv;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_account_invest_records_info);
        if (getIntent() != null) {
            isTransfer = getIntent().getBooleanExtra("isTransfer", false);// .getBoolean("isTransfer");
            isFromHome = getIntent().getBooleanExtra("is_from_home", false);
        }
        sppw = new ChooseTPopupWindow(this, this);
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        sppw.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                iv_top_content_flag.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.icon_indicator_down));
            }
        });
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageFinanceListInfoActivity.this.finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText(isTransfer ? "可变现项目" : "我的投资");
        mTvTitle.setText(isTransfer ? "变现管理" : "我的投资");
        if (isTransfer) {
            findViewById(R.id.rl_top0).setVisibility(View.GONE);
        } else {
            findViewById(R.id.rl_top0).setVisibility(View.VISIBLE);
        }
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        iv_top_content_flag = (ImageView) findViewById(R.id.iv_top_content_flag);
        iv_biding0 = (ImageView) findViewById(R.id.iv_biding0);
        iv_biding1 = (ImageView) findViewById(R.id.iv_biding1);
        iv_biding2 = (ImageView) findViewById(R.id.iv_biding2);
        iv_biding3 = (ImageView) findViewById(R.id.iv_biding3);
        iv_biding00 = (ImageView) findViewById(R.id.iv_biding00);

        // sv=(MyScrollView) findViewById(R.id.sc_bottom);

        if (isTransfer) {
            ((TextView) findViewById(R.id.rl_tv_left1)).setText("可变现");
            ((TextView) findViewById(R.id.rl_tv_left2)).setText("变现中");
            ((TextView) findViewById(R.id.rl_tv_left3)).setText("已变现");
            // ((TextView) findViewById(R.id.rl_tv_left4)).setText("已过期");
            findViewById(R.id.rl_top4).setVisibility(View.GONE);
            findViewById(R.id.rl_top_type).setVisibility(View.GONE);
        } else {
            findViewById(R.id.rl_top_type).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            iv_top_content_flag
                                    .setBackgroundDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.icon_indicator_up));
                            sppw.goAnim(findViewById(R.id.rl_top_type), 0, 0,
                                    mCurrentP);
                        }
                    });

        }

        if (isFromHome) {
            switchLL(1, false);
        }

    }

    public void loadData() {
        hmflis.gainManageFinanceListInfo(isTransfer, mCurrentP, "1");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MemorySave.MS.mIsGoFinanceRecord || MemorySave.MS.mIsFinanceInfoFinish) {
            // 投标成功去账户概括整个逻辑结束
            finish();
            return;
        }
        loadData();
    }

    @Override
    public void setItemCashView() {
        findViewById(R.id.rl_top1).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        switchLL(1, false);
                    }
                });
        findViewById(R.id.rl_top2).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        switchLL(2, false);
                    }
                });

        findViewById(R.id.rl_top3).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        switchLL(3, false);
                    }
                });

        ((TextView) findViewById(R.id.tv_biding_num1)).setText(jobj
                .getCan_cash());
        ((TextView) findViewById(R.id.tv_biding_num2)).setText(jobj
                .getCashing());
        ((TextView) findViewById(R.id.tv_biding_num3))
                .setText(jobj.getCashed());
    }

    @Override
    public void setItemView() {

        findViewById(R.id.rl_top0).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        switchLL(5, false);
                    }
                });
        findViewById(R.id.rl_top1).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        switchLL(1, false);
                    }
                });
        findViewById(R.id.rl_top2).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        switchLL(2, false);
                    }
                });

        findViewById(R.id.rl_top3).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        switchLL(3, false);
                    }
                });
        findViewById(R.id.rl_top4).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        switchLL(4, false);
                    }
                });
        ((TextView) findViewById(R.id.tv_biding_num0)).setText(jobj.getStatus_5());
        ((TextView) findViewById(R.id.tv_biding_num1)).setText(jobj.getStatus_1());
        ((TextView) findViewById(R.id.tv_biding_num2)).setText(jobj.getStatus_2());
        ((TextView) findViewById(R.id.tv_biding_num3)).setText(jobj.getStatus_3());
        ((TextView) findViewById(R.id.tv_biding_num4)).setText(jobj.getStatus_4());
    }

    boolean isFirst = true;

    void switchLL(int index, boolean isRe) {

        if (isRe || findViewById(R.id.ll_1).getVisibility() == View.VISIBLE) {
            findViewById(R.id.ll_1).setVisibility(View.GONE);
            findViewById(R.id.rl_top1).setVisibility(View.VISIBLE);
            findViewById(R.id.rl_top2).setVisibility(View.VISIBLE);
            findViewById(R.id.rl_top3).setVisibility(View.VISIBLE);
            if (!isTransfer) {
                findViewById(R.id.rl_top4).setVisibility(View.VISIBLE);
                findViewById(R.id.rl_top0).setVisibility(View.VISIBLE);
            }


            return;
        }
        switch (index) {
            case 1:
                findViewById(R.id.rl_top1).setVisibility(View.VISIBLE);
                break;
            case 2:
                findViewById(R.id.rl_top2).setVisibility(View.VISIBLE);
                break;
            case 3:
                findViewById(R.id.rl_top3).setVisibility(View.VISIBLE);
                break;
            case 4:
                findViewById(R.id.rl_top4).setVisibility(View.VISIBLE);
                break;
            case 5:
                findViewById(R.id.rl_top0).setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        Intent intents = new Intent();
        intents.setClass(mContext, ManageFinanceListDetailsActivity.class);
        intents.putExtra("index", index);
        intents.putExtra("isRe", isRe);
        intents.putExtra("mCurrentP", mCurrentP);
        intents.putExtra("isTransfer", isTransfer);
        intents.putExtra("jobj", jobj);
        startActivity(intents);
    }

    boolean isSet = false;

    public void callBack(int code, ListView listView) {
        if (!isSet) {
            isSet = true;
            new ListViewUtility().setListViewHeightBasedOnChildren(listView);
        }
    }

    @Override
    public void chooseItem(int markItem) {
        mCurrentP = markItem;
        TextView topContentTV = (TextView) findViewById(R.id.tv_top_content);
        findViewById(R.id.rl_top0).setVisibility(View.VISIBLE);
        switch (markItem) {
            case 0:
                topContentTV.setText("所有投标产品");
                break;
            case 1:
                topContentTV.setText("短期宝");
                break;
            case 2:
                topContentTV.setText("长期宝");
                break;
            case 3:
                topContentTV.setText("超益宝");
                break;
            case 4:
                topContentTV.setText("速转让");
                findViewById(R.id.rl_top0).setVisibility(View.GONE);
                break;
            default:
                break;
        }
        loadData();
    }
}
