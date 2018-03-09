package com.lcshidai.lc.model.account;

import com.lcshidai.lc.model.BaseJson;

public class AccountHasEscrowedJson extends BaseJson{
	private AccountHasEscrowedData data;

	public AccountHasEscrowedData getData() {
		return data;
	}

	public void setData(AccountHasEscrowedData data) {
		this.data = data;
	}
}
