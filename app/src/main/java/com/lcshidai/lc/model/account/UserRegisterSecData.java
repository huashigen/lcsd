package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisterSecData extends BaseData {
	private String uid;//
	private String uname;//
	private String money;//
	private String comment;//
	private String rule;//

	public String getUid() {
		return uid;
	}

	@JsonProperty("uid")
	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	@JsonProperty("uname")
	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getMoney() {
		return money;
	}

	@JsonProperty("money")
	public void setMoney(String money) {
		this.money = money;
	}

	public String getComment() {
		return comment;
	}

	@JsonProperty("comment")
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getRule() {
		return rule;
	}

	@JsonProperty("rule")
	public void setRule(String rule) {
		this.rule = rule;
	}

}
