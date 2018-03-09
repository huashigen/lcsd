package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBankSearchData extends BaseData {
	private String id;
	private String mName;
	private String mBankNo;
	private boolean isSelected;

	public AccountBankSearchData() {

	}

	public AccountBankSearchData(String me) {

	}

	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getmName() {
		return mName;
	}

	@JsonProperty("bank_name")
	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmBankNo() {
		return mBankNo;
	}

	@JsonProperty("bank_no")
	public void setmBankNo(String mBankNo) {
		this.mBankNo = mBankNo;
	}

	public boolean isSelected() {
		return isSelected;
	}

	@JsonProperty("isSelected")
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
