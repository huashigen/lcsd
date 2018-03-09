package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalsBank extends BaseData{
	private String name; // 银行名称
	private String code; // 银行code
	private String myCode; // 银行myCode

	public WithdrawalsBank() {

	}

	public WithdrawalsBank(String me) {

	}
	
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}

	public String getMyCode() {
		return myCode;
	}

	@JsonProperty("myCode")
	public void setMyCode(String myCode) {
		this.myCode = myCode;
	}

}
