package com.lcshidai.lc.ui.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.licai.AttachBean;
import com.lcshidai.lc.model.licai.FundDetailInfo;
import com.lcshidai.lc.ui.adapter.base.ListViewDataAdapter;
import com.lcshidai.lc.ui.adapter.base.ViewHolderBase;
import com.lcshidai.lc.ui.adapter.base.ViewHolderCreator;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublicShowMaterialActivity extends TRJActivity implements View.OnClickListener {

    public static final String PDF_FILE_URL = "pdf_file_url";
    public static final String PDF_FILE_TITLE = "pdf_file_title";
    @Bind(R.id.ib_top_bar_back)
    ImageButton ibTopBarBack;
    @Bind(R.id.tv_top_bar_title)
    TextView tvTopBarTitle;
    @Bind(R.id.lv_public_material)
    ListView lvPublicMaterial;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;
    private List<AttachBean> attachBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_show_material);
        ButterKnife.bind(this);

        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        tvTopBarTitle.setText("项目材料");
        ibTopBarBack.setOnClickListener(this);
        final ListViewDataAdapter<AttachBean> attachBeanListViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<AttachBean>() {
            @Override
            public ViewHolderBase<AttachBean> createViewHolder(int position) {
                return new ViewHolderBase<AttachBean>() {
                    TextView tvMaterialName;
                    View vDivider;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View convertView = layoutInflater.inflate(R.layout.attach_bean_list_item, null);
                        tvMaterialName = (TextView) convertView.findViewById(R.id.tv_material_name);
                        vDivider = convertView.findViewById(R.id.v_divider);
                        return convertView;
                    }

                    @Override
                    public void showData(int position, AttachBean itemData) {
                        if (null != itemData) {
                            tvMaterialName.setText(itemData.getName());
                            if (position == attachBeanList.size() - 1) {
                                vDivider.setVisibility(View.GONE);
                            } else {
                                vDivider.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                };
            }
        });
        FundDetailInfo fundDetailInfo = (FundDetailInfo) getIntent().getSerializableExtra(Constants.FUND_DETAIL);
        if (null != fundDetailInfo) {
            attachBeanList = fundDetailInfo.getAttach();
            if (attachBeanList.size() > 0) {
                attachBeanListViewDataAdapter.getDataList().addAll(attachBeanList);
                lvPublicMaterial.setAdapter(attachBeanListViewDataAdapter);
                lvPublicMaterial.setVisibility(View.VISIBLE);
                tvEmpty.setVisibility(View.GONE);
            } else {
                lvPublicMaterial.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        }
        lvPublicMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 显示公示材料
                Intent intent = new Intent(mContext, PdfViewActivity.class);
                intent.putExtra(PDF_FILE_URL, attachBeanListViewDataAdapter.getDataList().get(position).getUrl());
                intent.putExtra(PDF_FILE_TITLE, attachBeanListViewDataAdapter.getDataList().get(position).getName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @OnClick(R.id.ib_top_bar_back)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;
        }
    }
}
