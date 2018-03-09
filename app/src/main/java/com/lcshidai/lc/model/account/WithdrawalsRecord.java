package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalsRecord extends BaseData{
	private String id; //
	private String amount; //
	private String out_account_no; //
	private String bank; //
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

	public String getBank() {
		return bank;
	}

	@JsonProperty("bank")
	public void setBank(String bank) {
		this.bank = bank;
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
