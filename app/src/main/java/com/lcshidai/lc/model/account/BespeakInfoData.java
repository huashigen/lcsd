package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakInfoData extends BaseData {
	private String ctime; //
	private String appoint_money; //
	private String appoint_rate; //
	private String appoint_time_limit; //
	private String status_show; //
	private String appoint_income_rate; //
	private String match_money; //
	private String etime;
	private String is_qyr;
	private String is_sdt;
	private String is_xyb;
	private String is_rys;
	private String status;
	private String prj_type;
	private List<BespeakInfoList> prj_list;

	public String getCtime() {
		return ctime;
	}

	@JsonProperty("ctime")
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getAppoint_money() {
		return appoint_money;
	}

	@JsonProperty("appoint_money")
	public void setAppoint_money(String appoint_money) {
		this.appoint_money = appoint_money;
	}

	public String getAppoint_rate() {
		return appoint_rate;
	}

	@JsonProperty("appoint_rate")
	public void setAppoint_rate(String appoint_rate) {
		this.appoint_rate = appoint_rate;
	}

	public String getAppoint_time_limit() {
		return appoint_time_limit;
	}

	@JsonProperty("appoint_time_limit")
	public void setAppoint_time_limit(String appoint_time_limit) {
		this.appoint_time_limit = appoint_time_limit;
	}

	public String getStatus_show() {
		return status_show;
	}

	@JsonProperty("status_show")
	public void setStatus_show(String status_show) {
		this.status_show = status_show;
	}

	public String getAppoint_income_rate() {
		return appoint_income_rate;
	}

	@JsonProperty("appoint_income_rate")
	public void setAppoint_income_rate(String appoint_income_rate) {
		this.appoint_income_rate = appoint_income_rate;
	}

	public String getMatch_money() {
		return match_money;
	}

	@JsonProperty("match_money")
	public void setMatch_money(String match_money) {
		this.match_money = match_money;
	}

	public String getEtime() {
		return etime;
	}

	@JsonProperty("etime")
	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getIs_qyr() {
		return is_qyr;
	}

	@JsonProperty("is_qyr")
	public void setIs_qyr(String is_qyr) {
		this.is_qyr = is_qyr;
	}

	public String getIs_sdt() {
		return is_sdt;
	}

	@JsonProperty("is_sdt")
	public void setIs_sdt(String is_sdt) {
		this.is_sdt = is_sdt;
	}

	public String getIs_xyb() {
		return is_xyb;
	}

	@JsonProperty("is_xyb")
	public void setIs_xyb(String is_xyb) {
		this.is_xyb = is_xyb;
	}

	public String getIs_rys() {
		return is_rys;
	}

	@JsonProperty("is_rys")
	public void setIs_rys(String is_rys) {
		this.is_rys = is_rys;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getPrj_type() {
		return prj_type;
	}

	@JsonProperty("prj_type")
	public void setPrj_type(String prj_type) {
		this.prj_type = prj_type;
	}

	public List<BespeakInfoList> getPrj_list() {
		return prj_list;
	}

	@JsonProperty("prj_list")
	public void setPrj_list(List<BespeakInfoList> prj_list) {
		this.prj_list = prj_list;
	}

}
