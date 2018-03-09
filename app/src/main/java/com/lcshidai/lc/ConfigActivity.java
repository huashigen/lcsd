package com.lcshidai.lc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.ui.LoadingActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 环境配置页面
 * //   运维环境
 * //	public static final String BASE_API_HEAD =  "https://jrtest.tourongjia.com/";
 * //	public static final String BASE_WAP_HEAD =  "https://jrmtest.tourongjia.com/";
 * //   测试
 * //   public static final String BASE_API_HEAD = "https://escrow.tourongjia.com/";
 * //   public static final String BASE_WAP_HEAD = "https://wapescrow.tourongjia.com/";
 * //   生产环境
 * //   public static String BASE_API_HEAD = "https://www.tourongjia.com/";
 * //   public static String BASE_WAP_HEAD = "https://m.tourongjia.com/";
 */
public class ConfigActivity extends TRJActivity {

    @Bind(R.id.tv_current_version)
    TextView tvCurrentVersion;
    @Bind(R.id.tv_env_choose_label)
    TextView tvEnvChooseLabel;
    @Bind(R.id.btn_start)
    Button btnStart;
    @Bind(R.id.btn_clear_sp)
    Button btnClearSp;
    @Bind(R.id.rb_test)
    RadioButton rbTest;
    @Bind(R.id.rb_yw)
    RadioButton rbYw;
    @Bind(R.id.rg_env)
    RadioGroup rgEnv;
    @Bind(R.id.rb_zs)
    RadioButton rbZs;
    @Bind(R.id.tv_env_show_label)
    TextView tvEnvShowLabel;
    @Bind(R.id.tv_show_label_api)
    TextView tvShowLabelApi;
    @Bind(R.id.tv_show_label_wap)
    TextView tvShowLabelWap;
    @Bind(R.id.ib_top_bar_back)
    ImageButton ibTopBarBack;
    @Bind(R.id.tv_top_bar_title)
    TextView tvTopBarTitle;
    @Bind(R.id.tv_top_bar_right_action)
    TextView tvTopBarRightAction;
    @Bind(R.id.rl_top_bar)
    RelativeLayout rlTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        ibTopBarBack.setVisibility(View.GONE);
        tvTopBarTitle.setText("环境配置");
        tvCurrentVersion.setText("版本名称:" + CommonUtil.getVersionName(mContext,
                mContext.getPackageName()));
        tvShowLabelApi.setText("Api地址：" + LCHttpClient.BASE_API_HEAD);
        tvShowLabelWap.setText("Wap地址：" + LCHttpClient.BASE_WAP_HEAD);
        rbTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    LCHttpClient.BASE_API_HEAD = "https://escrow.tourongjia.com/";
//                    LCHttpClient.BASE_WAP_HEAD = "https://wapescrow.tourongjia.com/";
//                    测试环境
//                    LCHttpClient.BASE_API_HEAD = "http://uatweb.lcshidai.com/";
//                    LCHttpClient.BASE_WAP_HEAD = "http://uatwap.lcshidai.com/";
//                    生产环境
                    LCHttpClient.BASE_API_HEAD = "http://www.lcshidai.com/";
                    LCHttpClient.BASE_WAP_HEAD = "http://m.lcshidai.com/";
                    tvShowLabelApi.setText("Api地址：" + LCHttpClient.BASE_API_HEAD);
                    tvShowLabelWap.setText("Wap地址：" + LCHttpClient.BASE_WAP_HEAD);
                    KLog.init(true);
                }
            }
        });

        rbYw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    LCHttpClient.BASE_API_HEAD = "https://jrtest.tourongjia.com/";
//                    LCHttpClient.BASE_WAP_HEAD = "https://jrmtest.tourongjia.com/";

                    LCHttpClient.BASE_API_HEAD = "http://uatweb.lcshidai.com/";
                    LCHttpClient.BASE_WAP_HEAD = "http://uatwap.lcshidai.com/";
                    tvShowLabelApi.setText("Api地址：" + LCHttpClient.BASE_API_HEAD);
                    tvShowLabelWap.setText("Wap地址：" + LCHttpClient.BASE_WAP_HEAD);
                    KLog.init(true);
                }
            }
        });

        rbZs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    LCHttpClient.BASE_API_HEAD = "https://www.tourongjia.com/";
//                    LCHttpClient.BASE_WAP_HEAD = "https://m.tourongjia.com/";

                    LCHttpClient.BASE_API_HEAD = "http://uatweb.lcshidai.com/";
                    LCHttpClient.BASE_WAP_HEAD = "http://uatwap.lcshidai.com/";
                    tvShowLabelApi.setText("Api地址：" + LCHttpClient.BASE_API_HEAD);
                    tvShowLabelWap.setText("Wap地址：" + LCHttpClient.BASE_WAP_HEAD);
                    KLog.init(true);
                }
            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, LoadingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnClearSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.clearAllSpFiles();
            }
        });
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }
}
