package com.lcshidai.lc.model.finance;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceItemRepaymentPlayData extends BaseData {

	public  FinaceItemRepaymentPlayItem  listItem;
	public String money;
	public String sum_pri_interest;
	public String sum_yield;
	public String sum_principal;
	public String time_limit;
	public  List<FinaceItemRepaymentPlayInItem>  listFinaceItemRepaymentPlay=new ArrayList<FinaceItemRepaymentPlayInItem>();
	public FinaceItemRepaymentPlayData() {

	}

	public FinaceItemRepaymentPlayData(String me) {

	}

	
	 
	public String getMoney() {
		return money;
	}

	public FinaceItemRepaymentPlayItem getListItem() {
		return listItem;
	}
	@JsonProperty("list")
	public void setListItem(FinaceItemRepaymentPlayItem listItem) {
		this.listItem = listItem;
	}

	@JsonProperty("money")
	public void setMoney(String money) {
		this.money = money;
	}

	public String getSum_pri_interest() {
		return sum_pri_interest;
	}

	@JsonProperty("sum_pri_interest")
	public void setSum_pri_interest(String sum_pri_interest) {
		this.sum_pri_interest = sum_pri_interest;
	}

	public String getSum_yield() {
		return sum_yield;
	}

	@JsonProperty("sum_yield")
	public void setSum_yield(String sum_yield) {
		this.sum_yield = sum_yield;
	}

	public String getSum_principal() {
		return sum_principal;
	}

	@JsonProperty("sum_principal")
	public void setSum_principal(String sum_principal) {
		this.sum_principal = sum_principal;
	}

	public String getTime_limit() {
		return time_limit;
	}

	@JsonProperty("time_limit")
	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	public List<FinaceItemRepaymentPlayInItem> getListFinaceItemRepaymentPlay() {
		return listFinaceItemRepaymentPlay;
	}

	public void setListFinaceItemRepaymentPlay(
			List<FinaceItemRepaymentPlayInItem> listFinaceItemRepaymentPlay) {
		this.listFinaceItemRepaymentPlay = listFinaceItemRepaymentPlay;
	}

}
