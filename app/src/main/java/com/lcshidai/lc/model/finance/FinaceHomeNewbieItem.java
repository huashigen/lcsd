package com.lcshidai.lc.model.finance;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceHomeNewbieItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;// 项目id
	private String prj_name;// 项目名称
	private String prj_type;// 项目类型
	private String prj_type_name;// 项目类型中文显示
	private String is_pre_sale;//是否预售
    private String is_auto_start;
	private String is_extend;// 是否展期
	private String year_rate;
	private String time_limit; // 期限
	private String time_limit_unit_view; // 期限单位中文显示
	private String schedule;// 融资进度-已乘以100
	private String rate_type;// 利率类型
	private String rate_type_view;// 利率类型中文
	private String rate;// 利率
	private String rate_view;// 显示的利率
	private String rate_symbol;// 显示 "%"或 "‰"
	private String repay_way;// 回款类型
	private String repay_way_view;// 回款类型中文
	private String min_bid_amount_name;// 投资起始金额显示
	private String min_bid_amount;// 投资起始金额
	private String max_bid_amount;//
	private String step_bid_amount;// 递增额度
	private String step_bid_amount_view;// 递增额度显示
    private String is_xzd;
	private String demand_amount;// 融资金额
	private String demand_amount_view;// 融资金额显示
	private String bid_status;// 状态 1-待开标 2-投资; 3-已满标; 4-待回款; 5-已回款结束;
								// 7-投标截止; 8-回款中
	private String bid_status_view;// 状态中文显示
	private String start_bid_time;// 开标时间
	private String end_bid_time;// 结标时间
	private String prj_series;// 类型,1-高富帅 2-白富美
    private String [] client_type;
    private String guarantor_num;
    private String is_rate_yellow;
    private String prj_slogan;
    
//	private String remind_id = "";// -> 提醒Id（留用户切换是否提醒时用）
//	private String is_available = "";// -> 是否提醒（1: 是, 0: 否）

	private String is_rate_top;//

	private String is_transfer;// 1 可转让 0 不可转让

	private String is_new;// 是否新客项目 1 是, 0 否
	private String transfer_id;// 转让Id, 如果为0则不是转让项目, 非0则代表是转让的项目
	private String transfer_status;

	private String activity_id;
	private String start_bid_time_diff;
	private String invest_count;// 投资笔数
	private String invest_number;// 投资人数
	private String is_limit_amount;// 1,0
	private String max_bid_amount_name;
	private String min_bid_amount_raw;//
	private String max_bid_amount_raw;//
	private String icon_only_iphone;

	private String can_bianxian;// 可兑现图标
	private String icon_url;
	private String demand_amount_value;// : "1.00",(融资金额的值)
	private String demand_amount_unit;// : "万",(融资金额的单位)



	// 如果是展期 增加以下字段
	private String uni_symbol;// 连接符
	private String time_limit_extend;// 展期时间
	private String time_limit_extend_unit;// 展期单位
	private String time_limit_comment;// 展期说明
	private String practice_money;// 理财金额度

	private String wanyuanProfit;// 每万元收益
	private String remaining_amount;

	private String max_bid_amount_view;
	private String homeFlag = "";
	private String rest_remaind_amount;
	private String pre_sale_schedule;//
	
	private FinanceTabItemExtInfo activity_ext_info;
	private FinanceTabItemRemind remind;
	
//	public String icon_activity;//追加icon
//	public String icon;//追加icon 图片地址
//	private String big_icon;//追加icon 图片地址
	private String is_x;//猜你喜欢
	private String is_sale_over;//预售是否售满
	
	public String pre_end;

	private int is_collection;
	private String prj_name_show;
	
	
	
	public String getIs_xzd() {
		return is_xzd;
	}
	@JsonProperty("is_xzd")
	public void setIs_xzd(String is_xzd) {
		this.is_xzd = is_xzd;
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
	public String getPrj_type() {
		return prj_type;
	}
	@JsonProperty("prj_type")
	public void setPrj_type(String prj_type) {
		this.prj_type = prj_type;
	}
	public String getPrj_type_name() {
		return prj_type_name;
	}
	@JsonProperty("prj_type_name")
	public void setPrj_type_name(String prj_type_name) {
		this.prj_type_name = prj_type_name;
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
	public String getRate_type() {
		return rate_type;
	}
	@JsonProperty("rate_type")
	public void setRate_type(String rate_type) {
		this.rate_type = rate_type;
	}
	public String getRate_type_view() {
		return rate_type_view;
	}
	@JsonProperty("rate_type_view")
	public void setRate_type_view(String rate_type_view) {
		this.rate_type_view = rate_type_view;
	}
	public String getRate() {
		return rate;
	}
	@JsonProperty("rate")
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getRate_view() {
		return rate_view;
	}
	@JsonProperty("rate_view")
	public void setRate_view(String rate_view) {
		this.rate_view = rate_view;
	}
	public String getRate_symbol() {
		return rate_symbol;
	}
	@JsonProperty("rate_symbol")
	public void setRate_symbol(String rate_symbol) {
		this.rate_symbol = rate_symbol;
	}
	public String getRepay_way() {
		return repay_way;
	}
	@JsonProperty("repay_way")
	public void setRepay_way(String repay_way) {
		this.repay_way = repay_way;
	}
	public String getRepay_way_view() {
		return repay_way_view;
	}
	@JsonProperty("repay_way_view")
	public void setRepay_way_view(String repay_way_view) {
		this.repay_way_view = repay_way_view;
	}
	public String getMin_bid_amount_name() {
		return min_bid_amount_name;
	}
	@JsonProperty("min_bid_amount_name")
	public void setMin_bid_amount_name(String min_bid_amount_name) {
		this.min_bid_amount_name = min_bid_amount_name;
	}
	public String getMin_bid_amount() {
		return min_bid_amount;
	}
	@JsonProperty("min_bid_amount")
	public void setMin_bid_amount(String min_bid_amount) {
		this.min_bid_amount = min_bid_amount;
	}
	public String getMax_bid_amount() {
		return max_bid_amount;
	}
	@JsonProperty("max_bid_amount")
	public void setMax_bid_amount(String max_bid_amount) {
		this.max_bid_amount = max_bid_amount;
	}
	public String getStep_bid_amount() {
		return step_bid_amount;
	}
	@JsonProperty("step_bid_amount")
	public void setStep_bid_amount(String step_bid_amount) {
		this.step_bid_amount = step_bid_amount;
	}
	public String getStep_bid_amount_view() {
		return step_bid_amount_view;
	}
	@JsonProperty("step_bid_amount_view")
	public void setStep_bid_amount_view(String step_bid_amount_view) {
		this.step_bid_amount_view = step_bid_amount_view;
	}
	public String getSchedule() {
		return schedule;
	}
	@JsonProperty("schedule")
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getDemand_amount() {
		return demand_amount;
	}
	@JsonProperty("demand_amount")
	public void setDemand_amount(String demand_amount) {
		this.demand_amount = demand_amount;
	}
	public String getDemand_amount_view() {
		return demand_amount_view;
	}
	@JsonProperty("demand_amount_view")
	public void setDemand_amount_view(String demand_amount_view) {
		this.demand_amount_view = demand_amount_view;
	}
	public String getBid_status() {
		return bid_status;
	}
	@JsonProperty("bid_status")
	public void setBid_status(String bid_status) {
		this.bid_status = bid_status;
	}
	public String getBid_status_view() {
		return bid_status_view;
	}
	@JsonProperty("bid_status_view")
	public void setBid_status_view(String bid_status_view) {
		this.bid_status_view = bid_status_view;
	}
	public String getStart_bid_time() {
		return start_bid_time;
	}
	@JsonProperty("start_bid_time")
	public void setStart_bid_time(String start_bid_time) {
		this.start_bid_time = start_bid_time;
	}
	public String getEnd_bid_time() {
		return end_bid_time;
	}
	@JsonProperty("end_bid_time")
	public void setEnd_bid_time(String end_bid_time) {
		this.end_bid_time = end_bid_time;
	}
	public String getPrj_series() {
		return prj_series;
	}
	@JsonProperty("prj_series")
	public void setPrj_series(String prj_series) {
		this.prj_series = prj_series;
	}
	public String getIs_rate_top() {
		return is_rate_top;
	}
	@JsonProperty("is_rate_top")
	public void setIs_rate_top(String is_rate_top) {
		this.is_rate_top = is_rate_top;
	}
	public String getIs_transfer() {
		return is_transfer;
	}
	@JsonProperty("is_transfer")
	public void setIs_transfer(String is_transfer) {
		this.is_transfer = is_transfer;
	}
	public String getYear_rate() {
		return year_rate;
	}
	@JsonProperty("year_rate")
	public void setYear_rate(String year_rate) {
		this.year_rate = year_rate;
	}
	public String getIs_new() {
		return is_new;
	}
	@JsonProperty("is_new")
	public void setIs_new(String is_new) {
		this.is_new = is_new;
	}
	public String getTransfer_id() {
		return transfer_id;
	}
	@JsonProperty("transfer_id")
	public void setTransfer_id(String transfer_id) {
		this.transfer_id = transfer_id;
	}
	public String getTransfer_status() {
		return transfer_status;
	}
	@JsonProperty("transfer_status")
	public void setTransfer_status(String transfer_status) {
		this.transfer_status = transfer_status;
	}
	public String getActivity_id() {
		return activity_id;
	}
	@JsonProperty("activity_id")
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getStart_bid_time_diff() {
		return start_bid_time_diff;
	}
	@JsonProperty("start_bid_time_diff")
	public void setStart_bid_time_diff(String start_bid_time_diff) {
		this.start_bid_time_diff = start_bid_time_diff;
	}
	public String getInvest_count() {
		return invest_count;
	}
	@JsonProperty("invest_count")
	public void setInvest_count(String invest_count) {
		this.invest_count = invest_count;
	}
	public String getInvest_number() {
		return invest_number;
	}
	@JsonProperty("invest_number")
	public void setInvest_number(String invest_number) {
		this.invest_number = invest_number;
	}
	public String getIs_limit_amount() {
		return is_limit_amount;
	}
	@JsonProperty("is_limit_amount")
	public void setIs_limit_amount(String is_limit_amount) {
		this.is_limit_amount = is_limit_amount;
	}
	public String getMax_bid_amount_name() {
		return max_bid_amount_name;
	}
	@JsonProperty("max_bid_amount_name")
	public void setMax_bid_amount_name(String max_bid_amount_name) {
		this.max_bid_amount_name = max_bid_amount_name;
	}
	public String getMin_bid_amount_raw() {
		return min_bid_amount_raw;
	}
	@JsonProperty("min_bid_amount_raw")
	public void setMin_bid_amount_raw(String min_bid_amount_raw) {
		this.min_bid_amount_raw = min_bid_amount_raw;
	}
	public String getMax_bid_amount_raw() {
		return max_bid_amount_raw;
	}
	@JsonProperty("max_bid_amount_raw")
	public void setMax_bid_amount_raw(String max_bid_amount_raw) {
		this.max_bid_amount_raw = max_bid_amount_raw;
	}
	public String getIcon_only_iphone() {
		return icon_only_iphone;
	}
	@JsonProperty("icon_only_iphone")
	public void setIcon_only_iphone(String icon_only_iphone) {
		this.icon_only_iphone = icon_only_iphone;
	}
	public String getCan_bianxian() {
		return can_bianxian;
	}
	@JsonProperty("can_bianxian")
	public void setCan_bianxian(String can_bianxian) {
		this.can_bianxian = can_bianxian;
	}
	public String getIcon_url() {
		return icon_url;
	}
	@JsonProperty("icon_url")
	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getDemand_amount_value() {
		return demand_amount_value;
	}
	@JsonProperty("demand_amount_value")
	public void setDemand_amount_value(String demand_amount_value) {
		this.demand_amount_value = demand_amount_value;
	}
	public String getDemand_amount_unit() {
		return demand_amount_unit;
	}
	@JsonProperty("demand_amount_unit")
	public void setDemand_amount_unit(String demand_amount_unit) {
		this.demand_amount_unit = demand_amount_unit;
	}
	public String getIs_pre_sale() {
		return is_pre_sale;
	}
	@JsonProperty("is_pre_sale")
	public void setIs_pre_sale(String is_pre_sale) {
		this.is_pre_sale = is_pre_sale;
	}
	public String getIs_extend() {
		return is_extend;
	}
	@JsonProperty("is_extend")
	public void setIs_extend(String is_extend) {
		this.is_extend = is_extend;
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
	public String getTime_limit_comment() {
		return time_limit_comment;
	}
	@JsonProperty("time_limit_comment")
	public void setTime_limit_comment(String time_limit_comment) {
		this.time_limit_comment = time_limit_comment;
	}
	public String getPractice_money() {
		return practice_money;
	}
	@JsonProperty("practice_money")
	public void setPractice_money(String practice_money) {
		this.practice_money = practice_money;
	}
	public String getWanyuanProfit() {
		return wanyuanProfit;
	}
	@JsonProperty("wanyuanProfit")
	public void setWanyuanProfit(String wanyuanProfit) {
		this.wanyuanProfit = wanyuanProfit;
	}

	public String getMax_bid_amount_view() {
		return max_bid_amount_view;
	}
	@JsonProperty("max_bid_amount_view")
	public void setMax_bid_amount_view(String max_bid_amount_view) {
		this.max_bid_amount_view = max_bid_amount_view;
	}
	public String getHomeFlag() {
		return homeFlag;
	}
	@JsonProperty("homeFlag")
	public void setHomeFlag(String homeFlag) {
		this.homeFlag = homeFlag;
	}
	public String getRest_remaind_amount() {
		return rest_remaind_amount;
	}
	@JsonProperty("rest_remaind_amount")
	public void setRest_remaind_amount(String rest_remaind_amount) {
		this.rest_remaind_amount = rest_remaind_amount;
	}
	public String getPre_sale_schedule() {
		return pre_sale_schedule;
	}
	@JsonProperty("pre_sale_schedule")
	public void setPre_sale_schedule(String pre_sale_schedule) {
		this.pre_sale_schedule = pre_sale_schedule;
	}
	public String getIs_x() {
		return is_x;
	}
	
	@JsonProperty("is_x")
	public void setIs_x(String is_x) {
		this.is_x = is_x;
	}
	public FinanceTabItemExtInfo getActivity_ext_info() {
		return activity_ext_info;
	}
	
	@JsonProperty("activity_ext_info")
	public void setActivity_ext_info(FinanceTabItemExtInfo activity_ext_info) {
		this.activity_ext_info = activity_ext_info;
	}
	public FinanceTabItemRemind getRemind() {
		return remind;
	}
	
	@JsonProperty("remind")
	public void setRemind(FinanceTabItemRemind remind) {
		this.remind = remind;
	}
	public String getIs_sale_over() {
		return is_sale_over;
	}
	
	@JsonProperty("is_sale_over")
	public void setIs_sale_over(String is_sale_over) {
		this.is_sale_over = is_sale_over;
	}
	public String getIs_auto_start() {
		return is_auto_start;
	}
	@JsonProperty("is_auto_start")
	public void setIs_auto_start(String is_auto_start) {
		this.is_auto_start = is_auto_start;
	}

	public String[] getClient_type() {
		return client_type;
	}
	@JsonProperty("client_type")
	public void setClient_type(String[] client_type) {
		this.client_type = client_type;
	}
	public String getGuarantor_num() {
		return guarantor_num;
	}
	@JsonProperty("guarantor_num")
	public void setGuarantor_num(String guarantor_num) {
		this.guarantor_num = guarantor_num;
	}
	public String getIs_rate_yellow() {
		return is_rate_yellow;
	}
	@JsonProperty("is_rate_yellow")
	public void setIs_rate_yellow(String is_rate_yellow) {
		this.is_rate_yellow = is_rate_yellow;
	}
	public String getPrj_slogan() {
		return prj_slogan;
	}
	@JsonProperty("prj_slogan")
	public void setPrj_slogan(String prj_slogan) {
		this.prj_slogan = prj_slogan;
	}
	public String getRemaining_amount() {
		return remaining_amount;
	}
	@JsonProperty("remaining_amount")
	public void setRemaining_amount(String remaining_amount) {
		this.remaining_amount = remaining_amount;
	}

	public int getIs_collection() {
		return is_collection;
	}
	@JsonProperty("is_collection")
	public void setIs_collection(int is_collection) {
		this.is_collection = is_collection;
	}

	public String getPrj_name_show() {
		return prj_name_show;
	}
	@JsonProperty("prj_name_show")
	public void setPrj_name_show(String prj_name_show) {
		this.prj_name_show = prj_name_show;
	}
}
