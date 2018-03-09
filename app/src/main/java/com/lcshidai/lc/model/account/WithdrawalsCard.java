package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalsCard {
	private String bank_card_id; // 银行卡id
	private String account_no; // 银行卡号
	private String channel; // 充值通道
	private String code; // 银行code
	private String name; // 银行名称
	private String sub_bank; // 支行
	private String sub_bank_id; // 支行id
	private String bank_province; // 省份id
	private String bank_city; // 城市id
	private String cashoutAmount; // 可提现额度
	// String is_bankck; //银行卡是否可编辑 0-可以编辑 1-不能编辑
	private String mcashoutAmount;
	private String freeze_cashout_amount; // mcashoutAmount 和
											// freeze_cashout_amount 都为0时银行卡可编辑
	private String short_account_no;

	public WithdrawalsCard() {

	}

	public WithdrawalsCard(String me) {

	}
	
	public String getBank_card_id() {
		return bank_card_id;
	}

	@JsonProperty("id")
	public void setBank_card_id(String bank_card_id) {
		this.bank_card_id = bank_card_id;
	}

	public String getAccount_no() {
		return account_no;
	}

	@JsonProperty("account_no")
	public void setAccount_no(String account_no) {
		this.account_no = account_no;
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

	@JsonProperty("bank")
	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	@JsonProperty("bank_name")
	public void setName(String name) {
		this.name = name;
	}

	public String getSub_bank() {
		return sub_bank;
	}

	@JsonProperty("sub_bank")
	public void setSub_bank(String sub_bank) {
		this.sub_bank = sub_bank;
	}

	public String getSub_bank_id() {
		return sub_bank_id;
	}

	@JsonProperty("sub_bank_id")
	public void setSub_bank_id(String sub_bank_id) {
		this.sub_bank_id = sub_bank_id;
	}

	public String getBank_province() {
		return bank_province;
	}

	@JsonProperty("bank_province")
	public void setBank_province(String bank_province) {
		this.bank_province = bank_province;
	}

	public String getBank_city() {
		return bank_city;
	}

	@JsonProperty("bank_city")
	public void setBank_city(String bank_city) {
		this.bank_city = bank_city;
	}

	public String getCashoutAmount() {
		return cashoutAmount;
	}

	@JsonProperty("cashoutAmount")
	public void setCashoutAmount(String cashoutAmount) {
		this.cashoutAmount = cashoutAmount;
	}

	public String getMcashoutAmount() {
		return mcashoutAmount;
	}

	@JsonProperty("mcashoutAmount")
	public void setMcashoutAmount(String mcashoutAmount) {
		this.mcashoutAmount = mcashoutAmount;
	}

	public String getFreeze_cashout_amount() {
		return freeze_cashout_amount;
	}

	@JsonProperty("freeze_cashout_amount")
	public void setFreeze_cashout_amount(String freeze_cashout_amount) {
		this.freeze_cashout_amount = freeze_cashout_amount;
	}

	public String getShort_account_no() {
		return short_account_no;
	}

	@JsonProperty("short_account_no")
	public void setShort_account_no(String short_account_no) {
		this.short_account_no = short_account_no;
	}

}
