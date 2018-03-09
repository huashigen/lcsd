package com.lcshidai.lc.ui.adapter;

import java.util.List;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.finance.FinanceHBItemData;
import com.lcshidai.lc.utils.RewardCheckUtil;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Allin on 2016/8/26.
 */
public class FinanceHBAdapter extends BaseAdapter {
    private List<FinanceHBItemData> list;
    private int mCheckedPosition = -1;
    private boolean mClear;

    public FinanceHBAdapter(List<FinanceHBItemData> list) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.finance_hb_item, null);
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final FinanceHBItemData financeHBItemData = list.get(position);

        holder.tv_title.setText(financeHBItemData.getBonus_name());
        holder.tv_use_amount.setText(String.format("-%s元", financeHBItemData.getUse_amount()));
        holder.tv_rest_amount.setText(String.format("%s/%s", financeHBItemData.getRest_amount(), financeHBItemData.getTotal_amount()));
        holder.tv_describe.setText("可用(元)");
        holder.tv_rate.setText(financeHBItemData.getRate() + "%比例红包");
        holder.tv_use_tip.setText(financeHBItemData.getUse_tip());
        holder.tv_date.setText(String.format("%s-%s", financeHBItemData.getStart_time_date().replaceAll("-", "."), financeHBItemData.getEnd_time_date().replaceAll("-", ".")));

        int ct = financeHBItemData.getCt();
        int is_usable = financeHBItemData.getIs_usable();
        if (ct <= 1 && is_usable == 1) {
            holder.iv_bg_hb.setImageResource(R.drawable.bg_hb1);
            holder.tv_use_amount.setVisibility(View.VISIBLE);
            holder.tv_unable.setVisibility(View.GONE);
        } else if (ct <= 1 && is_usable == 0) {
            holder.iv_bg_hb.setImageResource(R.drawable.bg_hb3);
            holder.tv_use_amount.setVisibility(View.GONE);
            holder.tv_unable.setVisibility(View.VISIBLE);
        } else if (ct > 1 && is_usable == 1) {
            holder.iv_bg_hb.setImageResource(R.drawable.bg_hb2);
            holder.tv_use_amount.setVisibility(View.VISIBLE);
            holder.tv_unable.setVisibility(View.GONE);
        } else if (ct > 1 && is_usable == 0) {
            holder.iv_bg_hb.setImageResource(R.drawable.bg_hb4);
            holder.tv_use_amount.setVisibility(View.GONE);
            holder.tv_unable.setVisibility(View.VISIBLE);
        }

        if (is_usable == 1) {
            holder.tv_title.setTextColor(Color.BLACK);
            holder.tv_use_tip.setTextColor(Color.BLACK);
        } else {
            holder.tv_title.setTextColor(Color.rgb(0x99, 0x99, 0x99));
            holder.tv_use_tip.setTextColor(Color.RED);
        }

        if (ct > 1) {
            holder.tv_date.setVisibility(View.INVISIBLE);
        } else {
            holder.tv_date.setVisibility(View.VISIBLE);
        }

        if (position == mCheckedPosition) {
            if (holder.cb_check.isChecked()) {
                holder.cb_check.setChecked(false);
                holder.tv_use_amount.setSelected(false);
                if (mClear) {
                    RewardCheckUtil.getInstance().resetHbMjq();
//                    RewardCheckUtil.getInstance().setReward_type("1");
                }
            } else {
                holder.cb_check.setChecked(true);
                holder.tv_use_amount.setSelected(true);
                if (mClear) {
                    RewardCheckUtil.getInstance().resetHbMjq();
                    RewardCheckUtil.getInstance().setReward_type("1");
                    RewardCheckUtil.getInstance().setBouns_rate(String.valueOf(financeHBItemData.getRate()));
                    RewardCheckUtil.getInstance().setBouns_prj_term(financeHBItemData.getPrj_term());
                    RewardCheckUtil.getInstance().setAmount(financeHBItemData.getUse_amount());
                }
            }
        } else {
            holder.cb_check.setChecked(false);
            holder.tv_use_amount.setSelected(false);
        }

        return convertView;
    }

    public void setCheckIndex(int position, boolean clear) {
        mCheckedPosition = position;
        mClear = clear;
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
    }
}
