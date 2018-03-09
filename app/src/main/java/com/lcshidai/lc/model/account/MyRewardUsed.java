package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyRewardUsed extends BaseData {
	private String id;
	private String prj_name; 
	private String uid;
	private String money; 
	private String yield; 
	private String muji_day; 
	private String year_rate; 
	private String tyj_expire_time; 
	private String tyj_time_limit; 
	private String actual_repay_time; 
	private String expect_repay_time;
	private String status;
	private String mstatus;
	private String tstatus;
	private String ctime;
	private String mtime;
	private String from_uid;
	private String source;
	private String uname;
	private String info;

	public MyRewardUsed() {

	}

	public MyRewardUsed(String me) {

	}
	
	public String getId() {
		return id;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPrj_name() {
		return prj_name;
	}
	@JsonProperty("prj_name")
	public void setPrj_name(String prj_name) {
		this.prj_name = prj_name;
	}

	public String getUid() {
		return uid;
	}
	@JsonProperty("uid")
	public void setUid(String uid) {
		this.uid = uid;
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

	public String getYear_rate() {
		return year_rate;
	}
	@JsonProperty("year_rate")
	public void setYear_rate(String year_rate) {
		this.year_rate = year_rate;
	}

	public String getTyj_expire_time() {
		return tyj_expire_time;
	}
	@JsonProperty("tyj_expire_time")
	public void setTyj_expire_time(String tyj_expire_time) {
		this.tyj_expire_time = tyj_expire_time;
	}

	public String getTyj_time_limit() {
		return tyj_time_limit;
	}
	@JsonProperty("tyj_time_limit")
	public void setTyj_time_limit(String tyj_time_limit) {
		this.tyj_time_limit = tyj_time_limit;
	}

	public String getActual_repay_time() {
		return actual_repay_time;
	}
	@JsonProperty("actual_repay_time")
	public void setActual_repay_time(String actual_repay_time) {
		this.actual_repay_time = actual_repay_time;
	}

	public String getExpect_repay_time() {
		return expect_repay_time;
	}
	@JsonProperty("expect_repay_time")
	public void setExpect_repay_time(String expect_repay_time) {
		this.expect_repay_time = expect_repay_time;
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

	public String getTstatus() {
		return tstatus;
	}
	@JsonProperty("tstatus")
	public void setTstatus(String tstatus) {
		this.tstatus = tstatus;
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

	public String getFrom_uid() {
		return from_uid;
	}
	@JsonProperty("from_uid")
	public void setFrom_uid(String from_uid) {
		this.from_uid = from_uid;
	}

	public String getSource() {
		return source;
	}
	@JsonProperty("source")
	public void setSource(String source) {
		this.source = source;
	}

	public String getUname() {
		return uname;
	}
	@JsonProperty("uname")
	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getInfo() {
		return info;
	}
	@JsonProperty("info")
	public void setInfo(String info) {
		this.info = info;
	}
	
	

}
