package com.lcshidai.lc.model.finance.reward;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ManjianTicketModel {
	
	private String amount;//": "3000.00",
    private String reward_type;//": 2,
    private String reward_id;//": "137",
    private String expire_time;//": "1970-01-01 08:33:35"
    private String reward_name;
    
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
	public String getReward_id() {
		return reward_id;
	}
	
	@JsonProperty("reward_id")
	public void setReward_id(String reward_id) {
		this.reward_id = reward_id;
	}
	public String getExpire_time() {
		return expire_time;
	}
	
	@JsonProperty("expire_time")
	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}

	public String getReward_name() {
		return reward_name;
	}

	@JsonProperty("reward_name")
	public void setReward_name(String reward_name) {
		this.reward_name = reward_name;
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
