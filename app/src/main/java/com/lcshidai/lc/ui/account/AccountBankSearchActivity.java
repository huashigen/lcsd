package com.lcshidai.lc.ui.account;

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

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.AccountBankSearchImpl;
import com.lcshidai.lc.model.account.AccountBankSearchData;
import com.lcshidai.lc.model.account.AccountBankSearchJson;
import com.lcshidai.lc.service.account.HttpAccountBankSearchService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

import java.util.ArrayList;

/**
 * 开户行列表
 */
public class AccountBankSearchActivity extends TRJActivity implements AccountBankSearchImpl {
    HttpAccountBankSearchService habss;
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn, mSearch;
    private ListView mListView;
    private ItemAdapter mAdapter;
    private View mProgressContainer;
    private CustomEditTextLeftIcon edit_text;
    private String mCode, pcode, ccode;
    private String key_word = "";
    private View mEmptyView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_bank_search);
        mAdapter = new ItemAdapter(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCode = bundle.getString("code");
            pcode = bundle.getString("pcode");
            ccode = bundle.getString("ccode");
        }
        habss = new HttpAccountBankSearchService(this, this);
        initView();
        loadData(key_word);
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
        mTvTitle.setText("开户行");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);
        mSearch = (Button) findViewById(R.id.btn_search);
        mSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearch.setClickable(false);
                String key_word = edit_text.getEdtText().toString().trim();
                loadData(key_word);
            }
        });
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                Item item = mAdapter.getItem(pos);
                Intent intent = new Intent(mContext, UserBankAddActivity.class);
                intent.putExtra("sid", item.id);
                if (item.id.equals("-1")) {
                    intent.putExtra("name", "其他");
                } else {
                    intent.putExtra("name", item.mName);
                }
                setResult(23, intent);
                finish();
            }
        });
        mProgressContainer = findViewById(R.id.progressContainer);
        edit_text = (CustomEditTextLeftIcon) findViewById(R.id.edit_key_text);
        edit_text.setHint("输入关键字");
        mEmptyView = findViewById(R.id.rl_empty);
    }

    public void loadData(String key) {
        mProgressContainer.setVisibility(View.VISIBLE);
        mAdapter.mList.clear();
        mAdapter.notifyDataSetChanged();
        habss.gainAccountBankSearch(mCode, pcode, ccode, key);
    }

    private class Item {
        private String id;
        private String mName;//
        private String mBankNo;
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
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder vh = new ViewHolder();
            if (convertView == null || convertView.getTag() == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.activity_bank_list_item, null);
                vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                vh.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
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
    public void gainAccountBankSearchsuccess(AccountBankSearchJson response) {
        try {
            mProgressContainer.setVisibility(View.GONE);
            if (response != null) {
                if (response.getBoolen().equals("1")) {
                    if (response.getData() != null) {
                        mEmptyView.setVisibility(View.GONE);
                        int size = response.getData().size();
                        for (int i = 0; i < size; i++) {
                            AccountBankSearchData obj = response.getData().get(i);
                            Item item = new Item();
                            item.id = obj.getId();
                            item.mName = obj.getmName();
                            item.mBankNo = obj.getmBankNo();
                            mAdapter.mList.add(item);
                        }
                    } else {
                        Item item = new Item();
                        item.id = "-1";
                        item.mName = "无数据，选择其他";
                        item.mBankNo = "";
                        mAdapter.mList.add(item);
                    }
                } else {
                    Item item = new Item();
                    item.id = "-1";
                    item.mName = "无数据，选择其他";
                    item.mBankNo = "";
                    mAdapter.mList.add(item);
                }
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSearch.setClickable(true);
    }

    @Override
    public void gainAccountBankSearchfail() {
        mProgressContainer.setVisibility(View.GONE);
        mSearch.setClickable(true);
    }
}
