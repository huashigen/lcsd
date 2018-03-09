package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoldFinanceRecord extends BaseData {
	public String prj_name;// ："项目名称",
	public String money;// :"理财金",
	public String yield;// :"收益",
	public String muji_day;// :"募集天数",
	public String status;// :"回款状态1待回款2已回款",
	public String mstatus;// : "理财金使用状态1使用中2已使用3已过期",
	public String expect_repay_time;// :"预计回款时间",
	public String source;// ："来源类型1新用户注册2推荐用户注册3参加活动",

	public String ctime;//
	public String mtime;//
	public String id;//
	public String uid;//

	public String tyj_time_limit;
	public String year_rate;
	public String actual_repay_time;
	public String info;

	public String getPrj_name() {
		return prj_name;
	}

	@JsonProperty("prj_name")
	public void setPrj_name(String prj_name) {
		this.prj_name = prj_name;
	}

	public String getMoney() {
		return money;
	}

	@JsonProperty("money")
	public void setMoney(String money) {
		this.money = money;
	}

	public String getYield() {
		return yield;
	}

	@JsonProperty("yield")
	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getMuji_day() {
		return muji_day;
	}

	@JsonProperty("muji_day")
	public void setMuji_day(String muji_day) {
		this.muji_day = muji_day;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getMstatus() {
		return mstatus;
	}

	@JsonProperty("mstatus")
	public void setMstatus(String mstatus) {
		this.mstatus = mstatus;
	}

	public String getExpect_repay_time() {
		return expect_repay_time;
	}

	@JsonProperty("expect_repay_time")
	public void setExpect_repay_time(String expect_repay_time) {
		this.expect_repay_time = expect_repay_time;
	}

	public String getSource() {
		return source;
	}

	@JsonProperty("source")
	public void setSource(String source) {
		this.source = source;
	}

	public String getCtime() {
		return ctime;
	}

	@JsonProperty("ctime")
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getMtime() {
		return mtime;
	}

	@JsonProperty("mtime")
	public void setMtime(String mtime) {
		this.mtime = mtime;
	}

	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	@JsonProperty("uid")
	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTyj_time_limit() {
		return tyj_time_limit;
	}

	@JsonProperty("tyj_time_limit")
	public void setTyj_time_limit(String tyj_time_limit) {
		this.tyj_time_limit = tyj_time_limit;
	}

	public String getYear_rate() {
		return year_rate;
	}

	@JsonProperty("year_rate")
	public void setYear_rate(String year_rate) {
		this.year_rate = year_rate;
	}

	public String getActual_repay_time() {
		return actual_repay_time;
	}

	@JsonProperty("actual_repay_time")
	public void setActual_repay_time(String actual_repay_time) {
		this.actual_repay_time = actual_repay_time;
	}

	public String getInfo() {
		return info;
	}

	@JsonProperty("info")
	public void setInfo(String info) {
		this.info = info;
	}

}
