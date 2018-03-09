package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoldFinanceYieldData extends BaseData {
	private String id;//
	private String yield;//
	private String prj_type_name;//
	private String is_extend;//
	private String time_limit;//
	private String time_limit_unit_view;//
	private String uni_symbol;//
	private String time_limit_extend;//
	private String time_limit_extend_unit;//
	private String wanyuanProfit;//
	private String year_rate;//
	private String max_bid_amount_view;//
	private String min_bid_amount_name;//

	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getYield() {
		return yield;
	}

	@JsonProperty("yield")
	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getPrj_type_name() {
		return prj_type_name;
	}

	@JsonProperty("prj_type_name")
	public void setPrj_type_name(String prj_type_name) {
		this.prj_type_name = prj_type_name;
	}

	public String getIs_extend() {
		return is_extend;
	}

	@JsonProperty("is_extend")
	public void setIs_extend(String is_extend) {
		this.is_extend = is_extend;
	}

	public String getTime_limit() {
		return time_limit;
	}

	@JsonProperty("time_limit")
	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	public String getTime_limit_unit_view() {
		return time_limit_unit_view;
	}

	@JsonProperty("time_limit_unit_view")
	public void setTime_limit_unit_view(String time_limit_unit_view) {
		this.time_limit_unit_view = time_limit_unit_view;
	}

	public String getUni_symbol() {
		return uni_symbol;
	}

	@JsonProperty("uni_symbol")
	public void setUni_symbol(String uni_symbol) {
		this.uni_symbol = uni_symbol;
	}

	public String getTime_limit_extend() {
		return time_limit_extend;
	}

	@JsonProperty("time_limit_extend")
	public void setTime_limit_extend(String time_limit_extend) {
		this.time_limit_extend = time_limit_extend;
	}

	public String getTime_limit_extend_unit() {
		return time_limit_extend_unit;
	}

	@JsonProperty("time_limit_extend_unit")
	public void setTime_limit_extend_unit(String time_limit_extend_unit) {
		this.time_limit_extend_unit = time_limit_extend_unit;
	}

	public String getWanyuanProfit() {
		return wanyuanProfit;
	}

	@JsonProperty("wanyuanProfit")
	public void setWanyuanProfit(String wanyuanProfit) {
		this.wanyuanProfit = wanyuanProfit;
	}

	public String getYear_rate() {
		return year_rate;
	}

	@JsonProperty("year_rate")
	public void setYear_rate(String year_rate) {
		this.year_rate = year_rate;
	}

	public String getMax_bid_amount_view() {
		return max_bid_amount_view;
	}

	@JsonProperty("max_bid_amount_view")
	public void setMax_bid_amount_view(String max_bid_amount_view) {
		this.max_bid_amount_view = max_bid_amount_view;
	}

	public String getMin_bid_amount_name() {
		return min_bid_amount_name;
	}

	@JsonProperty("min_bid_amount_name")
	public void setMin_bid_amount_name(String min_bid_amount_name) {
		this.min_bid_amount_name = min_bid_amount_name;
	}

}
