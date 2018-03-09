package com.lcshidai.lc.model.account;

import com.lcshidai.lc.model.BaseJson;

public class GetEscrowRemindJson extends BaseJson{
	private GetEscrowRemindData data;

	public GetEscrowRemindData getData() {
		return data;
	}

	public void setData(GetEscrowRemindData data) {
		this.data = data;
	}
}
