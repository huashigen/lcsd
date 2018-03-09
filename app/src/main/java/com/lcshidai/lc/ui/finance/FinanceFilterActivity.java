package com.lcshidai.lc.ui.finance;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.SearchConditionImpl;
import com.lcshidai.lc.model.finance.ConditionItemData;
import com.lcshidai.lc.model.finance.SearchConditionData;
import com.lcshidai.lc.model.finance.SearchConditionJson;
import com.lcshidai.lc.service.finance.HttpSearchConditionService;
import com.lcshidai.lc.ui.base.TRJActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by a on 2016/5/31.
 */
public class FinanceFilterActivity extends TRJActivity implements View.OnClickListener, SearchConditionImpl {
    private ImageButton btn_back, btn_search;
    private TextView tv_param;

    private HttpSearchConditionService mHttpSearchConditionService;

    private ParamsFilter mParamsFilter;

    private List<SearchConditionData> mData = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_finance_filter);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        tv_param = (TextView) findViewById(R.id.tv_param);

        btn_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        mHttpSearchConditionService = new HttpSearchConditionService(this, this);
        mHttpSearchConditionService.gainSearchCondition();

        mParamsFilter = new ParamsFilter();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mData != null) {
            refreshData();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_search:
                String params = mParamsFilter.getParams();
                String names = mParamsFilter.getNames();
                Intent intent = new Intent(this, FinanceFilterResultActivity.class);
                intent.putExtra("param", params);
                intent.putExtra("name", names);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void gainSearchConditionSuccess(SearchConditionJson response) {
        if (response != null) {
            if (response.getBoolen().equals("1")) {
                List<SearchConditionData> data = response.getData();
                mData = data;
                refreshData();
            }
        }
    }

    private void refreshData() {
        mParamsFilter.clear();
        tv_param.setText(mParamsFilter.getNames());
        LinearLayout contentView = (LinearLayout) findViewById(R.id.content_view);
        contentView.removeAllViews();
        List<SearchConditionData> data = mData;
        for (int i = 0; i < data.size(); i++) {
            final SearchConditionData searchConditionData = data.get(i);
            View filterItemView = LayoutInflater.from(this).inflate(R.layout.finance_filter_item, null);
            TextView tv_filter_item = (TextView) filterItemView.findViewById(R.id.tv_filter_item);
            GridView gridView = (GridView) filterItemView.findViewById(R.id.grid_view);

            tv_filter_item.setText(searchConditionData.getName());

            String[] from = {"text"};
            int[] to = {R.id.tv_grid_item};
            List<ConditionItemData> ConditionItemDataList = searchConditionData.getData();
            List<Map<String, String>> list = new ArrayList();
            for (ConditionItemData conditionItemData : ConditionItemDataList) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("text", conditionItemData.getName());
                list.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.finance_filter_grid_item, from, to);
            gridView.setAdapter(adapter);

            contentView.addView(filterItemView);

            final int index = i;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int count = adapterView.getCount();

                    //项目期限单选
                    if (index == 0) {
                        CheckedTextView curView = (CheckedTextView) adapterView.getChildAt(i).findViewById(R.id.tv_grid_item);
                        boolean isChecked = curView.isChecked();
                        for (int j = 0; j < count; j++) {
                            if (j != i) {
                                CheckedTextView item = (CheckedTextView) adapterView.getChildAt(j).findViewById(R.id.tv_grid_item);
                                item.setChecked(false);
                                mParamsFilter.remove(searchConditionData.getData().get(j).getValue(), searchConditionData.getData().get(j).getName());
                            }
                        }
                        curView.setChecked(!isChecked);
                        mParamsFilter.filter(searchConditionData.getData().get(i).getValue(), searchConditionData.getData().get(i).getName());
                    } else {
                        CheckedTextView firstView = (CheckedTextView) adapterView.getChildAt(0).findViewById(R.id.tv_grid_item);
                        if (i == 0) {
                            if (firstView.isChecked()) {
                                firstView.setChecked(false);
                                for (int j = 1; j < count; j++) {
                                    CheckedTextView item = (CheckedTextView) adapterView.getChildAt(j).findViewById(R.id.tv_grid_item);
                                    item.setChecked(false);
                                    mParamsFilter.filter(searchConditionData.getData().get(j).getValue(), searchConditionData.getData().get(j).getName());
                                }
                            } else {
                                firstView.setChecked(true);
                                for (int j = 1; j < count; j++) {
                                    CheckedTextView item = (CheckedTextView) adapterView.getChildAt(j).findViewById(R.id.tv_grid_item);
                                    item.setChecked(true);
                                    mParamsFilter.add(searchConditionData.getData().get(j).getValue(), searchConditionData.getData().get(j).getName());
                                }
                            }
                        } else {
                            if (firstView.isChecked()) {
                                firstView.setChecked(false);
                            }
                            CheckedTextView item = (CheckedTextView) adapterView.getChildAt(i).findViewById(R.id.tv_grid_item);
                            if (item.isChecked()) {
                                item.setChecked(false);
                            } else {
                                item.setChecked(true);
                            }
                            mParamsFilter.filter(searchConditionData.getData().get(i).getValue(), searchConditionData.getData().get(i).getName());
                        }
                    }

                    tv_param.setText(mParamsFilter.getNames());
                }
            });

        }
    }

    @Override
    public void gainSearchConditionFail() {

    }

    private class ParamsFilter {
        private List<String> mParams = new ArrayList<String>();
        private List<String> mNames = new ArrayList<String>();

        public void filter(String value, String name) {
            if (mParams.contains(value)) {
                mParams.remove(value);
                mNames.remove(name);
            } else {
                mParams.add(value);
                mNames.add(name);
            }
        }

        public void add(String value, String name) {
            if (!mParams.contains(value)) {
                mParams.add(value);
                mNames.add(name);
            }
        }

        public void remove(String value, String name) {
            if (mParams.contains(value)) {
                mParams.remove(value);
                mNames.remove(name);
            }
        }

        public String getParams() {
            StringBuffer buffer = new StringBuffer();
            for (String param : mParams) {
                buffer.append(param).append("|");
            }
            return buffer.toString();
        }

        public String getNames() {
            StringBuffer buffer = new StringBuffer();
            for (String name : mNames) {
                buffer.append(name).append("|");
            }
            String names = buffer.toString();
            if (names.endsWith("|")) {
                names = names.substring(0, names.length() - 1);
            }
            return names;
        }

        public void clear() {
            mParams.clear();
            mNames.clear();
        }

    }

}
