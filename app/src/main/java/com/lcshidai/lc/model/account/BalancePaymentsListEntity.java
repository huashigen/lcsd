package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalancePaymentsListEntity extends BaseData {
	private String status; // 状态类型: 充值-1 投资-2 回款-3 提现-4 奖金-5
							// 债权交易-6（买入还是卖出可通过show_type判断） 资金服务费抵用券-7
	private String ctime; // 时间
	private String money_change; // 账户变化金额
	private int show_type; // 变化类型 1-存入 2-支出 3-冻结
	private String amount; // 当前可用余额
	private String prj_name; // 项目名称 没有为'--'
	private String record_no; // 流水号
	private String remark;
	private String money_change1;
	private String show_type1;
	private String type;

	public BalancePaymentsListEntity() {

	}

	public BalancePaymentsListEntity(String me) {

	}
	
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getCtime() {
		return ctime;
	}

	@JsonProperty("ctime")
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getMoney_change() {
		return money_change;
	}

	@JsonProperty("money_change")
	public void setMoney_change(String money_change) {
		this.money_change = money_change;
	}

	public int getShow_type() {
		return show_type;
	}

	@JsonProperty("show_type")
	public void setShow_type(int show_type) {
		this.show_type = show_type;
	}

	public String getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPrj_name() {
		return prj_name;
	}

	@JsonProperty("prj_name")
	public void setPrj_name(String prj_name) {
		this.prj_name = prj_name;
	}

	public String getRecord_no() {
		return record_no;
	}

	@JsonProperty("record_no")
	public void setRecord_no(String record_no) {
		this.record_no = record_no;
	}

	public String getRemark() {
		return remark;
	}

	@JsonProperty("remark")
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMoney_change1() {
		return money_change1;
	}

	@JsonProperty("money_change1")
	public void setMoney_change1(String money_change1) {
		this.money_change1 = money_change1;
	}

	public String getShow_type1() {
		return show_type1;
	}

	@JsonProperty("show_type1")
	public void setShow_type1(String show_type1) {
		this.show_type1 = show_type1;
	}

	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
}
