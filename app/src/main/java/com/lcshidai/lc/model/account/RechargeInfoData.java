package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RechargeInfoData extends BaseData {
	private String amount; //
	private String real_amount; //
	private String bank_name; //
	private String ctime; //
	private String ticket_no; //
	private String status; //
	private String channel;
	private String bak;
	private String deal_bak;
	private String verifytime;

	public String getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReal_amount() {
		return real_amount;
	}

	@JsonProperty("real_amount")
	public void setReal_amount(String real_amount) {
		this.real_amount = real_amount;
	}

	public String getBank_name() {
		return bank_name;
	}

	@JsonProperty("bank_name")
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getCtime() {
		return ctime;
	}

	@JsonProperty("ctime")
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getTicket_no() {
		return ticket_no;
	}

	@JsonProperty("ticket_no")
	public void setTicket_no(String ticket_no) {
		this.ticket_no = ticket_no;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getChannel() {
		return channel;
	}

	@JsonProperty("channel")
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getBak() {
		return bak;
	}

	@JsonProperty("bak")
	public void setBak(String bak) {
		this.bak = bak;
	}

	public String getDeal_bak() {
		return deal_bak;
	}

	@JsonProperty("deal_bak")
	public void setDeal_bak(String deal_bak) {
		this.deal_bak = deal_bak;
	}

	public String getVerifytime() {
		return verifytime;
	}

	@JsonProperty("verifytime")
	public void setVerifytime(String verifytime) {
		this.verifytime = verifytime;
	}

}
