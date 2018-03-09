package com.lcshidai.lc.ui.adapter;

import java.util.List;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.finance.FinanceMJQItemData;
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
public class FinanceMJQAdapter extends BaseAdapter {
    private List<FinanceMJQItemData> list;
    private int mCheckedPosition = -1;
    private boolean mClear;

    public FinanceMJQAdapter(List<FinanceMJQItemData> list) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.finance_mjq_item, null);
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

        final FinanceMJQItemData financeMJQItemData = list.get(position);
        holder.tv_title.setText(String.format("%s (%s", financeMJQItemData.getDisp_name(), financeMJQItemData.getSource()));
        holder.tv_use_amount.setText(String.format("-%s元", financeMJQItemData.getAmount()));
        holder.tv_rest_amount.setText(financeMJQItemData.getRemark());
        holder.tv_describe.setText(financeMJQItemData.getUse_tip());
        holder.tv_use_tip.setText(financeMJQItemData.getSource());
        holder.tv_date.setText(String.format("%s-%s", financeMJQItemData.getCtime().replaceAll("-", "."), financeMJQItemData.getEnd_time().replaceAll("-", ".")));

        int is_usable = financeMJQItemData.getIs_usable();
        if (is_usable == 1) {
            holder.iv_bg_hb.setImageResource(R.drawable.bg_hb1);
            holder.tv_use_amount.setVisibility(View.VISIBLE);
            holder.tv_unable.setVisibility(View.GONE);
            holder.tv_title.setTextColor(Color.BLACK);
        } else if (is_usable == 0) {
            holder.iv_bg_hb.setImageResource(R.drawable.bg_hb3);
            holder.tv_use_amount.setVisibility(View.GONE);
            holder.tv_unable.setVisibility(View.VISIBLE);
            holder.tv_title.setTextColor(Color.rgb(0x99, 0x99, 0x99));
        }

        if (position == mCheckedPosition) {
            if (holder.cb_check.isChecked()) {
                holder.cb_check.setChecked(false);
                holder.tv_use_amount.setSelected(false);
                if (mClear) {
                    RewardCheckUtil.getInstance().resetHbMjq();
//                    RewardCheckUtil.getInstance().setReward_type("2");
                }
            } else {
                holder.cb_check.setChecked(true);
                holder.tv_use_amount.setSelected(true);
                if (mClear) {
                    RewardCheckUtil.getInstance().resetHbMjq();
                    RewardCheckUtil.getInstance().setReward_type("2");
                    RewardCheckUtil.getInstance().setMjq_id(financeMJQItemData.getId());
                    RewardCheckUtil.getInstance().setAmount(financeMJQItemData.getAmount());
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
