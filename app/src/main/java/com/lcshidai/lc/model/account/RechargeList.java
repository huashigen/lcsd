package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RechargeList extends BaseData {
	private String id; //
	private String amount; //
	private String out_account_no; //
	private String sub_bank; //
	private String ctime; //
	private String status; //

	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOut_account_no() {
		return out_account_no;
	}

	@JsonProperty("out_account_no")
	public void setOut_account_no(String out_account_no) {
		this.out_account_no = out_account_no;
	}

	public String getSub_bank() {
		return sub_bank;
	}

	@JsonProperty("sub_bank")
	public void setSub_bank(String sub_bank) {
		this.sub_bank = sub_bank;
	}

	public String getCtime() {
		return ctime;
	}

	@JsonProperty("ctime")
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}
}
