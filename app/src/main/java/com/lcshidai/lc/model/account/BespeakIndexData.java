package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakIndexData extends BaseData {
	private int is_has_appoint; //
	private String appoint_success_num; //
	private String appoint_success_amount; //
	private String min_avg_rate; //
	private String max_avg_rate; //
	private String time_limit_min; //
	private String time_limit_max; //
	private String remaining_amount; //
	private String succ_count; //
	private String prj_type; //
	private String succ_amount; //
	private String uid_count; //
	private String is_limit_apply;
	private BespeakIndexAppointRecord appoint_record;

	public BespeakIndexData() {

	}

	public BespeakIndexData(String me) {

	}

	public int getIs_has_appoint() {
		return is_has_appoint;
	}

	@JsonProperty("is_has_appoint")
	public void setIs_has_appoint(int is_has_appoint) {
		this.is_has_appoint = is_has_appoint;
	}

	public String getAppoint_success_num() {
		return appoint_success_num;
	}

	@JsonProperty("appoint_success_num")
	public void setAppoint_success_num(String appoint_success_num) {
		this.appoint_success_num = appoint_success_num;
	}

	public String getAppoint_success_amount() {
		return appoint_success_amount;
	}

	@JsonProperty("appoint_success_amount")
	public void setAppoint_success_amount(String appoint_success_amount) {
		this.appoint_success_amount = appoint_success_amount;
	}

	public String getMin_avg_rate() {
		return min_avg_rate;
	}

	@JsonProperty("min_avg_rate")
	public void setMin_avg_rate(String min_avg_rate) {
		this.min_avg_rate = min_avg_rate;
	}

	public String getMax_avg_rate() {
		return max_avg_rate;
	}

	@JsonProperty("max_avg_rate")
	public void setMax_avg_rate(String max_avg_rate) {
		this.max_avg_rate = max_avg_rate;
	}

	public String getTime_limit_min() {
		return time_limit_min;
	}

	@JsonProperty("time_limit_min")
	public void setTime_limit_min(String time_limit_min) {
		this.time_limit_min = time_limit_min;
	}

	public String getTime_limit_max() {
		return time_limit_max;
	}

	@JsonProperty("time_limit_max")
	public void setTime_limit_max(String time_limit_max) {
		this.time_limit_max = time_limit_max;
	}

	public String getRemaining_amount() {
		return remaining_amount;
	}

	@JsonProperty("remaining_amount")
	public void setRemaining_amount(String remaining_amount) {
		this.remaining_amount = remaining_amount;
	}

	public String getSucc_count() {
		return succ_count;
	}

	@JsonProperty("succ_count")
	public void setSucc_count(String succ_count) {
		this.succ_count = succ_count;
	}

	public String getPrj_type() {
		return prj_type;
	}

	@JsonProperty("prj_type")
	public void setPrj_type(String prj_type) {
		this.prj_type = prj_type;
	}

	public String getSucc_amount() {
		return succ_amount;
	}

	@JsonProperty("succ_amount")
	public void setSucc_amount(String succ_amount) {
		this.succ_amount = succ_amount;
	}

	public String getUid_count() {
		return uid_count;
	}

	@JsonProperty("uid_count")
	public void setUid_count(String uid_count) {
		this.uid_count = uid_count;
	}

	public BespeakIndexAppointRecord getAppoint_record() {
		return appoint_record;
	}

	@JsonProperty("appoint_record")
	public void setAppoint_record(BespeakIndexAppointRecord appoint_record) {
		this.appoint_record = appoint_record;
	}

	public String getIs_limit_apply() {
		return is_limit_apply;
	}

	@JsonProperty("is_limit_apply")
	public void setIs_limit_apply(String is_limit_apply) {
		this.is_limit_apply = is_limit_apply;
	}

}
