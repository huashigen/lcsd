package com.lcshidai.lc.model.account;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MobileCheckData implements Serializable {

	private int status;
	private String msg;
	private String mobile;
	private int isVCode;

	public int getVCode() {
		return isVCode;
	}

	@JsonProperty("is_vcode")
	public void setIsVCode(int idVCode) {
		this.isVCode = idVCode;
	}

	public int getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	@JsonProperty("msg")
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMobile() {
		return mobile;
	}

	@JsonProperty("mobile")
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
