package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistHongbaoData extends BaseData {
	private String num;//
	private String mess;//

	public String getNum() {
		return num;
	}

	@JsonProperty("num")
	public void setNum(String num) {
		this.num = num;
	}

	public String getMess() {
		return mess;
	}

	@JsonProperty("mess")
	public void setMess(String mess) {
		this.mess = mess;
	}

}
