package com.lcshidai.lc.ui.adapter;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.model.finance.lcs.LcsItemData;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.widget.NumberProgressBar;

import java.util.List;

/**
 * Created by Allin on 2016/7/19.
 */
public class LicaiAdapter extends BaseAdapter {
    private List<LcsItemData> list;
    private LinearLayout.LayoutParams progressParams;
    private Shader leftShader;
    private String activeColorStr, inactiveColorStr;

    public LicaiAdapter(List<LcsItemData> list) {
        this.list = list;
        progressParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        progressParams.setMarginStart(12);
        activeColorStr = "#666666";
        inactiveColorStr = "#868686";
        int colorStart = Color.parseColor("#FFE31C");
        int colorEnd = Color.parseColor("#FF811C");
        leftShader = new LinearGradient(0, 0, 0, 80, colorStart, colorEnd, Shader.TileMode.CLAMP);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.licai_item, null);
            holder.ivPrjStatus = (ImageView) convertView.findViewById(R.id.iv_prj_status);
            holder.tvFoundTypeName = (TextView) convertView.findViewById(R.id.tv_fund_type_name);
            holder.tvFoundName = (TextView) convertView.findViewById(R.id.tv_fund_name);
            holder.tvActivity = (TextView) convertView.findViewById(R.id.iv_activity);
            holder.tvLeftValue = (TextView) convertView.findViewById(R.id.tv_left_value);
            holder.tvLeftValueUnit = (TextView) convertView.findViewById(R.id.tv_left_value_unit);
            holder.tvLeftLabel = (TextView) convertView.findViewById(R.id.tv_left_label);
            holder.tvMidValue = (TextView) convertView.findViewById(R.id.tv_mid_value);
            holder.tvMidValueUnit = (TextView) convertView.findViewById(R.id.tv_mid_value_unit);
            holder.tvMidLabel = (TextView) convertView.findViewById(R.id.tv_mid_label);
            holder.tvRightValue = (TextView) convertView.findViewById(R.id.tv_right_value);
            holder.tvRightValueUnit = (TextView) convertView.findViewById(R.id.tv_right_value_unit);
            holder.tvRightLabel = (TextView) convertView.findViewById(R.id.tv_right_label);
            holder.pbProgress = (NumberProgressBar) convertView.findViewById(R.id.pb_progress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // data bind
        LcsItemData lcsItemData = list.get(position);
        if (null != lcsItemData) {
            holder.tvFoundTypeName.setText(lcsItemData.getFund_type_str());
            holder.tvFoundName.setText(lcsItemData.getFund_name());
            if (lcsItemData.getActivity().equals("1")) {// 1的时候显示爆款图片
                holder.tvActivity.setVisibility(View.VISIBLE);
            } else {
                holder.tvActivity.setVisibility(View.INVISIBLE);
            }
            if (!CommonUtil.isNullOrEmpty(lcsItemData.getCollect_process())) {
                try {
                    int progress = Integer.parseInt(lcsItemData.getCollect_process());
                    holder.pbProgress.setProgress(progress);
                    if (progress == 0 || progress == 100) {
                        holder.pbProgress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            dealWithLabelsAndValues(lcsItemData, holder);
            dealWithFundStatus(lcsItemData.getFund_status(), holder);
        }

        return convertView;
    }

    private void dealWithLabelsAndValues(LcsItemData lcsItemData, ViewHolder holder) {
        String fundType = lcsItemData.getFund_type();
        String leftLabel = "", midLabel = "投资期限", rightLabel = "";
        int leftValueTextSize = 16, midValueTextSize = 16, rightValueTextSize = 16;
        // 1：固收，2：证券基金，3：股权基金，4：信托产品，5：债权基金，6：资管计划
        if (fundType.equals("1")) {
            dealWithLeftValue(lcsItemData.getAnnual_rate(), holder);
            leftLabel = "基准收益";
            rightLabel = "认购起点";
            leftValueTextSize = 22;
            rightValueTextSize = 18;
            holder.tvRightValue.setText(lcsItemData.getMin_buy_amount());
            holder.tvRightValueUnit.setVisibility(View.VISIBLE);
        } else if (fundType.equals("2")) {
            dealWithLeftValue(lcsItemData.getNet_worth(), holder);
            leftLabel = "最新净值";
            rightLabel = "认购起点";
            leftValueTextSize = 22;
            rightValueTextSize = 18;
            holder.tvRightValue.setText(lcsItemData.getMin_buy_amount());
            holder.tvRightValueUnit.setVisibility(View.VISIBLE);
        } else if (fundType.equals("3")) {
            holder.tvLeftValue.setText(lcsItemData.getFund_invest_at());
            leftLabel = "资金投向";
            rightLabel = "认购起点";
            leftValueTextSize = 16;
            rightValueTextSize = 16;
            holder.tvLeftValueUnit.setVisibility(View.GONE);
            holder.tvRightValue.setText(lcsItemData.getMin_buy_amount());
            holder.tvRightValueUnit.setVisibility(View.VISIBLE);
        } else if (fundType.equals("4") || fundType.equals("6")) {
            dealWithLeftValue(lcsItemData.getAnnual_rate(), holder);
            leftLabel = "最高预期收益";
            rightLabel = "投资领域";
            leftValueTextSize = 22;
            rightValueTextSize = 16;
            holder.tvRightValue.setText(lcsItemData.getInvest_field_str());
            holder.tvRightValueUnit.setVisibility(View.GONE);
        } else if (fundType.equals("5")) {
            dealWithLeftValue(lcsItemData.getAnnual_rate(), holder);
            leftLabel = "基准收益";
            rightLabel = "基金类型";
            leftValueTextSize = 22;
            rightValueTextSize = 16;
            holder.tvRightValue.setText(lcsItemData.getItem_fund_type_str());
            holder.tvRightValueUnit.setVisibility(View.GONE);
        }
        holder.tvLeftLabel.setText(leftLabel);
        holder.tvLeftValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, leftValueTextSize);
        holder.tvMidLabel.setText(midLabel);
        holder.tvMidValue.setTextSize(midValueTextSize);
        dealWithMidValue(lcsItemData.getTime_limit(), holder);
        holder.tvRightLabel.setText(rightLabel);
        holder.tvRightValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, rightValueTextSize);
    }

    /**
     * 左值处理
     *
     * @param annualRate 年化收益、最新净值、基准收益等
     * @param holder     holder
     */
    private void dealWithLeftValue(String annualRate, ViewHolder holder) {
        if (!CommonUtil.isNullOrEmpty(annualRate)) {
            if (CommonUtil.isPercentageNum(annualRate)) {
                int index = annualRate.indexOf("%");
                if (index != -1) {
                    String annual_rate = annualRate.substring(0, index);
                    holder.tvLeftValue.setText(annual_rate);
                    holder.tvLeftValueUnit.setVisibility(View.VISIBLE);
                } else {
                    holder.tvLeftValue.setText(annualRate);
                    holder.tvLeftValueUnit.setVisibility(View.GONE);
                }
            } else if (CommonUtil.isNumeric(annualRate)) {
                holder.tvLeftValue.setText(annualRate);
                holder.tvLeftValueUnit.setVisibility(View.VISIBLE);
            } else if (annualRate.contains("%")) {
                int index = annualRate.indexOf("%");
                if (index != -1) {
                    String annual_rate = annualRate.substring(0, index);
                    holder.tvLeftValue.setText(annual_rate);
                    holder.tvLeftValueUnit.setVisibility(View.VISIBLE);
                }
            } else {
                holder.tvLeftValue.setText(annualRate);
                holder.tvLeftValueUnit.setVisibility(View.GONE);
            }
        } else {
            holder.tvLeftValue.setText(annualRate);
            holder.tvLeftValueUnit.setVisibility(View.GONE);
        }
    }

    /**
     * 中值处理
     *
     * @param timeLimit 认购期限
     * @param holder    holder
     */
    private void dealWithMidValue(String timeLimit, ViewHolder holder) {
        if (!CommonUtil.isNullOrEmpty(timeLimit)) {
            int index = timeLimit.indexOf("个");
            if (index != -1) {
                String time_limit = timeLimit.substring(0, index);
                holder.tvMidValue.setText(time_limit);
                holder.tvMidValueUnit.setVisibility(View.VISIBLE);
            } else {
                holder.tvMidValue.setText(timeLimit);
                holder.tvMidValueUnit.setVisibility(View.GONE);
            }
        } else {
            holder.tvMidValue.setText(timeLimit);
            holder.tvMidValueUnit.setVisibility(View.GONE);
        }
    }

    /**
     * 基金状态处理
     *
     * @param fund_status fund_status 1：预热，2：募集中，3：首发募集中，4：开放募集中，5：封闭运行中，6：已结束，7：已兑付
     * @param holder      holder
     */
    private void dealWithFundStatus(String fund_status, ViewHolder holder) {
        if (fund_status.equals("1") || fund_status.equals("2") || fund_status.equals("3") || fund_status.equals("4")) {
            holder.tvMidValue.setTextColor(Color.parseColor(activeColorStr));
            holder.tvRightValue.setTextColor(Color.parseColor(activeColorStr));
            holder.tvLeftValue.getPaint().setShader(leftShader);
            holder.tvLeftValueUnit.getPaint().setShader(leftShader);
        } else {
            holder.tvLeftValue.setTextColor(Color.parseColor(inactiveColorStr));
            holder.tvLeftValueUnit.setTextColor(Color.parseColor(inactiveColorStr));
            holder.tvMidValue.setTextColor(Color.parseColor(inactiveColorStr));
            holder.tvMidValueUnit.setTextColor(Color.parseColor(inactiveColorStr));
            holder.tvRightValue.setTextColor(Color.parseColor(inactiveColorStr));
            holder.tvRightValueUnit.setTextColor(Color.parseColor(inactiveColorStr));
            holder.tvLeftValue.getPaint().setShader(null);
            holder.tvLeftValueUnit.getPaint().setShader(null);
        }
        switch (Integer.valueOf(fund_status)) {
            case 1:
                holder.ivPrjStatus.setImageResource(R.drawable.icon_status_fund_hot);
                holder.pbProgress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Visible);
                break;
            case 2:
                holder.ivPrjStatus.setImageResource(R.drawable.icon_status_fund_collecting);
                holder.pbProgress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Visible);
                break;
            case 3:
                holder.ivPrjStatus.setImageResource(R.drawable.icon_status_fund_collecting);
                holder.pbProgress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Visible);
                break;
            case 4:
                holder.ivPrjStatus.setImageResource(R.drawable.icon_status_fund_collecting);
                holder.pbProgress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Visible);
                break;
            case 5:
                holder.ivPrjStatus.setImageResource(R.drawable.icon_status_fund_close);
                holder.pbProgress.setProgress(0);
                holder.pbProgress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
                break;
            case 6:
                holder.ivPrjStatus.setImageResource(R.drawable.icon_status_fund_end);
                holder.pbProgress.setProgress(0);
                holder.pbProgress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
                break;
            case 7:
                holder.ivPrjStatus.setImageResource(R.drawable.icon_status_fund_change);
                holder.pbProgress.setProgress(0);
                holder.pbProgress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
                break;
        }
    }

    class ViewHolder {
        ImageView ivPrjStatus;              // 项目状态（开放中、在售
        TextView tvFoundTypeName;           // 项目类型icon
        TextView tvFoundName;               // 项目类型名称
        TextView tvActivity;                // 活动图标（如爆款）
        TextView tvLeftValue;               // 左值
        TextView tvLeftValueUnit;           // 左值单位
        TextView tvLeftLabel;               // 左值label
        TextView tvMidValue;                // 中间值
        TextView tvMidValueUnit;            // 中间值单位
        TextView tvMidLabel;                // 中间值label
        TextView tvRightValue;              // 右值
        TextView tvRightValueUnit;          // 右值单位
        TextView tvRightLabel;              // 右值label
        NumberProgressBar pbProgress;       // 进度条显示
    }

}
