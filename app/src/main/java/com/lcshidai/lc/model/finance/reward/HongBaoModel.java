package com.lcshidai.lc.model.finance.reward;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class HongBaoModel {
	
	  private String amount;//": 0,
      private String reward_type;//红包type为1
      private String remain_money;//
      private String benxi;
      private String total_rate;
      
      
      public String getRemain_money() {
    	  return remain_money;
      }
      
      @JsonProperty("remain_money")
      public void setRemain_money(String remain_money) {
    	  this.remain_money = remain_money;
      }
      
	public String getAmount() {
		return amount;
	}
	
	@JsonProperty("amount")
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getReward_type() {
		return reward_type;
	}
	
	@JsonProperty("reward_type")
	public void setReward_type(String reward_type) {
		this.reward_type = reward_type;
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
