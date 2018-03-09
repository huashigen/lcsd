package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalsData extends BaseData{
	private String is_has_bank; // 是否已经绑过银行卡
	private String amount_use_view; // 用户可用余额
	private String freeCashout; // 自由提现额度
	private String free_money; // 抵用券
	private String free_tixian_times; // 免费提现次数
	private List<WithdrawalsList> data;

	
	public WithdrawalsData(){
		
	}
	
	public WithdrawalsData(String me){
		
	}
	
	public String getIs_has_bank() {
		return is_has_bank;
	}

	@JsonProperty("is_has_bank")
	public void setIs_has_bank(String is_has_bank) {
		this.is_has_bank = is_has_bank;
	}

	public String getAmount_use_view() {
		return amount_use_view;
	}

	@JsonProperty("amount_use_view")
	public void setAmount_use_view(String amount_use_view) {
		this.amount_use_view = amount_use_view;
	}

	public String getFreeCashout() {
		return freeCashout;
	}

	@JsonProperty("freeCashout")
	public void setFreeCashout(String freeCashout) {
		this.freeCashout = freeCashout;
	}

	public String getFree_money() {
		return free_money;
	}

	@JsonProperty("free_money")
	public void setFree_money(String free_money) {
		this.free_money = free_money;
	}

	public String getFree_tixian_times() {
		return free_tixian_times;
	}

	@JsonProperty("free_tixian_times")
	public void setFree_tixian_times(String free_tixian_times) {
		this.free_tixian_times = free_tixian_times;
	}

	public List<WithdrawalsList> getData() {
		return data;
	}

	@JsonProperty("list")
	public void setData(List<WithdrawalsList> data) {
		this.data = data;
	}

}
