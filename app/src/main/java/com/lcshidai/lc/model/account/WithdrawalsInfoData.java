package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalsInfoData extends BaseData{
	private String ctime; // 申请时间
	private String status; // 状态 1-处理中 2-成功 3-失败 4-取消
	private String amount; // 提现金额（元）
	private String free_money; // 资金服务费抵用券
	private String fuwu_fee; // 资金服务费
	private String tixian_fee; // 提现手续费
	private String free_tixian_times; // 是否使用免收手续费
	private String bank_name; // 银行名称
	private String sub_bank; // 支行名称
	private String out_account_no; // 银行卡号
	private String prj_recharge; // 如果有该项 请拼在资金服务费前面
	private String deal_bak; // 处理备注（失败时才有）
	private String deal_time; // 处理时间（失败时才有）

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

	public String getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFree_money() {
		return free_money;
	}

	@JsonProperty("free_money")
	public void setFree_money(String free_money) {
		this.free_money = free_money;
	}

	public String getFuwu_fee() {
		return fuwu_fee;
	}

	@JsonProperty("fuwu_fee")
	public void setFuwu_fee(String fuwu_fee) {
		this.fuwu_fee = fuwu_fee;
	}

	public String getTixian_fee() {
		return tixian_fee;
	}

	@JsonProperty("tixian_fee")
	public void setTixian_fee(String tixian_fee) {
		this.tixian_fee = tixian_fee;
	}

	public String getFree_tixian_times() {
		return free_tixian_times;
	}

	@JsonProperty("free_tixian_times")
	public void setFree_tixian_times(String free_tixian_times) {
		this.free_tixian_times = free_tixian_times;
	}

	public String getBank_name() {
		return bank_name;
	}

	@JsonProperty("bank_name")
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getSub_bank() {
		return sub_bank;
	}

	@JsonProperty("sub_bank")
	public void setSub_bank(String sub_bank) {
		this.sub_bank = sub_bank;
	}

	public String getOut_account_no() {
		return out_account_no;
	}

	@JsonProperty("out_account_no")
	public void setOut_account_no(String out_account_no) {
		this.out_account_no = out_account_no;
	}

	public String getPrj_recharge() {
		return prj_recharge;
	}

	@JsonProperty("prj_recharge")
	public void setPrj_recharge(String prj_recharge) {
		this.prj_recharge = prj_recharge;
	}

	public String getDeal_bak() {
		return deal_bak;
	}

	@JsonProperty("deal_bak")
	public void setDeal_bak(String deal_bak) {
		this.deal_bak = deal_bak;
	}

	public String getDeal_time() {
		return deal_time;
	}

	@JsonProperty("deal_time")
	public void setDeal_time(String deal_time) {
		this.deal_time = deal_time;
	}

}
