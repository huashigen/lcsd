package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyCashoutData extends BaseData {
	private String amount;//
	private String recharge_lowest_limit;//
	private String myCode;//
	private String name;//
	private String channel;//
	private String code;//

	public String getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRecharge_lowest_limit() {
		return recharge_lowest_limit;
	}

	@JsonProperty("recharge_lowest_limit")
	public void setRecharge_lowest_limit(String recharge_lowest_limit) {
		this.recharge_lowest_limit = recharge_lowest_limit;
	}

	public String getMyCode() {
		return myCode;
	}

	@JsonProperty("myCode")
	public void setMyCode(String myCode) {
		this.myCode = myCode;
	}

	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	public String getChannel() {
		return channel;
	}

	@JsonProperty("channel")
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}

}
