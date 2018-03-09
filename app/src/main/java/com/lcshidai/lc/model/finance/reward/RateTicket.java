package com.lcshidai.lc.model.finance.reward;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RateTicket {
	
	 private String id;//": "139",
     private String rate;//": "12.000000",
     private String expire_date;//": "2015-10-13 11:11:56",
     private String reward_type;//": 3
     private String remain_money;
     private String reward_name;//
     private String amount;
     private String benxi;
     private String total_rate;
     
     
	public String getId() {
		return id;
	}
	
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	public String getRate() {
		return rate;
	}
	
	@JsonProperty("rate")
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getExpire_date() {
		return expire_date;
	}
	
	@JsonProperty("expire_date")
	public void setExpire_date(String expire_date) {
		this.expire_date = expire_date;
	}
	public String getReward_type() {
		return reward_type;
	}
	
	@JsonProperty("reward_type")
	public void setReward_type(String reward_type) {
		this.reward_type = reward_type;
	}

	public String getRemain_money() {
		return remain_money;
	}

	@JsonProperty("remain_money")
	public void setRemain_money(String remain_money) {
		this.remain_money = remain_money;
	}

	public String getReward_name() {
		return reward_name;
	}

	@JsonProperty("reward_name")
	public void setReward_name(String reward_name) {
		this.reward_name = reward_name;
	}

	public String getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBenxi() {
		return benxi;
	}

	@JsonProperty("benxi")
	public void setBenxi(String benxi) {
		this.benxi = benxi;
	}

	public String getTotal_rate() {
		return total_rate;
	}

	@JsonProperty("total_rate")
	public void setTotal_rate(String total_rate) {
		this.total_rate = total_rate;
	}

	
     
     
}
