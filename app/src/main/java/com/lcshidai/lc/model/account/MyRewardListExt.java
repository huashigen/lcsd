package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyRewardListExt extends BaseData {
	private String rule_id; // 规则ID，查看规则的时候要传递的参数
	private String prj_name; // 项目名称，type=2的时候才有的
	private String prj_id; // 项目ID；
	private String bonus_type_name; // 红包的类型名称

	public MyRewardListExt() {

	}

	public MyRewardListExt(String me) {

	}

	public String getRule_id() {
		return rule_id;
	}

	@JsonProperty("rule_id")
	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}

	public String getPrj_name() {
		return prj_name;
	}

	@JsonProperty("prj_name")
	public void setPrj_name(String prj_name) {
		this.prj_name = prj_name;
	}

	public String getPrj_id() {
		return prj_id;
	}

	@JsonProperty("prj_id")
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}

	public String getBonus_type_name() {
		return bonus_type_name;
	}

	@JsonProperty("bonus_type_name")
	public void setBonus_type_name(String bonus_type_name) {
		this.bonus_type_name = bonus_type_name;
	}

}
