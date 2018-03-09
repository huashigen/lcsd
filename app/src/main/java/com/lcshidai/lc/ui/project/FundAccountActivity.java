package com.lcshidai.lc.ui.project;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.GetLiCaiUcInfoImpl;
import com.lcshidai.lc.impl.licai.AccessTokenImpl;
import com.lcshidai.lc.impl.licai.FundLoginImpl;
import com.lcshidai.lc.impl.licai.GetFundAccountInfoImpl;
import com.lcshidai.lc.impl.licai.ModifyAreaImpl;
import com.lcshidai.lc.impl.licai.ModifyAuditImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.LiCaiUcInfoJson;
import com.lcshidai.lc.model.account.LicaiUcInfoData;
import com.lcshidai.lc.model.licai.AccessTokenJson;
import com.lcshidai.lc.model.licai.ClsAuditListBean;
import com.lcshidai.lc.model.licai.FundAccountData;
import com.lcshidai.lc.model.licai.FundAccountInfo;
import com.lcshidai.lc.model.licai.FundAccountInfoJson;
import com.lcshidai.lc.model.licai.FundLoginData;
import com.lcshidai.lc.model.licai.FundLoginInfo;
import com.lcshidai.lc.model.licai.FundLoginJson;
import com.lcshidai.lc.model.licai.ProvinceCityModel;
import com.lcshidai.lc.service.account.HttpGetLiCaiUcInfoService;
import com.lcshidai.lc.service.licai.HttpFundLoginService;
import com.lcshidai.lc.service.licai.HttpGetAccessTokenService;
import com.lcshidai.lc.service.licai.HttpGetFundAccountInfoService;
import com.lcshidai.lc.service.licai.HttpModifyAreaService;
import com.lcshidai.lc.service.licai.HttpModifyAuditService;
import com.lcshidai.lc.ui.adapter.base.ListViewDataAdapter;
import com.lcshidai.lc.ui.adapter.base.ViewHolderBase;
import com.lcshidai.lc.ui.adapter.base.ViewHolderCreator;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.wheelview.OnWheelChangedListener;
import com.lcshidai.lc.widget.wheelview.WheelView;
import com.lcshidai.lc.widget.wheelview.adapters.ArrayWheelAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 基金账户 Activity
 */
public class FundAccountActivity extends TRJActivity implements View.OnClickListener, GetLiCaiUcInfoImpl,
        AccessTokenImpl, FundLoginImpl, ModifyAuditImpl, GetFundAccountInfoImpl, ModifyAreaImpl {
    String access_token;

    HttpGetLiCaiUcInfoService getLiCaiUcInfoService = null;
    HttpFundLoginService fundLoginService = null;
    HttpModifyAuditService modifyAuditService = null;
    HttpModifyAreaService modifyAreaService = null;
    HttpGetFundAccountInfoService getFundAccountInfoService = null;
    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    private HttpGetAccessTokenService getAccessTokenService;
    @Bind(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @Bind(R.id.tv_total_preview_amount)
    TextView tvTotalPreviewAmount;
    @Bind(R.id.ll_total_preview_container)
    LinearLayout llTotalPreviewContainer;
    @Bind(R.id.tv_total_deal_amount)
    TextView tvTotalDealAmount;
    @Bind(R.id.ll_total_deal_container)
    LinearLayout llTotalDealContainer;
    @Bind(R.id.tv_financial_planner_label)
    TextView tvFinancialPlannerLabel;
    @Bind(R.id.tv_financial_planner)
    TextView tvFinancialPlanner;
    @Bind(R.id.rl_my_special_financial_planner_container)
    RelativeLayout rlMySpecialFinancialPlannerContainer;
    @Bind(R.id.tv_property_config)
    TextView tvPropertyConfig;
    @Bind(R.id.tv_unit)
    TextView tvUnit;
    @Bind(R.id.rl_property_config_container)
    RelativeLayout rlPropertyConfigContainer;
    @Bind(R.id.tv_my_appointment_label)
    TextView tvMyAppointmentLabel;
    @Bind(R.id.rl_my_appointment_container)
    RelativeLayout rlMyAppointmentContainer;
    @Bind(R.id.tv_qualified_investor_label)
    TextView tvQualifiedInvestorLabel;
    @Bind(R.id.rl_qualified_investor_container)
    RelativeLayout rlQualifiedInvestorContainer;
    @Bind((R.id.tv_qualified_state))
    TextView tvQualifiedState;
    @Bind(R.id.tv_risk_test_label)
    TextView tvRiskTestLabel;
    @Bind(R.id.tv_risk_test_result)
    TextView tvRiskTestResult;
    @Bind(R.id.tv_risk_deadline)
    TextView tvRiskDeadline;
    @Bind(R.id.rl_risk_test_container)
    RelativeLayout rlRiskTestContainer;
    private FundLoginInfo fundLoginInfo;
    private FundAccountInfo fundAccountInfo;
    private Dialog cfpDialog, qualifiedInvestorDialog, cityChooserDialog;
    private int invest_level;
    private String lcsAuditStr;

    private List<ProvinceCityModel> provinceCityModelList = new ArrayList<>();
    private String[] provinces;
    private int mCurrentProvinceIndex, mCurrentAreaIndex;

    /**
     * 所选省名
     */
    protected String mCurrentProvinceName;
    /**
     * 所选市名
     */
    protected String mCurrentCityName;

    private boolean isDataFromLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_account);
        ButterKnife.bind(this);

        getLiCaiUcInfoService = new HttpGetLiCaiUcInfoService(this, this);
        fundLoginService = new HttpFundLoginService(this, this);
        modifyAuditService = new HttpModifyAuditService(this, this);
        modifyAreaService = new HttpModifyAreaService(this, this);
        getFundAccountInfoService = new HttpGetFundAccountInfoService(this, this);

        initViewsAndEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isDataFromLogin) {
            getFundAccountInfoService.getFundAccountInfo(GoLoginUtil.getAccessToken(this), GoLoginUtil.getUserToken(this));
            showLoadingDialog(this, "正在更新", true);
        }
    }

    private void initViewsAndEvent() {
        access_token = getIntent().getStringExtra(Constants.ACCESS_TOKEN_KEY);
        rlQualifiedInvestorContainer.setOnClickListener(this);
        rlRiskTestContainer.setOnClickListener(this);
        rlMySpecialFinancialPlannerContainer.setOnClickListener(this);
        rlPropertyConfigContainer.setOnClickListener(this);
        rlMyAppointmentContainer.setOnClickListener(this);
        topBackBtn.setOnClickListener(this);

        topTitleText.setText("基金账户");
        if (!CommonUtil.isNullOrEmpty(access_token)) {
            getLiCaiUcInfoService.getLiCaiUcInfo(GoLoginUtil.getUcId(this));
            showLoadingDialog(this, "正在加载", true);
        } else {
            getAccessTokenService = new HttpGetAccessTokenService(this, this);
            getAccessTokenService.getAccessToken("android", CommonUtil.getDeviceId(mContext));
            showLoadingDialog(this, "正在加载", true);
        }
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return true;
    }

    @Override
    public void onBackPressed() {
        ToastUtil.cancelToast();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;
            case R.id.rl_my_special_financial_planner_container:
                // 弹出窗口
                if (null == cfpDialog) {
                    initCfpDialog();
                    cfpDialog.show();
                } else {
                    cfpDialog.show();
                }
                break;
            case R.id.rl_property_config_container:
                intent = new Intent(mContext, PropertyConfigActivity.class);
                intent.putExtra(Constants.FUND_LOGIN_INFO, fundLoginInfo);
                startActivity(intent);
                break;
            case R.id.rl_my_appointment_container:
                intent = new Intent(mContext, MyInvestAppointmentListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_risk_test_container:
                if (invest_level == 0) {
                    intent = new Intent(mContext, RiskTestActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.showShortToast(mContext, "您已经完成了风险测试");
                }
                break;
            case R.id.rl_qualified_investor_container:
                if (!CommonUtil.isNullOrEmpty(lcsAuditStr)) {
                    if (!(lcsAuditStr.equals("1") || lcsAuditStr.equals("2") || lcsAuditStr.equals("3"))) {
                        initQualifiedInvestorDialog(fundLoginInfo.getCls_audit_list());
                    }
                } else {
                    tvQualifiedState.setText("已认证");
                }
                break;
        }
    }

    /**
     * 初始化联系我的理财师弹窗
     */
    private void initCfpDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_contact_cfp, null);
        ImageView ivImgCfp = (ImageView) view.findViewById(R.id.iv_img_cfp);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        TextView tvNotContactNow = (TextView) view.findViewById(R.id.tv_not_contact_now);
        TextView tvContactNow = (TextView) view.findViewById(R.id.tv_contact_now);
        int width = (int) (DensityUtil.getScreenWidth(mContext) * 0.8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width / 2);
        ivImgCfp.setLayoutParams(params);

        tvContent.setText(String.format("您的专属理财师：%s", GoLoginUtil.getManagerName((TRJActivity) mContext)));

        tvNotContactNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfpDialog.dismiss();
            }
        });

        tvContactNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +
                        GoLoginUtil.getManagerPhone((TRJActivity) mContext)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                cfpDialog.dismiss();
            }
        });

        cfpDialog = new Dialog(this, R.style.style_loading_dialog);
        cfpDialog.setContentView(view);
        cfpDialog.setCancelable(true);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = cfpDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
        cfpDialog.getWindow().setAttributes(lp);
    }

    int selectPos = 0;

    /**
     * 初始化合格投资者认证窗口
     */
    private void initQualifiedInvestorDialog(List<ClsAuditListBean> clsAuditListBeanList) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_qulified_investor, null);
        ListView lvQualifiedCondition = (ListView) view.findViewById(R.id.lv_qualified_condition);
        final TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvConfirm.setClickable(false);
        final ListViewDataAdapter<ClsAuditListBean> adapter = new ListViewDataAdapter<>(new ViewHolderCreator<ClsAuditListBean>() {
            @Override
            public ViewHolderBase<ClsAuditListBean> createViewHolder(int position) {
                return new ViewHolderBase<ClsAuditListBean>() {
                    ImageView ivConditionImg;
                    TextView tvConditionText;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View convertView = layoutInflater.inflate(R.layout.qualified_condition_list_item, null);
                        ivConditionImg = (ImageView) convertView.findViewById(R.id.iv_qualified_condition_img);
                        tvConditionText = (TextView) convertView.findViewById(R.id.tv_qualified_condition_text);
                        return convertView;
                    }

                    @Override
                    public void showData(int position, ClsAuditListBean itemData) {
                        if (null != itemData) {
                            tvConditionText.setText(itemData.getLcs_audit_str());
                            if (position == selectPos) {
                                ivConditionImg.setImageResource(R.drawable.icon_qualified_checked);
                            } else {
                                ivConditionImg.setImageResource(R.drawable.icon_qualified_normal);
                            }
                        }
                    }
                };
            }
        });
        adapter.getDataList().addAll(clsAuditListBeanList);
        lvQualifiedCondition.setAdapter(adapter);

        lvQualifiedCondition.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPos = position;
                if (selectPos != -1) {
                    tvConfirm.setClickable(true);
                    tvConfirm.setTextColor(getResources().getColor(R.color.white));
                    tvConfirm.setBackgroundResource(R.drawable.shape_account_recharge);
                } else {
                    tvConfirm.setClickable(false);
                    tvConfirm.setTextColor(getResources().getColor(R.color.theme_color));
                    tvConfirm.setBackgroundResource(R.drawable.shape_account_cashout);
                }
                adapter.notifyDataSetChanged();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualifiedInvestorDialog.dismiss();
                String access_token = GoLoginUtil.getAccessToken((TRJActivity) mContext);
                String user_token = GoLoginUtil.getUserToken((TRJActivity) mContext);
                modifyAuditService.modifyAudit(access_token, user_token, adapter.getDataList().get(selectPos).getLcs_audi());
            }
        });
        qualifiedInvestorDialog = new Dialog(this, R.style.style_loading_dialog);
        qualifiedInvestorDialog.setContentView(view);
        qualifiedInvestorDialog.setCancelable(false);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = qualifiedInvestorDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.9); //设置宽度
        qualifiedInvestorDialog.getWindow().setAttributes(lp);
    }

    /**
     * 初始化城市选择对话框
     */
    private void initCityChooseDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.dialog_city_chooser, null);
        final WheelView wvProvince = (WheelView) view.findViewById(R.id.id_province);
        final WheelView wvCity = (WheelView) view.findViewById(R.id.id_city);
        wvProvince.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCities(wvProvince, wvCity);
            }
        });
        wvCity.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

            }
        });
        // 初始化数据
        initProvinceCityData();
        wvProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, provinces));
        wvProvince.setCurrentItem(0);
        wvProvince.setVisibleItems(3);
        updateCities(wvProvince, wvCity);
        wvCity.setVisibleItems(3);
        final TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityChooserDialog.dismiss();
                mCurrentProvinceIndex = wvProvince.getCurrentItem();
                mCurrentAreaIndex = wvCity.getCurrentItem();
                // modify the area info
                mCurrentProvinceName = provinceCityModelList.get(mCurrentProvinceIndex).getProvince();
                mCurrentCityName = provinceCityModelList.get(mCurrentProvinceIndex).getArea_list()[mCurrentAreaIndex];
                modifyAreaService.modifyArea(GoLoginUtil.getAccessToken((TRJActivity) mContext),
                        GoLoginUtil.getUserToken((TRJActivity) mContext), mCurrentProvinceName, mCurrentCityName);
            }
        });
        cityChooserDialog = new Dialog(mContext, R.style.style_loading_dialog);
        cityChooserDialog.setContentView(view);
        cityChooserDialog.setCancelable(false);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = cityChooserDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.85); //设置宽度
        cityChooserDialog.getWindow().setAttributes(lp);
    }

    /**
     * 初始化省市数据
     */
    private void initProvinceCityData() {
        Gson gson = new Gson();
        provinceCityModelList = gson.fromJson(getResources().getString(R.string.province_city),
                new TypeToken<List<ProvinceCityModel>>() {
                }.getType());
        provinces = new String[provinceCityModelList.size()];
        for (int i = 0; i < provinceCityModelList.size(); i++) {
            provinces[i] = provinceCityModelList.get(i).getProvince();
        }
    }

    private void updateCities(WheelView wvProvince, WheelView wvCity) {
        mCurrentProvinceIndex = wvProvince.getCurrentItem();
        if (provinceCityModelList != null && provinceCityModelList.size() > 0) {
            mCurrentProvinceName = provinceCityModelList.get(mCurrentProvinceIndex).getProvince();
            String[] areas = provinceCityModelList.get(mCurrentProvinceIndex).getArea_list();

            wvCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areas));
            wvCity.setCurrentItem(0);
        }
    }

    @Override
    public void getLiCaiUcInfoSuccess(LiCaiUcInfoJson response) {
        hideLoadingDialog();
        if (null != response) {
            LicaiUcInfoData data = response.getData();
            if (null != data) {
                String username;
                if (!CommonUtil.isNullOrEmpty(MemorySave.MS.userInfo.real_name)) {
                    username = MemorySave.MS.userInfo.real_name;
                } else {
                    username = MemorySave.MS.userInfo.uname;
                }
                fundLoginService.fundLogin(GoLoginUtil.getAccessToken(this), data.getToken(), username,
                        MemorySave.MS.userInfo.mobile, MemorySave.MS.userInfo.person_id, "", data.getId(), data.getAuth(), "android");
            }
        }
    }

    @Override
    public void getLiCaiUcInfoFailed() {
        hideLoadingDialog();
    }

    @Override
    public void fundLoginSuccess(FundLoginJson response) {
        if (null != response) {
            isDataFromLogin = false;
            FundLoginData data = response.getData();
            fundLoginInfo = data.getInfo();
            if (null != fundLoginInfo) {
                lcsAuditStr = fundLoginInfo.getLcs_audit();
                if (CommonUtil.isNullOrEmpty(lcsAuditStr)) {
                    initQualifiedInvestorDialog(fundLoginInfo.getCls_audit_list());
                    qualifiedInvestorDialog.show();
                } else {
                    if (!(lcsAuditStr.equals("1") || lcsAuditStr.equals("2") || lcsAuditStr.equals("3"))) {
                        initQualifiedInvestorDialog(fundLoginInfo.getCls_audit_list());
                        qualifiedInvestorDialog.show();
                    } else {
                        tvQualifiedState.setText("已认证");
                    }
                    if (CommonUtil.isNullOrEmpty(fundLoginInfo.getProvince()) || CommonUtil.isNullOrEmpty(fundLoginInfo.getCity())) {
                        if (cityChooserDialog != null) {
                            cityChooserDialog.show();
                        } else {
                            initCityChooseDialog();
                            cityChooserDialog.show();
                        }
                    }
                }
                GoLoginUtil.saveUserToken(data.getUser_token(), this);
                getFundAccountInfoService.getFundAccountInfo(GoLoginUtil.getAccessToken(this), GoLoginUtil.getUserToken(this));
            }
        }
    }

    @Override
    public void fundLoginFailed(FundLoginJson errorResponse) {
        if (null != errorResponse) {
            if (!CommonUtil.isNullOrEmpty(errorResponse.getMessage())) {
                ToastUtil.showLongToast(mContext, errorResponse.getMessage());
            }
        }
    }

    private void initViewsWithData(FundAccountInfo fundAccountInfo) {
        tvTotalPreviewAmount.setText(fundAccountInfo.getAmount());
        tvTotalDealAmount.setText(fundAccountInfo.getAmount_done());
        tvFinancialPlanner.setText(fundAccountInfo.getManager());
//        0：未鉴定，1：保守型，2：稳健性，3：进取型
        if (!CommonUtil.isNullOrEmpty(fundAccountInfo.getInvest_level())) {
            invest_level = Integer.valueOf(fundAccountInfo.getInvest_level());
            if (!CommonUtil.isNullOrEmpty(fundAccountInfo.getInvest_level_expire_date())) {
                tvRiskDeadline.setText(String.format("%s到期", fundAccountInfo.getInvest_level_expire_date()));
            }
            switch (invest_level) {
                case 0:
                    tvRiskTestResult.setText("未鉴定");
                    break;
                case 1:
                    tvRiskTestResult.setText("保守型");
                    break;
                case 2:
                    tvRiskTestResult.setText("稳健型");
                    break;
                case 3:
                    tvRiskTestResult.setText("进取型");
                    break;
            }
        } else {
            tvRiskTestResult.setText("");
            tvRiskDeadline.setVisibility(View.GONE);
        }
    }

    @Override
    public void getAccessTokenSuccess(AccessTokenJson response) {
        hideLoadingDialog();
        if (null != response) {
            String access_token = response.getData().getAccess_token();
            GoLoginUtil.saveAccessToken(access_token, this);
            getLiCaiUcInfoService.getLiCaiUcInfo(GoLoginUtil.getUcId(this));
            Log.e("getAccessTokenSuccess", "获取AccessToken成功\n" + "Message=" + response.getMessage() + "\nStatus=" + response.getStatus());
        }
    }

    @Override
    public void getAccessTokenFailed() {
        hideLoadingDialog();
        Log.e("getAccessTokenFailed", "获取理财AccessToken失败");
    }

    @Override
    public void modifyAuditSuccess(BaseJson response) {
        if (null != response) {
            if (!CommonUtil.isNullOrEmpty(response.getMessage())) {
                if (cityChooserDialog != null) {
                    cityChooserDialog.show();
                } else {
                    initCityChooseDialog();
                    cityChooserDialog.show();
                }
            }
        }
    }

    @Override
    public void modifyAuditFailed(BaseJson errorResponse) {
        if (null != errorResponse) {
            if (!CommonUtil.isNullOrEmpty(errorResponse.getMessage())) {
                ToastUtil.showLongToast(mContext, errorResponse.getMessage());
            }
        }
    }

    @Override
    public void modifyAreaSuccess(BaseJson response) {
        if (null != response) {
            if (!CommonUtil.isNullOrEmpty(response.getMessage())) {
                Log.e("modifyAreaSuccess", response.getMessage());
            }
            getFundAccountInfoService.getFundAccountInfo(GoLoginUtil.getAccessToken(this), GoLoginUtil.getUserToken(this));
        }
    }

    @Override
    public void modifyAreaFailed(BaseJson errorResponse) {
        if (null != errorResponse) {
            if (!CommonUtil.isNullOrEmpty(errorResponse.getMessage())) {
                Log.e("modifyAreaFailed", errorResponse.getMessage());
            }
        }
    }

    @Override
    public void getFundAccountInfoSuccess(FundAccountInfoJson response) {
        hideLoadingDialog();
        if (null != response) {
            FundAccountData data = response.getData();
            if (null != data) {
                fundAccountInfo = data.getInfo();
                initViewsWithData(fundAccountInfo);
                if (null != data.getInfo()) {
                    GoLoginUtil.saveManagerPhone(data.getInfo().getManager_phone(), this);
                    GoLoginUtil.saveManagerName(data.getInfo().getManager(), this);
                }
            }
        }
    }

    @Override
    public void getFundAccountInfoFailed(FundAccountInfoJson errorResponse) {
        hideLoadingDialog();
    }
}
