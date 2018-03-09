package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceItemRepaymentPlayItem {
	public List<FinaceItemRepaymentPlayInItem> ListFive;
	public List<FinaceItemRepaymentPlayInItem> ListSix;
	public List<FinaceItemRepaymentPlayInItem> ListSeven;

	public FinaceItemRepaymentPlayItem() {

	}

	public FinaceItemRepaymentPlayItem(String me) {

	}

	public List<FinaceItemRepaymentPlayInItem> getListFive() {
		return ListFive;
	}

	@JsonProperty("5")
	public void setListFive(List<FinaceItemRepaymentPlayInItem> listFive) {
		ListFive = listFive;
	}

	public List<FinaceItemRepaymentPlayInItem> getListSix() {
		return ListSix;
	}

	@JsonProperty("6")
	public void setListSix(List<FinaceItemRepaymentPlayInItem> listSix) {
		ListSix = listSix;
	}

	public List<FinaceItemRepaymentPlayInItem> getListSeven() {
		return ListSeven;
	}

	@JsonProperty("7")
	public void setListSeven(List<FinaceItemRepaymentPlayInItem> listSeven) {
		ListSeven = listSeven;
	}

}
