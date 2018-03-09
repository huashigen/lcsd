package com.lcshidai.lc.ui.account;

import java.util.ArrayList;
import java.util.List;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.CityListImpl;
import com.lcshidai.lc.model.account.CityListChild;
import com.lcshidai.lc.model.account.CityListData;
import com.lcshidai.lc.model.account.CityListJson;
import com.lcshidai.lc.service.account.HttpCityListService;
import com.lcshidai.lc.ui.base.TRJActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 省份列表
 *
 * @author
 */
public class CityListActivity extends TRJActivity implements CityListImpl {
    HttpCityListService hcls;
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;
    private ListView mListView;
    private ItemAdapter mAdapter;
    private View mProgressContainer;
    private String pcode = "", pname = "", ccode = "", mCode = ""; // mCode-银行卡code
    private String province_code = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        mAdapter = new ItemAdapter(this);
        mAdapter.clear();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (-1 == bundle.getInt("intent_flag")) {
                mCode = bundle.getString("mCode");
            } else {
                mCode = bundle.getString("code");
                pcode = bundle.getString("pcode");
                pname = bundle.getString("pname");
            }
        }
        hcls = new HttpCityListService(this, this);
        initView();
        loadData();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("开户城市");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        // if(!pcode.equals("")){
        // mSaveBtn.setVisibility(View.VISIBLE);
        // mSaveBtn.setBackgroundDrawable(null);
        // mSaveBtn.setText(" 确定　");
        // mSaveBtn.setOnClickListener(new OnClickListener(){
        // @Override
        // public void onClick(View v) {
        // String name = "",code = "";
        // for(Item it : mAdapter.mList){
        // if(it.isSelected){
        // name = it.mName;
        // code = it.code;
        // }
        // }
        // if(name.equals("")){
        // showToast("请选择城市");
        // return;
        // }
        //
        // Intent intent = new Intent();
        // intent.putExtra("pname", pname);
        // intent.putExtra("pcode", pcode);
        // intent.putExtra("cname", name);
        // intent.putExtra("ccode", code);
        // setResult(RESULT_OK, intent);
        // finish();
        // }
        // });
        // }else{
        // mSaveBtn.setVisibility(View.INVISIBLE);
        // }
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                Item item = mAdapter.getItem(pos);
                if (pcode.equals("")) {
                    province_code = item.code;
                    Intent intent = new Intent(mContext, CityListActivity.class);
                    intent.putExtra("code", mCode);
                    intent.putExtra("pcode", item.code);
                    intent.putExtra("pname", item.mName);
                    startActivityForResult(intent, 21);
                } else {
                    ccode = item.code;
                    Intent intent = new Intent(mContext, AccountBankSearchActivity.class);
                    intent.putExtra("code", mCode);
                    intent.putExtra("pcode", pcode);
                    intent.putExtra("ccode", ccode);
                    startActivityForResult(intent, 22);
                }
            }
        });
        mProgressContainer = findViewById(R.id.progressContainer);
    }

    public void loadData() {
        hcls.gainCityList(pcode);
    }

    private class Item {
        private String id;
        private String code;
        private String mName;//
        private boolean isSelected;
    }

    public class ItemAdapter extends ArrayAdapter<Item> {
        private Context mContext;
        private ArrayList<Item> mList = new ArrayList<Item>();

        public ItemAdapter(Activity context) {
            super(context, 0);
            mContext = context;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Item getItem(int position) {
            return mList.get(position);
        }

        @Override
        public View getView(final int position, View convertView,
                            final ViewGroup parent) {
            ViewHolder vh = new ViewHolder();
            if (convertView == null || convertView.getTag() == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.activity_bank_list_item, null);
                vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                vh.iv_select = (ImageView) convertView
                        .findViewById(R.id.iv_select);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            final Item item = getItem(position);
            vh.tv_name.setText(item.mName);
            if (item.isSelected) {
                vh.iv_select.setVisibility(View.VISIBLE);
            } else {
                vh.iv_select.setVisibility(View.GONE);
            }
            return convertView;
        }

        public class ViewHolder {
            TextView tv_name;
            ImageView iv_select;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        // 开户行回到城市
        if (requestCode == 22 && resultCode == 23) {
            intent.putExtra("sid", data.getStringExtra("sid"));
            intent.putExtra("khh_name", data.getStringExtra("name"));
            intent.putExtra("city_code", ccode);
            setResult(24, intent);
            finish();
        }

        // 城市回到省份
        if (requestCode == 21 && resultCode == 24) {
            intent.putExtra("sid", data.getStringExtra("sid"));
            intent.putExtra("khh_name", data.getStringExtra("khh_name"));
            intent.putExtra("city_code", data.getStringExtra("city_code"));
            intent.putExtra("province_code", province_code);
            setResult(25, intent);
            finish();
        }
    }

    @Override
    public void gainCityListsuccess(CityListJson response) {
        try {
            mProgressContainer.setVisibility(View.GONE);
            if (response != null) {
                if (response.getBoolen().equals("1")) {
                    List<CityListData> array = response.getData();
                    if (array != null) {
                        int size = array.size();
                        for (int i = 0; i < size; i++) {
                            CityListData obj = array.get(i);
                            if (!pcode.equals("")) {
                                Item item = new Item();
                                item.id = obj.getId();
                                item.code = obj.getCode();
                                item.mName = obj.getName_cn();
                                mAdapter.mList.add(item);
                            } else {
                                List<CityListChild> array_list = obj.getData();
                                if (array_list != null) {
                                    int len = array_list.size();
                                    for (int j = 0; j < len; j++) {
                                        CityListChild jo = array_list.get(j);
                                        Item item = new Item();
                                        item.id = jo.getId();
                                        item.code = jo.getCode();
                                        item.mName = jo.getName_cn();
                                        mAdapter.mList.add(item);
                                    }
                                }
                            }
                        }
                    }
                    mListView.setAdapter(mAdapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainCityListfail() {
        mProgressContainer.setVisibility(View.GONE);
    }
}
