package com.lcshidai.lc.model.finance.reward;

import android.text.SpannableString;

public class InvestRewardChooseModel {

	
	public boolean isTitle=false;
	public String title;
	public RewardChooseInfo chooseInfo=new RewardChooseInfo();
	private String reward_name;
	private SpannableString reward_value;
	private String reward_time_limit;
	private String remain_money;
	private String benxi;
    private String total_rate;
	
    public String getBenxi() {
		return benxi;
	}

	public void setBenxi(String benxi) {
		this.benxi = benxi;
	}

	public String getTotal_rate() {
		return total_rate;
	}

	public void setTotal_rate(String total_rate) {
		this.total_rate = total_rate;
	}
    
	
	public boolean isTitle() {
		return isTitle;
	}
	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReward_name() {
		return reward_name;
	}
	public void setReward_name(String reward_name) {
		this.reward_name = reward_name;
	}
	public SpannableString getReward_value() {
		return reward_value;
	}
	public void setReward_value(SpannableString spannableString) {
		this.reward_value = spannableString;
	}
	public String getReward_time_limit() {
		return reward_time_limit;
	}
	public void setReward_time_limit(String reward_time_limit) {
		this.reward_time_limit = reward_time_limit;
	}
	public String getRemain_money() {
		return remain_money;
	}
	public void setRemain_money(String remain_money) {
		this.remain_money = remain_money;
	}
	
	
	
}
