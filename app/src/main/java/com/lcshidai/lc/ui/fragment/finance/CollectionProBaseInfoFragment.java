package com.lcshidai.lc.ui.fragment.finance;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.GetProjectBaseInfoImpl;
import com.lcshidai.lc.impl.onKeyInvest.GetAutoInvestProjectListImpl;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoData;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoExtensionVInItem;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoJson;
import com.lcshidai.lc.model.oneKeyInvest.CollectionProData;
import com.lcshidai.lc.model.oneKeyInvest.GetAutoInvestProjectListJson;
import com.lcshidai.lc.service.finance.GetFinanceProjectBaseInfoService;
import com.lcshidai.lc.service.oneKeyInvest.GetAutoInvestProjectListService;
import com.lcshidai.lc.ui.adapter.base.ListViewDataAdapter;
import com.lcshidai.lc.ui.adapter.base.ViewHolderBase;
import com.lcshidai.lc.ui.adapter.base.ViewHolderCreator;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.widget.MyListView;
import com.lcshidai.lc.widget.xlvfresh.XListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 集合基本信息详情
 * Created by RandyZhang on 2017/4/10.
 */

public class CollectionProBaseInfoFragment extends TRJFragment implements
        GetAutoInvestProjectListImpl, GetProjectBaseInfoImpl {
    private String mCollectionId;
    private String mPrjId;
    private ListViewDataAdapter<CollectionProData> collectionProDataListViewDataAdapter = null;

    private GetAutoInvestProjectListService autoInvestProjectListService = null;
    private GetFinanceProjectBaseInfoService getFinanceProjectBaseInfoService = null;

    private XListView lvCollectProjects;
    private MyListView lvHeadBaseInfo;
    private int isCollection = 0;

    private List<BaseInfoItem> baseInfoItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            isCollection = args.getInt(Constants.Project.IS_COLLECTION);
            mPrjId = args.getString(Constants.Project.PROJECT_ID);
            mCollectionId = mPrjId;
        }
        autoInvestProjectListService =
                new GetAutoInvestProjectListService((TRJActivity) getActivity(), this);
        getFinanceProjectBaseInfoService =
                new GetFinanceProjectBaseInfoService((TRJActivity) getActivity(), this);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_pro_base_info, container, false);
        View headerView = inflater.inflate(R.layout.fragment_collection_pro_list_header, null);
        lvHeadBaseInfo = (MyListView) headerView.findViewById(R.id.lv_head_base_info);

        lvCollectProjects = (XListView) view.findViewById(R.id.lv_collection_project);
        lvCollectProjects.addHeaderView(headerView);
        collectionProDataListViewDataAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<CollectionProData>() {
            @Override
            public ViewHolderBase<CollectionProData> createViewHolder(int position) {
                return new ViewHolderBase<CollectionProData>() {
                    TextView tvProNo;
                    TextView tvRaiseFundsAmount;
                    TextView tvAvailableAmount;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.fragment_collection_pro_list_item, null);
                        tvProNo = (TextView) view.findViewById(R.id.tv_project_no);
                        tvRaiseFundsAmount = (TextView) view.findViewById(R.id.tv_raise_funds_amount);
                        tvAvailableAmount = (TextView) view.findViewById(R.id.tv_available_invest_amount);
                        return view;
                    }

                    @Override
                    public void showData(int position, CollectionProData itemData) {
                        if (null != itemData) {
                            tvProNo.setText(itemData.getPrj_no());
                            tvRaiseFundsAmount.setText(itemData.getDemand_amount());
                            tvAvailableAmount.setText(itemData.getRemaining_amount());
                            if (!itemData.getBid_status().equals("1") && !itemData.getBid_status().equals("2")) {
                                tvAvailableAmount.setText("已完成");
                            } else {
                                tvAvailableAmount.setText(itemData.getRemaining_amount());
                            }
                        }
                    }
                };
            }
        });
        lvCollectProjects.setAdapter(collectionProDataListViewDataAdapter);
        lvCollectProjects.setPullRefreshEnable(false);      // 禁用下拉刷新
        lvCollectProjects.setPullLoadEnable(false);         // 禁用加载更多（接口未做分页）
        lvCollectProjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 1) {
                    CollectionProData collectionProData = collectionProDataListViewDataAdapter
                            .getDataList().get(position - 2);//由于添加了两个header，所以要减2
                    Intent intent = new Intent();
                    String prj_name = collectionProData.getPrj_type() + "-" + collectionProData.getPrj_no();
                    intent.putExtra(Constants.Project.PROJECT_ID, collectionProData.getPrj_id());
                    intent.putExtra(Constants.Project.PROJECT_NAME, prj_name);
                    // isCollection，子表的isCollection为0
                    intent.putExtra(Constants.Project.IS_COLLECTION, 0);
                    intent.setClass(mContext, FinanceProjectDetailActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        if (null != collectionProDataListViewDataAdapter)
            collectionProDataListViewDataAdapter.getDataList().clear();
        autoInvestProjectListService.getAutoInvestProjectList(mCollectionId);
        getFinanceProjectBaseInfoService.getProBaseInfo(mPrjId, isCollection);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void getAutoInvestProListSuccess(GetAutoInvestProjectListJson response) {
        // 处理自动投资项目列表
        if (null != response) {
            if (response.getData() != null && response.getData().size() > 0) {
                collectionProDataListViewDataAdapter.getDataList().addAll(response.getData());
                collectionProDataListViewDataAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void getAutoInvestProListFailed() {

    }

    @Override
    public void getProjectBaseInfoSuccess(FinanceItemBaseInfoJson response) {
        if (null != response) {
            dealWithProBaseInfo(response.getData());
            BaseInfoAdapter adapter = new BaseInfoAdapter();
            if (null != lvHeadBaseInfo)
                lvHeadBaseInfo.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void getProjectBaseInfoFail() {

    }

    private void dealWithProBaseInfo(FinanceItemBaseInfoData data) {
        baseInfoItems.clear();
        if (null != data) {
            List<FinanceItemBaseInfoExtensionVInItem> list = data.getBaseInfoList();
            String project_attribute_tips = data.getPrj_attribute_tips();
            if (null != list && list.size() > 0) {
                for (FinanceItemBaseInfoExtensionVInItem item : list) {
                    baseInfoItems.add(new BaseInfoItem(0, item.getK(), item.getV()));
                }
                baseInfoItems.add(new BaseInfoItem(1, "", project_attribute_tips));
                baseInfoItems.add(new BaseInfoItem(2, "", ""));
            }

        }
    }

    private class BaseInfoItem implements Serializable {
        private int type;
        private String key;
        private String value;

        public BaseInfoItem(int type, String key, String value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    class BaseInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return baseInfoItems.size();
        }

        @Override
        public Object getItem(int position) {
            return baseInfoItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            BaseInfoItem infoItem = baseInfoItems.get(position);
            int type = getItemViewType(position);
            if (convertView == null) {
                holder = new ViewHolder();
                if (type == 0) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_collection_pro_header_list_item0, null);
                    holder.tvKey = (TextView) convertView.findViewById(R.id.tv_key);
                    holder.tvValue = (TextView) convertView.findViewById(R.id.tv_value);
                }
                if (type == 1) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_collection_pro_header_list_item1, null);
                    holder.tvValue = (TextView) convertView.findViewById(R.id.tv_value);
                }
                if (type == 2) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_collection_pro_header_list_item2, null);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (null != infoItem) {
                if (type == 0) {
                    holder.tvKey.setText(infoItem.getKey());
                    holder.tvValue.setText(infoItem.getValue());
                } else if (type == 1) {
                    holder.tvValue.setText(infoItem.getValue());
                }
            }

            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            return baseInfoItems.get(position).getType();
        }
    }

    class ViewHolder {
        TextView tvKey;
        TextView tvValue;
        ImageView ivProIcon;
    }
}
