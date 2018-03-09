package com.lcshidai.lc.ui.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.licai.FundManagerBean;
import com.lcshidai.lc.ui.base.TRJFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 基金列表Fragment
 */
public class FundManagerFragment extends TRJFragment {

    public static final String FUND_MANAGER_BEAN = "fund_manager_bean";
    @Bind(R.id.tv_manager_name)
    TextView tvManagerName;
    @Bind(R.id.tv_manager_experience)
    TextView tvManagerExperience;
    @Bind(R.id.tv_manager_school)
    TextView tvManagerSchool;
    @Bind(R.id.tv_manager_desc)
    TextView tvManagerDesc;

    private FundManagerBean fundManagerBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fundManagerBean = (FundManagerBean) getArguments().getSerializable(FUND_MANAGER_BEAN);
        }
    }

    public static FundManagerFragment newInstance(FundManagerBean fundManagerBean) {
        FundManagerFragment fragment = new FundManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FUND_MANAGER_BEAN, fundManagerBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fund_manager, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewWithData(fundManagerBean);
    }

    private void initViewWithData(FundManagerBean fundManagerBean) {

        if (null != fundManagerBean) {
            tvManagerName.setText(fundManagerBean.getXname());
            tvManagerExperience.setText(fundManagerBean.getWork_year());
            tvManagerSchool.setText(fundManagerBean.getGraduate_college());
            tvManagerDesc.setText(fundManagerBean.getDescribes());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
