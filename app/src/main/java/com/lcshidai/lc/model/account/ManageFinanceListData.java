package com.lcshidai.lc.model.account;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageFinanceListData extends BaseData implements Serializable{
	private String can_cash;// : 可变现(int类型),
	private String cashing;// : 变现中(int类型),
	private String cashed;// : 已变现(int类型)
	private String status_5;// : 预约
	private String status_0;// : 全部总数
	private String status_1;// : 投资中总数
	private String status_2;// : 投资结束总数
	private String status_3;// : 待回款总数
	private String status_4;// : 已回款结束总数

	public ManageFinanceListData() {

	}

	public ManageFinanceListData(String me) {

	}
	
	public String getCan_cash() {
		return can_cash;
	}

	@JsonProperty("can_cash")
	public void setCan_cash(String can_cash) {
		this.can_cash = can_cash;
	}

	public String getCashing() {
		return cashing;
	}

	@JsonProperty("cashing")
	public void setCashing(String cashing) {
		this.cashing = cashing;
	}

	public String getCashed() {
		return cashed;
	}

	@JsonProperty("cashed")
	public void setCashed(String cashed) {
		this.cashed = cashed;
	}

	public String getStatus_5() {
		return status_5;
	}

	@JsonProperty("status_5")
	public void setStatus_5(String status_5) {
		this.status_5 = status_5;
	}

	public String getStatus_0() {
		return status_0;
	}

	@JsonProperty("status_0")
	public void setStatus_0(String status_0) {
		this.status_0 = status_0;
	}

	public String getStatus_1() {
		return status_1;
	}

	@JsonProperty("status_1")
	public void setStatus_1(String status_1) {
		this.status_1 = status_1;
	}

	public String getStatus_2() {
		return status_2;
	}

	@JsonProperty("status_2")
	public void setStatus_2(String status_2) {
		this.status_2 = status_2;
	}

	public String getStatus_3() {
		return status_3;
	}

	@JsonProperty("status_3")
	public void setStatus_3(String status_3) {
		this.status_3 = status_3;
	}

	public String getStatus_4() {
		return status_4;
	}

	@JsonProperty("status_4")
	public void setStatus_4(String status_4) {
		this.status_4 = status_4;
	}

}
