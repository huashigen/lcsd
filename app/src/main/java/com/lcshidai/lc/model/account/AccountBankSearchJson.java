package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBankSearchJson extends BaseJson {
	private List<AccountBankSearchData> data;

	public List<AccountBankSearchData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<AccountBankSearchData> data) {
		this.data = data;
	}

}
