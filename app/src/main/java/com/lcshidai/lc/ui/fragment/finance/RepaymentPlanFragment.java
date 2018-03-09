package com.lcshidai.lc.ui.fragment.finance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.FinaceItemRepaymentPlayImpl;
import com.lcshidai.lc.model.finance.FinaceItemRepaymentPlayData;
import com.lcshidai.lc.model.finance.FinaceItemRepaymentPlayInItem;
import com.lcshidai.lc.model.finance.FinaceItemRepaymentPlayItem;
import com.lcshidai.lc.model.finance.FinaceItemRepaymentPlayListJson;
import com.lcshidai.lc.service.finance.HttpFinaceItemRepaymentPlayService;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.utils.StringUtils;

/**
 * @author 001355
 * @Description: 回款计划
 */
public class RepaymentPlanFragment extends TRJFragment implements FinaceItemRepaymentPlayImpl {
    protected View mProgressContainer;
    protected ListView mListView;
    protected LVItemAdapter mAdapter;
    protected View mEmptyView;

    public static int MI = 50;

    public static int PROFIT_DP = 74;
    public static String HOOK_MARK = "^Ho@oK^";
    public static String HOOK_MARK_S = "\\^Ho@oK\\^";

    public static String HOOK_MARK_LAST = "^Ho@LASToK^";
    public static String HOOK_MARK_LAST_S = "\\^Ho@LASToK\\^";
    public static int TOTAL_ACCOUNT_DP = PROFIT_DP + 154;

    public static int THIRD_DP = TOTAL_ACCOUNT_DP + 122;

    private String mPrjId;// , mID;
    public View mStateView;

    HttpFinaceItemRepaymentPlayService hfrps;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPrjId = args.getString("prj_id");
        }
        hfrps = new HttpFinaceItemRepaymentPlayService(
                (FinanceProjectDetailActivity) getActivity(), this);
    }

    View mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_finance_repaymentlv, container, false);
        mListView = (ListView) mContentView.findViewById(R.id.listview);
        mEmptyView = mContentView.findViewById(R.id.empty);
        mListView.setEmptyView(mEmptyView);
        mProgressContainer = mContentView.findViewById(R.id.progressContainer);
        mProgressContainer.setVisibility(View.GONE);
        mStateView = mContentView.findViewById(R.id.iv_state);
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        mStateView.setVisibility(View.GONE);
    }

    void setView(FinaceItemRepaymentPlayData pi) {
        ((TextView) mContentView.findViewById(R.id.tv_sum_pri_interest))
                .setText(replaseYuan(pi.sum_pri_interest));
        ((TextView) mContentView.findViewById(R.id.tv_sum_principal))
                .setText(replaseYuan(pi.sum_principal));
        ((TextView) mContentView.findViewById(R.id.tv_sum_yield))
                .setText(replaseYuan(pi.sum_yield));
        ((TextView) mContentView.findViewById(R.id.tv_sum_rest_principal)).setText("");
        String str1 = "以下数据根据\"";
        String str2 = "投资金额" + pi.money + "元,期限" + pi.time_limit;
        String str3 = "\"计算,您的收益请在账户-我的投资中查看.";
        String totle = str1 + str2 + str3;
        SpannableString ss = new SpannableString(totle);

        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_finance_haikuan_1)),
                0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ForegroundColorSpan(getResources()
                        .getColor(R.color.color_finance_child_yellow)), str1.length(),
                str2.length() + str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_finance_haikuan_1)),
                str1.length() + str2.length(), str1.length() + str2.length()
                        + str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) mContentView.findViewById(R.id.tv_info)).setText(ss);
        mAdapter = new LVItemAdapter();

        boolean isExit = false;
        for (FinaceItemRepaymentPlayInItem item : pi.listFinaceItemRepaymentPlay) {
            String info = item.yield.replace("元", "");
            if (!StringUtils.isFloat(info) && !StringUtils.isDouble(info)
                    && !StringUtils.isInteger(info)) {
                String infoV = item.yield;
                item.principal = "-";
                item.yield = "-";
                item.pri_interest = "-";
                isExit = true;
            }
            mAdapter.mItems.add(item);
        }

        if (!isExit) {
            ((TextView) mContentView.findViewById(R.id.tv_info_un)).setText("单位：元");
        }
        setAdapter(mAdapter);
        mContentView.findViewById(R.id.rl_empty).setVisibility(View.GONE);
    }

    public void setAdapter(LVItemAdapter adapter) {
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadData() {
        hfrps.gainFinaceItemRepaymentPlay(mPrjId);

    }

    public abstract class ExpandableListAdapter<K, V> extends
            BaseExpandableListAdapter {
        private HashMap<K, ArrayList<V>> mMap = new HashMap<K, ArrayList<V>>();
        private ArrayList<K> mArrayList = new ArrayList<K>();

        public void add(K k, ArrayList<V> a) {
            mArrayList.add(k);
            mMap.put(k, a);
        }

        @Override
        public int getGroupCount() {
            return mArrayList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mMap.get(mArrayList.get(groupPosition)).size();
        }

        @Override
        public K getGroup(int groupPosition) {
            return mArrayList.get(groupPosition);
        }

        @Override
        public V getChild(int groupPosition, int childPosition) {
            return mMap.get(getGroup(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition * 10000 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public abstract View getGroupView(int groupPosition,
                                          boolean isExpanded, View convertView, ViewGroup parent);

        @Override
        public abstract View getChildView(int groupPosition, int childPosition,
                                          boolean isLastChild, View convertView, ViewGroup parent);

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    public class ItemAdapter extends ExpandableListAdapter<String, String> {

        public TextView getGenericView() {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 64);

            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            return textView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder groupHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.fragment_exlist_group_item_re, null);
                groupHolder = new GroupHolder();
                groupHolder.img = (ImageView) convertView.findViewById(R.id.tag_img);
                groupHolder.iv_left = (ImageView) convertView.findViewById(R.id.iv_left);
                groupHolder.title = (TextView) convertView.findViewById(R.id.title_view);
                convertView.setTag(groupHolder);
            }
            groupHolder = (GroupHolder) convertView.getTag();
            if (isExpanded) {
                groupHolder.img.setImageResource(R.drawable.icon_indicator_up);
            } else {
                groupHolder.img.setImageResource(R.drawable.icon_indicator_down);
            }

            groupHolder.title.setText(getGroup(groupPosition));
            groupHolder.iv_left.setVisibility(View.GONE);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, final ViewGroup parent) {

            final String str = getChild(groupPosition, childPosition);
            String[] sts;
            if (str.indexOf(HOOK_MARK_LAST) != -1) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.fragment_finance_repayment_last_item, null);
                TextView tv_sum_pri_interest, tv_sum_principal, tv_sum_yield;
                tv_sum_pri_interest = (TextView) convertView
                        .findViewById(R.id.tv_sum_pri_interest);
                tv_sum_principal = (TextView) convertView
                        .findViewById(R.id.tv_sum_principal);
                tv_sum_yield = (TextView) convertView
                        .findViewById(R.id.tv_sum_yield);
                if (str.indexOf(HOOK_MARK_LAST) != -1) {
                    sts = str.split(HOOK_MARK_LAST_S);
                    if (sts.length > 0)
                        tv_sum_pri_interest.setText(sts[0]);
                    if (sts.length > 1)
                        tv_sum_principal.setText(sts[1]);
                    if (sts.length > 2)
                        tv_sum_yield.setText(sts[2]);
                }
                return convertView;
            }

            final TextView tv_label;
            TextView tv_value;

            // final View v1, v2, v3, v, imgV;
            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.group_ch, null);
            tv_label = (TextView) convertView.findViewById(R.id.tv_label);
            tv_value = (TextView) convertView.findViewById(R.id.tv_value);
            if (str.indexOf(HOOK_MARK) != -1) {
                sts = str.split(HOOK_MARK_S);
                if (sts.length > 0)
                    tv_label.setText(sts[0]);
                if (sts.length > 1)
                    tv_value.setText(sts[1]);
            }

            return convertView;
        }

    }

    class GroupHolder {
        ImageView iv_left;
        ImageView img;
        TextView title;
    }

    class LVItemAdapter extends BaseAdapter {

        public ArrayList<FinaceItemRepaymentPlayInItem> mItems = new ArrayList<FinaceItemRepaymentPlayInItem>();

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.finance_item_repayment_item, null);
                holder.tv_1 = (TextView) convertView.findViewById(R.id.tv_t1);
                holder.tv_2 = (TextView) convertView.findViewById(R.id.tv_t2);
                holder.tv_3 = (TextView) convertView.findViewById(R.id.tv_t3);
                holder.tv_4 = (TextView) convertView.findViewById(R.id.tv_t4);
                holder.tv_5 = (TextView) convertView.findViewById(R.id.tv_t5);
                holder.tv_time = (TextView) convertView
                        .findViewById(R.id.tv_tbottom);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (mItems.size() > 0) {
                FinaceItemRepaymentPlayInItem repayment = mItems.get(position);
                if (repayment.repay_periods.equals("募集期利息")) {
                    holder.tv_1.setText("(" + repayment.repay_periods + "回款日)");
                } else {
                    holder.tv_1.setText("(" + repayment.repay_periods + repayment.numString + ")");
                }
                // holder.tv_1.setText(replaseYuan(repayment.repay_periods));
                holder.tv_2.setText(replaseYuan(repayment.pri_interest));
                holder.tv_3.setText(replaseYuan(repayment.principal));
                holder.tv_4.setText(replaseYuan(repayment.yield));
                // holder.tv_4.setText(replaseYuan("2000000.00"));
                holder.tv_5.setText(replaseYuan(repayment.rest_principal));
                holder.tv_time.setText(replaseYuan(repayment.repay_date));
                if (repayment.status.equals("回款结束") || repayment.status.equals("已回款")) {
                    holder.tv_time.append(" (已回款)");
                }
            }

            return convertView;
        }

    }

    class ViewHolder {
        TextView tv_1;
        TextView tv_2;
        TextView tv_3;
        TextView tv_4;
        TextView tv_5;
        TextView tv_time;
    }

    private String replaseYuan(String str) {
        str = str.replaceAll("元", "");
        // if(str.trim().equals("")||str.trim().equals("0.00"))return "-";
        return str;
    }

    @Override
    public void gainFinaceItemRepaymentPlaysuccess(
            FinaceItemRepaymentPlayListJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    FinaceItemRepaymentPlayData jobj = response.getData();
                    FinaceItemRepaymentPlayItem listItem = jobj.getListItem();

                    List<FinaceItemRepaymentPlayInItem> ListFive = listItem.getListFive();

                    if (ListFive != null) {
                        int sizeFive = ListFive.size();
                        for (int index = 0; index < sizeFive; index++) {
                            FinaceItemRepaymentPlayInItem inItem = ListFive.get(index);
                            if (index > 1) {
                                inItem.setNumString("第" + (index + 1) + "期");
                            } else {
                                inItem.setNumString("");
                            }
                            jobj.listFinaceItemRepaymentPlay.add(inItem);
                        }

                    }
                    List<FinaceItemRepaymentPlayInItem> ListSix = listItem.getListSix();
                    if (ListSix != null) {
                        int size = ListSix.size();
                        for (int index = 0; index < size; index++) {
                            FinaceItemRepaymentPlayInItem jitem = ListSix.get(index);
                            if (index > 1) {
                                jitem.setNumString("第" + (index + 1) + "期");
                            } else {
                                jitem.setNumString("");
                            }
                            jobj.listFinaceItemRepaymentPlay.add(jitem);
                        }

                    }

                    List<FinaceItemRepaymentPlayInItem> ListSeven = listItem.getListSeven();
                    if (ListSeven != null) {
                        int size = ListSeven.size();
                        for (int index = 0; index < size; index++) {
                            FinaceItemRepaymentPlayInItem jitem = ListSeven.get(index);

                            if (index > 1) {
                                jitem.setNumString("第" + (index + 1) + "期");
                            } else {
                                jitem.setNumString("");
                            }
                            jobj.listFinaceItemRepaymentPlay.add(jitem);
                        }
                    }
                    setView(jobj);
                } else {
                    mContentView.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    @Override
    public void gainFinaceItemRepaymentPlayfail() {
        mContentView.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
    }
}