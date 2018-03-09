package com.lcshidai.lc.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.finance.FinanceJxqItemData;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.RewardCheckUtil;

import java.util.List;

/**
 * 加息券Adapter
 */
public class FinanceJxqAdapter extends BaseAdapter {
    private List<FinanceJxqItemData> list;
    private String mCurrentSelectJxqId = "-1";
    private boolean mClear;

    public FinanceJxqAdapter(List<FinanceJxqItemData> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.finance_jxq_item, null);
            holder.iv_bg_hb = (ImageView) convertView.findViewById(R.id.iv_bg_hb);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_top_bar_title);
            holder.cb_check = (CheckBox) convertView.findViewById(R.id.cb_check);
            holder.tv_use_amount = (TextView) convertView.findViewById(R.id.tv_use_amount);
            holder.tv_unable = (TextView) convertView.findViewById(R.id.tv_unable);
            holder.tv_rest_amount = (TextView) convertView.findViewById(R.id.tv_rest_amount);
            holder.tv_describe = (TextView) convertView.findViewById(R.id.tv_describe);
            holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
            holder.tv_use_tip = (TextView) convertView.findViewById(R.id.tv_use_tip);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tvInvestAmountLimit = (TextView) convertView.findViewById(R.id.tv_invest_amount_limit);
            holder.tvInvestTimeLimit = (TextView) convertView.findViewById(R.id.tv_invest_time_limit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final FinanceJxqItemData financeJxqItemData = list.get(position);
        holder.tv_title.setText(String.format("%s (%s", financeJxqItemData.getDisp_name(),
                financeJxqItemData.getSource()));
        holder.tv_use_amount.setText(String.format("-%s元", financeJxqItemData.getAmount()));
        holder.tv_rest_amount.setText(String.format("+%s", financeJxqItemData.getRate_view()));
        holder.tv_describe.setText(financeJxqItemData.getUse_tip());
        holder.tv_use_tip.setText(financeJxqItemData.getSource());
        if (!CommonUtil.isNullOrEmpty(financeJxqItemData.getPrj_day_limit_view()))
            holder.tvInvestTimeLimit.setText(financeJxqItemData.getPrj_day_limit_view());
        if (!CommonUtil.isNullOrEmpty(financeJxqItemData.getMoney_limit_view()))
            holder.tvInvestAmountLimit.setText(financeJxqItemData.getMoney_limit_view());
        holder.tv_date.setText(String.format("%s-%s", financeJxqItemData.getCtime().replaceAll("-", "."),
                financeJxqItemData.getEnd_time().replaceAll("-", ".")));

        int is_usable = financeJxqItemData.getIs_usable();
        if (is_usable == 1) {
            holder.iv_bg_hb.setImageResource(R.drawable.bg_hb1);
//        	holder.tv_use_amount.setVisibility(View.VISIBLE);
            holder.tv_unable.setVisibility(View.GONE);
            holder.tv_title.setTextColor(Color.BLACK);
        } else if (is_usable == 0) {
            holder.iv_bg_hb.setImageResource(R.drawable.bg_hb3);
//        	holder.tv_use_amount.setVisibility(View.GONE);
            holder.tv_unable.setVisibility(View.VISIBLE);
            holder.tv_title.setTextColor(Color.rgb(0x99, 0x99, 0x99));
        }
        if (mCurrentSelectJxqId != null && mCurrentSelectJxqId.equals(financeJxqItemData.getId())) {
            if (holder.cb_check.isChecked()) {// 点击已选择的加息券，
                holder.cb_check.setChecked(false);
                holder.tv_use_amount.setSelected(false);
                if (mClear) {
                    RewardCheckUtil.getInstance().resetJxq();// 不清除reward type
                    RewardCheckUtil.getInstance().setJxq_id("-1");
                }
            } else {// 点击未选择的加息券
                holder.cb_check.setChecked(true);
                holder.tv_use_amount.setSelected(true);
                if (mClear) {
                    RewardCheckUtil.getInstance().resetJxq();// 不清除reward type
                    RewardCheckUtil.getInstance().setJxq_id(financeJxqItemData.getId());
                    RewardCheckUtil.getInstance().setJx_rate(financeJxqItemData.getRate_view());
                }
            }
        } else {
            holder.cb_check.setChecked(false);
            holder.tv_use_amount.setSelected(false);
        }

        return convertView;
    }

    public void setCurrentSelectJxqId(String jxqId) {
        this.mCurrentSelectJxqId = jxqId;
    }

    /**
     * 是否清楚之前的选择
     *
     * @param isClear 是否清除
     */
    public void isClearSelected(boolean isClear) {
        mClear = isClear;
        mCurrentSelectJxqId = "-1";
    }

    class ViewHolder {
        ImageView iv_bg_hb;
        TextView tv_title;
        CheckBox cb_check;
        TextView tv_use_amount;
        TextView tv_unable;
        TextView tv_rest_amount;
        TextView tv_describe;
        TextView tv_rate;
        TextView tv_use_tip;
        TextView tv_date;
        TextView tvInvestAmountLimit;// 投资金额上限
        TextView tvInvestTimeLimit;// 投资期限上限
    }

}
