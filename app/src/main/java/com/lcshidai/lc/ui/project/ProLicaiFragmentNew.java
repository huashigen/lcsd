package com.lcshidai.lc.ui.project;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.GetLiCaiUcInfoImpl;
import com.lcshidai.lc.impl.finance.LcsUserAlerImpl;
import com.lcshidai.lc.impl.licai.AccessTokenImpl;
import com.lcshidai.lc.impl.licai.FundLoginImpl;
import com.lcshidai.lc.impl.licai.GetFundTypeListImpl;
import com.lcshidai.lc.impl.licai.ModifyAreaImpl;
import com.lcshidai.lc.impl.licai.ModifyAuditImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.LiCaiUcInfoJson;
import com.lcshidai.lc.model.account.LicaiUcInfoData;
import com.lcshidai.lc.model.finance.lcs.LcsUserAlerData;
import com.lcshidai.lc.model.finance.lcs.LcsUserAlerJson;
import com.lcshidai.lc.model.licai.AccessTokenJson;
import com.lcshidai.lc.model.licai.ClsAuditListBean;
import com.lcshidai.lc.model.licai.FundLoginData;
import com.lcshidai.lc.model.licai.FundLoginJson;
import com.lcshidai.lc.model.licai.FundTypeListJson;
import com.lcshidai.lc.model.licai.ProvinceCityModel;
import com.lcshidai.lc.service.account.HttpGetLiCaiUcInfoService;
import com.lcshidai.lc.service.finance.HttpLcsUserAlertService;
import com.lcshidai.lc.service.licai.HttpFundLoginService;
import com.lcshidai.lc.service.licai.HttpGetAccessTokenService;
import com.lcshidai.lc.service.licai.HttpGetFundTypeListService;
import com.lcshidai.lc.service.licai.HttpModifyAreaService;
import com.lcshidai.lc.service.licai.HttpModifyAuditService;
import com.lcshidai.lc.ui.adapter.base.ListViewDataAdapter;
import com.lcshidai.lc.ui.adapter.base.ViewHolderBase;
import com.lcshidai.lc.ui.adapter.base.ViewHolderCreator;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.wheelview.OnWheelChangedListener;
import com.lcshidai.lc.widget.wheelview.WheelView;
import com.lcshidai.lc.widget.wheelview.adapters.ArrayWheelAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allin on 2016/7/18.
 */
public class ProLicaiFragmentNew extends TRJFragment implements LcsUserAlerImpl, AccessTokenImpl,
        GetLiCaiUcInfoImpl, FundLoginImpl, GetFundTypeListImpl, ModifyAuditImpl, ModifyAreaImpl,
        ViewPager.OnPageChangeListener {
    private HttpLcsUserAlertService mHttpLcsUserAlertService;
    private HttpGetAccessTokenService getAccessTokenService;
    private HttpGetLiCaiUcInfoService getLiCaiUcInfoService;
    private HttpFundLoginService fundLoginService;
    private HttpGetFundTypeListService getFundTypeListService;
    private HttpModifyAuditService modifyAuditService = null;
    private HttpModifyAreaService modifyAreaService = null;

    private int mResultCode = 0;

    private FrameLayout bannerContainer;
    //    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private MagicIndicator magicIndicator;
    private ImageView ivMask;
    private ViewPager viewPager;

    private LicaiBannerFragment mBannerFragment = new LicaiBannerFragment();
    private List<String> titles = new ArrayList<String>();
    private List<String> titlesTextList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private Dialog qualifiedInvestorDialog;
    private Dialog cityChooserDialog;
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


    public ProLicaiFragmentNew() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_licai_new, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHttpLcsUserAlertService = new HttpLcsUserAlertService((TRJActivity) getActivity(), this);
        getAccessTokenService = new HttpGetAccessTokenService((TRJActivity) getActivity(), this);
        getLiCaiUcInfoService = new HttpGetLiCaiUcInfoService((TRJActivity) getActivity(), this);
        fundLoginService = new HttpFundLoginService((TRJActivity) getActivity(), this);
        getFundTypeListService = new HttpGetFundTypeListService((TRJActivity) getActivity(), this);
        modifyAuditService = new HttpModifyAuditService((TRJActivity) getActivity(), this);
        modifyAreaService = new HttpModifyAreaService((TRJActivity) getActivity(), this);
        initView(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isHidden()) {
            mResultCode = 0;
            return;
        }
        if (mResultCode == 0) {
            //  used  for test login
            mHttpLcsUserAlertService.getUserAlert();
        } else {
            mResultCode = 0;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mResultCode == 0) {
                mHttpLcsUserAlertService.getUserAlert();
            }
        }
    }

    private void initView(View view, Bundle savedInstanceState) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (mBannerFragment.isAdded()) {
            fm.putFragment(savedInstanceState, "Licaibanner", mBannerFragment);
        } else {
            fragmentTransaction.add(R.id.fl_banner, mBannerFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
        bannerContainer = (FrameLayout) view.findViewById(R.id.fl_banner);
        magicIndicator = (MagicIndicator) view.findViewById(R.id.pagerSlidingTabStrip);
        ivMask = (ImageView) view.findViewById(R.id.iv_mask);
//        pagerSlidingTabStrip.setTextColor(Color.parseColor("#848382"));
//        pagerSlidingTabStrip.setTabTextSelectedColor(getResources().getColor(R.color.theme_color));
//        pagerSlidingTabStrip.setTextSize(DensityUtil.sp2px(getActivity(), 10f));
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
    }

    int selectPos = 0;

    /**
     * 初始化合格投资者认证窗口
     */
    private void initQualifiedInvestorDialog(List<ClsAuditListBean> clsAuditListBeanList) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
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
                    tvConfirm.setTextColor(getResources().getColor(R.color.white));
                    tvConfirm.setBackgroundResource(R.drawable.shape_account_recharge);
                } else {
                    tvConfirm.setTextColor(getResources().getColor(R.color.theme_color));
                    tvConfirm.setBackgroundResource(R.drawable.shape_account_cashout);
                }
                adapter.notifyDataSetChanged();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPos == -1) {
                    ToastUtil.showLongToast(mContext, "您尚未进行选择");
                } else {
                    qualifiedInvestorDialog.dismiss();
                    String access_token = GoLoginUtil.getAccessToken((TRJActivity) mContext);
                    String user_token = GoLoginUtil.getUserToken((TRJActivity) mContext);
                    modifyAuditService.modifyAudit(access_token, user_token, adapter.getDataList().get(selectPos).getLcs_audi());
                }
            }
        });
        qualifiedInvestorDialog = new Dialog(getActivity(), R.style.style_loading_dialog);
        qualifiedInvestorDialog.setContentView(view);
        qualifiedInvestorDialog.setCancelable(false);
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = qualifiedInvestorDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.9); //设置宽度
        qualifiedInvestorDialog.getWindow().setAttributes(lp);
    }

    /**
     * 初始化城市选择对话框
     */
    private void initCityChooseDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
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
        // 初始化数据// 初始化数据
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
        cityChooserDialog = new Dialog(getActivity(), R.style.style_loading_dialog);
        cityChooserDialog.setContentView(view);
        cityChooserDialog.setCancelable(false);
        WindowManager windowManager = getActivity().getWindowManager();
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewPager.setCurrentItem(position);
        if (position == fragmentList.size() - 1) {
            ivMask.setVisibility(View.GONE);
        } else {
            ivMask.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void getAccessTokenSuccess(AccessTokenJson response) {
        if (null != response) {
            String access_token = response.getData().getAccess_token();
            GoLoginUtil.saveAccessToken(access_token, getActivity());
            getLiCaiUcInfoService.getLiCaiUcInfo(GoLoginUtil.getUcId(getActivity()));
            KLog.e("getAccessTokenSuccess", "获取AccessToken成功\n" + "Message=" + response.getMessage() + "\nStatus=" + response.getStatus());
        }
    }

    @Override
    public void getLiCaiUcInfoSuccess(LiCaiUcInfoJson response) {
        if (null != response) {
            LicaiUcInfoData data = response.getData();
            if (null != data) {
                String username;
                if (!CommonUtil.isNullOrEmpty(MemorySave.MS.userInfo.real_name)) {
                    username = MemorySave.MS.userInfo.real_name;
                } else {
                    username = MemorySave.MS.userInfo.uname;
                }
                fundLoginService.fundLogin(GoLoginUtil.getAccessToken(getActivity()), data.getToken(), username,
                        MemorySave.MS.userInfo.mobile, MemorySave.MS.userInfo.person_id, "", data.getId(), data.getAuth(), "android");
                KLog.e("getLiCaiUcInfoSuccess", "获取理财用户中心数据成功\n" + "Message=" + response.getMessage() + "\nStatus=" + response.getStatus());
                KLog.e("UcInfoSuccess->data", data.toString());
            } else {
                KLog.e("UcInfoSuccess->data", "data is null");

            }

        }
    }

    @Override
    public void getLiCaiUcInfoFailed() {
        KLog.e("getLiCaiUcInfoFailed", "获取理财UcInfo失败");
    }

    @Override
    public void getAccessTokenFailed() {
        KLog.e("getAccessTokenFailed", "获取理财AccessToken失败");
    }

    @Override
    public void fundLoginSuccess(FundLoginJson response) {
        if (null != response) {
            FundLoginData data = response.getData();
            if (null != data) {
                // 初始化列表的操作必须放在登录成功之后进行
                GoLoginUtil.saveUserToken(data.getUser_token(), getActivity());
                if (null != data.getInfo()) {
                    GoLoginUtil.saveManagerPhone(data.getInfo().getManager_phone(), getActivity());
                    GoLoginUtil.saveManagerName(data.getInfo().getManager(), getActivity());
                    lcsAuditStr = data.getInfo().getLcs_audit();
                    if (CommonUtil.isNullOrEmpty(lcsAuditStr)) {
                        initQualifiedInvestorDialog(data.getInfo().getCls_audit_list());
                        qualifiedInvestorDialog.show();
                    } else {
                        if (!(lcsAuditStr.equals("1") || lcsAuditStr.equals("2") || lcsAuditStr.equals("3"))) {
                            initQualifiedInvestorDialog(data.getInfo().getCls_audit_list());
                            qualifiedInvestorDialog.show();
                        }
                        if (CommonUtil.isNullOrEmpty(data.getInfo().getProvince()) || CommonUtil.isNullOrEmpty(data.getInfo().getCity())) {
                            if (cityChooserDialog != null) {
                                cityChooserDialog.show();
                            } else {
                                initCityChooseDialog();
                                cityChooserDialog.show();
                            }
                        }
                    }
                }
                getFundTypeListService.getFundTypeList(GoLoginUtil.getAccessToken(getActivity()), data.getUser_token());
            }
            KLog.e("fundLoginSuccess", "Message=" + response.getMessage() + "\nStatus=" + response.getStatus());
        }
    }

    @Override
    public void fundLoginFailed(FundLoginJson errorResponse) {
        if (null != errorResponse) {
            KLog.e("fundLoginSuccess", "Message=" + errorResponse.getMessage());
        }
    }

    private void convertTitleIdToText(List<String> titles) {
        titlesTextList.clear();
        for (int i = 0; i < titles.size(); i++) {
            int type = Integer.valueOf(titles.get(i));
//                1：固收，2：证券基金，3：股权基金，4：信托产品，5：债权基金，6：资管计划
            switch (type) {
                case 1:
                    titlesTextList.add(i, "固收");
                    break;
                case 2:
                    titlesTextList.add(i, "证券基金");
                    break;
                case 3:
                    titlesTextList.add(i, "股权基金");
                    break;
                case 4:
                    titlesTextList.add(i, "信托产品");
                    break;
                case 5:
                    titlesTextList.add(i, "债权基金");
                    break;
                case 6:
                    titlesTextList.add(i, "资管计划");
                    break;
            }
        }
    }

    @Override
    public void getFundTypeListSuccess(FundTypeListJson response) {
        if (null != response) {
            titles = response.getData().getFundTypeList();
            convertTitleIdToText(titles);
            if (null != titles && titles.size() > 0) {
                for (int i = 0; i < titles.size(); i++) {
                    fragmentList.add(FundFragment.newInstance(titles.get(i)));
                }
                MyPagerAdapter adapter = new MyPagerAdapter(getFragmentManager());
                viewPager.setAdapter(adapter);
                viewPager.setOnPageChangeListener(this);
                final CommonNavigator commonNavigator = new CommonNavigator(getActivity());
                commonNavigator.setScrollPivotX(0.35f);
                commonNavigator.setAdapter(new CommonNavigatorAdapter() {
                    @Override
                    public int getCount() {
                        return titlesTextList == null ? 0 : titlesTextList.size();
                    }

                    @Override
                    public IPagerTitleView getTitleView(Context context, final int index) {
                        SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                        simplePagerTitleView.setText(titlesTextList.get(index));
                        simplePagerTitleView.setNormalColor(getActivity().getResources().getColor(R.color.black));
                        simplePagerTitleView.setSelectedColor(getActivity().getResources().getColor(R.color.theme_color));
                        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewPager.setCurrentItem(index);
                            }
                        });
                        return simplePagerTitleView;
                    }

                    @Override
                    public IPagerIndicator getIndicator(Context context) {
                        WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                        indicator.setFillColor(getActivity().getResources().getColor(R.color.theme_color));
                        return indicator;
                    }
                });
                commonNavigator.setOnScrollToTheEndListener(new CommonNavigator.OnScrollToTheEndListener() {
                    @Override
                    public void scrollToTheEnd(boolean isEnd) {
                        if (isEnd) {
                            ivMask.setVisibility(View.GONE);
                        } else {
                            ivMask.setVisibility(View.VISIBLE);
                        }
                    }
                });
                magicIndicator.setNavigator(commonNavigator);
                ViewPagerHelper.bind(magicIndicator, viewPager);
            }
        }
    }

    @Override
    public void getFundTypeListFailed(FundTypeListJson errorResponse) {

    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (null != fragmentList && fragmentList.size() > 0) {
                return fragmentList.get(position);
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            if (null != titles && titles.size() > 0) {
                return titles.size();
            } else {
                return 0;
            }
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            if (null != titles && titles.size() > 0) {
//                int type = Integer.valueOf(titles.get(position));
////                1：固收，2：证券基金，3：股权基金，4：信托产品，5：债权基金，6：资管计划
//                switch (type) {
//                    case 1:
//                        return "固收";
//                    case 2:
//                        return "证券基金";
//                    case 3:
//                        return "股权基金";
//                    case 4:
//                        return "信托产品";
//                    case 5:
//                        return "债权基金";
//                    case 6:
//                        return "资管计划";
//                    default:
//                        return "固收";
//                }
//            } else {
//                return "";
//            }
//        }

    }


    public void setFlag(int resultCode) {
        mResultCode = resultCode;
    }

    @Override
    public void gainLcsUserAlerSuccess(LcsUserAlerJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                LcsUserAlerData data = response.getData();
                getAccessTokenService.getAccessToken("android", CommonUtil.getDeviceId(getActivity()));
            }
        }
    }

    @Override
    public void gainLcsUserAlerFail() {

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
                KLog.e("modifyAreaSuccess", response.getMessage());
            }
        }
    }

    @Override
    public void modifyAreaFailed(BaseJson errorResponse) {
        if (null != errorResponse) {
            if (!CommonUtil.isNullOrEmpty(errorResponse.getMessage())) {
                KLog.e("modifyAreaFailed", errorResponse.getMessage());
            }
        }
    }
}
