package com.lcshidai.lc.ui.fragment.newfinan;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.newfinan.NewFinanceActivity;
import com.lcshidai.lc.utils.GoClassUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.TestLogin;
import com.lcshidai.lc.widget.xlvfresh.XListView;
import com.lcshidai.lc.widget.xlvfresh.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewFinanceListFragment extends TRJFragment implements
        IXListViewListener {

    private String LOAD_URL = "Mobile2/Invest/flist";
    private XListView mListView;
    private View mProgressBar;
    private RelativeLayout empty_rl;
    private NewFinanceActivity newFinanceActivity;
    private int mPagesize = 10;
    private int mP = 1;
    boolean hasMore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newFinanceActivity = (NewFinanceActivity) getActivity();
        View contentView = inflater.inflate(R.layout.fragment_lisvt_view,
                container, false);
        mProgressBar = contentView.findViewById(R.id.progressContainer);
        empty_rl = (RelativeLayout) contentView.findViewById(R.id.rl_empty);
        mListView = (XListView) contentView.findViewById(R.id.listView);
        mItemApdater = new ItemAdapter(getActivity());
        mListView.setPullLoadEnable(false);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        mListView.setXListViewListener(this);
        mListView.setAdapter(mItemApdater);
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (newFinanceActivity.getIsAddedListFragment()) {
            if (mItemApdater != null && mItemApdater.mList.size() > 1) {
                if (MemorySave.MS.goToFinanceAll)
                    mListView.showHeader();
                MemorySave.MS.goToFinanceAll = false;
            } else {
                loadData();
            }
        }
    }

    public void loadData() {
        mP = 1;
        hasMore = false;
        mItemApdater.clear();
        mItemApdater.notifyDataSetChanged();
        mListView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        loadMore();
    }

    public synchronized void reLoadDataDown() {
        if (mItemApdater != null) {
            isDown = true;
            hasMore = false;
            mP = 1;
            loadMore();
        }
    }


    @Override
    public void onRefresh() {
        reLoadDataDown();
    }

    @Override
    public void onLoadMore() {

    }

    boolean isDown;
    ItemAdapter mItemApdater;

    public class ItemAdapter extends ArrayAdapter<Item> {
        private Context mContext;
        public ArrayList<Item> mList = new ArrayList<Item>();

        public ItemAdapter(Activity context) {
            super(context, 0);
            mContext = context;
        }

        @Override
        public void clear() {
            mList.clear();
            super.clear();
        }

        @Override
        public void add(Item object) {
            mList.add(object);
        }

        @Override
        public int getCount() {
            return mList.size() + (hasMore ? 1 : 0);
        }

        @Override
        public Item getItem(int position) {
            return mList.get(position);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView,
                            final ViewGroup parent) {
            final ViewHolders vh;
            if (position < mList.size()) {
                if (convertView == null || convertView.getTag() == null) {
                    vh = new ViewHolders();
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.layout_newfinance_list_item, null);
                    vh.tv_prj_type = (TextView) convertView
                            .findViewById(R.id.tv_prj_type);
                    vh.tv_prj_name = (TextView) convertView
                            .findViewById(R.id.tv_prj_name);
                    vh.im_transfer = (ImageView) convertView
                            .findViewById(R.id.im_transfer);
                    vh.im_transfer_cn = (ImageView) convertView
                            .findViewById(R.id.im_transfer_cn);
                    vh.im_new = (ImageView) convertView
                            .findViewById(R.id.im_new);
                    vh.iv_activity = (ImageView) convertView
                            .findViewById(R.id.iv_activity);
                    vh.iv_limit = (ImageView) convertView
                            .findViewById(R.id.iv_limit);
                    vh.title_tv_1 = (TextView) convertView
                            .findViewById(R.id.title_tv_1);
                    vh.title_tv_2 = (TextView) convertView
                            .findViewById(R.id.title_tv_2);
                    vh.title_tv_3 = (TextView) convertView
                            .findViewById(R.id.title_tv_3);
                    vh.content_tv_1 = (TextView) convertView
                            .findViewById(R.id.content_tv_1);
                    vh.content_tv_2 = (TextView) convertView
                            .findViewById(R.id.content_tv_2);
                    vh.content_tv_3 = (TextView) convertView
                            .findViewById(R.id.content_tv_3);
                    vh.rl_th_left = convertView.findViewById(R.id.rl_th_left);
                    vh.tv_th_left = (TextView) convertView
                            .findViewById(R.id.tv_th_left);
                    vh.btn_right = (Button) convertView
                            .findViewById(R.id.btn_right);
                    vh.pb = (ProgressBar) convertView.findViewById(R.id.pb);
                    convertView.setTag(vh);
                } else {
                    vh = (ViewHolders) convertView.getTag();
                }
                final Item pi = mList.get(position);

                vh.tv_prj_type.setText(pi.prj_type_name);
                vh.tv_prj_name.setText(pi.prj_name);

                if (pi.can_transfer != null || !pi.can_transfer.equals("1")) {
                    vh.im_transfer.setVisibility(View.GONE);
                } else {
                    vh.im_transfer.setVisibility(View.VISIBLE);
                }
                vh.content_tv_1.setText(pi.rate_view + pi.rate_symbol);

                vh.title_tv_2.setText("转让价格(元)");
                vh.title_tv_3.setText("剩余期限:");

                vh.content_tv_2.setText(pi.money_view);
                vh.content_tv_3
                        .setText(pi.time_limit + pi.time_limit_unit_view);

                if (pi.status.equals("1")) {
                    vh.rl_th_left.setBackgroundDrawable(getResources()
                            .getDrawable(
                                    R.drawable.feedback_sub_orange_hollow_bg));
                    vh.btn_right
                            .setBackgroundDrawable(getResources()
                                    .getDrawable(
                                            R.drawable.feedback_submit_right_hollow_bg_xml));
                    vh.tv_th_left.setTextColor(Color.rgb(0xff, 0xaa, 0x00));// ##FFAA00
                    vh.btn_right.setText("立即投资");
                    vh.btn_right.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                        }
                    });
                } else {
                    vh.rl_th_left
                            .setBackgroundDrawable(getResources().getDrawable(
                                    R.drawable.feedback_sub_gray_hollow_bg));
                    vh.btn_right
                            .setBackgroundDrawable(getResources()
                                    .getDrawable(
                                            R.drawable.feedback_sub_gray_right_hollow_bg));
                    vh.tv_th_left.setTextColor(Color.rgb(0xA4, 0xA8, 0xAE));// #"#A4A8AE"
                    vh.btn_right.setText("已转让");
                }
                vh.im_transfer_cn.setVisibility(View.VISIBLE);
                vh.tv_th_left.setText(pi.repay_way_view);

                vh.pb.setProgress(0);

                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();
                        args.putString("prj_id", pi.prjid);
//						getActivity().getIntent().putExtra("goClass",
//								FinanceProjectDetailActivity.class.getName());
                        GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
                        GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
                        MemorySave.MS.args = args;
                        new TestLogin(getActivity())
                                .testIt((TRJActivity) getActivity());
                    }
                });
            } else {
                View view = View.inflate(getActivity(), R.layout.loading_item,
                        null);
                loadMore();
                return view;
            }
            return convertView;
        }

    }

    public class ViewHolders {

        TextView tv_prj_type;
        TextView tv_prj_name;

        ImageView im_transfer;
        ImageView im_transfer_cn;
        ImageView im_new;
        ImageView iv_activity;
        ImageView iv_limit;

        TextView title_tv_1;
        TextView title_tv_2;
        TextView title_tv_3;

        TextView content_tv_1;
        TextView content_tv_2;
        TextView content_tv_3;

        View rl_th_left;
        TextView tv_th_left;
        Button btn_right;

        ProgressBar pb;
    }

    class Item {
        public String prjid;// 项目id
        public String id;// 转让id
        public String prj_name;// 项目名称
        public String time_limit;// 期限
        public String time_limit_unit;// 期限单位，日，月
        public String time_limit_unit_view;// 期限单位中文
        public String rate_type;// 利率单位
        public String rate_type_view;// 利率单位中文
        public String rate_symbol;// 显示 %或 ‰
        public String rate;// 利率
        public String rate_view;// 利率显示
        public String safeguards;// 担保
        public String safeguards_view;// 担保
        public String prj_revenue;// 份额预期价值
        public String prj_revenue_view;// 份额预期价值显示
        public String property;// 转让份额
        public String property_view;// 转让份额显示
        public String money;// 转让价格
        public String money_view;// 转让价格显示
        public String financing_will_income;// 投资预期收益
        public String is_transfer;
        public String year_rate;
        public String repay_way_view;
        public String prj_type;
        public String prj_type_name;// 项目类型中文显示;
        public String repay_way;
        public String pri_interest_view;
        public String status;// -> 转让状态码 (!A2014/03/20)
        public String status_display;// -> 转让状态 (!A2014/03/20)
        public String prj_no;
        public String can_transfer;//

    }

    void loadMore() {
        RequestParams params = new RequestParams();
        params.put("p", String.valueOf(mP++));// p 分页页数
        params.put("page_size", mPagesize + "");// 每页多少条 ，不传默认每页10条
        post(LOAD_URL, params, new JsonHttpResponseHandler(this.getActivity()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response != null) {
                        String boolen = response.getString("boolen");
                        if (boolen.equals("1")) {
                            if (isDown) {
                                isDown = false;
                                mItemApdater.clear();
                                mListView.stopRefresh();
                                mListView.stopLoadMore();
                                mListView.setRefreshTime();
                            }
                            JSONArray jarray = response.optJSONObject("data")
                                    .optJSONArray("list");// .optJSONArray("list");
                            // mAdapter.clear();
                            JSONObject jobj = null;
                            Item item = null;
                            hasMore = jarray.length() >= mPagesize;
                            if (hasMore)
                                if (StringUtils.isInteger(response
                                        .optJSONObject("data").optString(
                                                "total_page"))
                                        && StringUtils.isInteger(response
                                        .optJSONObject("data")
                                        .optString("current_page"))) {
                                    int count = Integer.parseInt(response
                                            .optJSONObject("data").optString(
                                                    "total_page"));
                                    if (Integer.parseInt(response
                                            .optJSONObject("data").optString(
                                                    "current_page")) >= count) {
                                        hasMore = false;
                                    }
                                }
                            for (int j = 0; j < jarray.length(); j++) {
                                jobj = jarray.getJSONObject(j);
                                item = new Item();
                                for (java.lang.reflect.Field field2 : item
                                        .getClass().getFields()) {
                                    String str = jobj.optString(field2
                                            .getName());
                                    if (str != null && !str.equals("null")) {
                                        field2.set(item, str.trim());
                                    } else {
                                        field2.set(item, "");
                                    }
                                }
                                mItemApdater.add(item);
                            }
                        } else {
                            hasMore = false;
                        }
                        if (mItemApdater.isEmpty()
                                && !(response.has("logined") && response
                                .getString("logined").equals("0"))) {
                            mListView.setVisibility(View.GONE);
                            empty_rl.setVisibility(View.VISIBLE);
                        } else {
                            empty_rl.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        hasMore = false;
                    }
                } catch (Exception e) {
                    hasMore = false;
                    e.printStackTrace();
                } finally {
                    mProgressBar.setVisibility(View.GONE);
                    mItemApdater.notifyDataSetChanged();
                }

            }
        });
    }

}
