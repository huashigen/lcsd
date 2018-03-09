package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayPwdQuestionJson extends BaseJson{
	private PayPwdQuestionData data;

	public PayPwdQuestionData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(PayPwdQuestionData data) {
		this.data = data;
	}
}
