package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceInfoData extends BaseData {
    private String prj_id;
    private String prj_no;// 项目编号
    public String prj_name;// 项目名称
    private String prj_series;// 项目系别
    private String prj_type_name;// 项目类型名称
    private String rate;// -> 利率
    private String rate_symbol;// -> 利率符号
    private String rate_type;// -> 利率类型
    private String rate_display;// -> 利率最终显示(如: 3.00‰日)
    private String time_limit;// -> 期限
    private String time_limit_unit_view;// 期限單位
    private String repay_way;// -> 回款方式
    private String addcredit;// -> 保障措施
    private String guarantor;// -> 担保人
    private String safeguard;// -> 保障性质
    private String value_date;// -> 起息日
    private String demand_amount;// -> 融资规模
    private String schedule;// -> 融资进度
    private String remaining_amount;// -> 剩余可投资金额(元)
    private String is_biding;// -> 是否投标中
    private String start_bid_time_diff;// -> 距离开标时间多少秒，如果是已开标，则为0
    private String end_bid_time_diff;// -> 距离结标时间多少秒 ，如果已结标，则为0
    private String repay_time;// -> 回款时间
    public String is_balance_less;// -> 是否余额不足, 1 是 0 否
    private String step_bid_amount;// -> 投资递增金额(用于输入键盘)
    public String bid_status_display;// 项目状态
    public String year_rate;// -> 年华利率
    private RemindData remind;//
    public String remind_id;// -> 提醒Id（留用户切换是否提醒时用） remind 子元素
    public String is_available;// -> 是否提醒（1: 是, 0: 否）remind 子元素
    public String bid_status;// 项目状态码
    private String max_bid_amount;// -> 最大投标金额
    private String min_bid_amount;// 起投金额
    // const BSTATUS_WATING = 1; // 待开标
    // const BSTATUS_BIDING = 2; // 投标中
    // const BSTATUS_FULL = 3; // 已满标
    // const BSTATUS_REPAYING = 4; // 待回款
    // const BSTATUS_REPAID = 5; // 已回款结束
    // const BSTATUS_END = 7; // 截止投标
    // const BSTATUS_REPAY_IN = 8; // 回款中
    // const BSTATUS_FAILD = 99; // 已流标
    private String prj_type;// 产品类型
    private String prj_type_display;// 产品类型显示
    private String is_transfer;//
    private String is_have_repay_plan;
    private String value_date_display;//
    private String min_bid_amount_raw;// 最低投标金额
    private String reward_money_rate;// 红包利率，没有为空字符串
    private String guarantor_num;
    private String is_pre_sale;// 是否预售
    // 如果是预售 增加以下字段
    private String pre_sale_schedule;// 预售进度
    private String rest_remaind_amount;// 剩余预售额度
    private String is_extend;// 是否展期
    // 如果是展期 增加以下字段
    private String uni_symbol;// -- 期限连接符
    private String time_limit_extend;// -- 展期时间
    private String time_limit_extend_unit;// -- 展期单位
    private String time_limit_comment;// -- 展期说明
    private String practice_money;//
    private String invest_count;//
    private String safety_guarantee_tip;// 安全保障提示语
    private String prj_business_type_name;// 业务类型显示

    private String time_limit_num;    // 期限时间
    private String time_limit_unit;    // 期限单位  天  月  年
    private String wanyuan_profit_view; // 万元收益
    private int wanyuan_profit; // 万元收益原始值
    private int is_float;    // 是否是浮动项目
    private int activity_id; // 活动id ;

    private String wanyuan_profit_total_view;

    private String can_read;
    private String can_read_msg;

    private String prj_name_show;

    public int getActivity_id() {
        return activity_id;
    }

    @JsonProperty("activity_id")
    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getIs_float() {
        return is_float;
    }

    @JsonProperty("is_float")
    public void setIs_float(int is_float) {
        this.is_float = is_float;
    }

    public String getTime_limit_num() {
        return time_limit_num;
    }

    public String getWanyuan_profit_total_view() {
        return wanyuan_profit_total_view;
    }

    @JsonProperty("wanyuan_profit_total_view")
    public void setWanyuan_profit_total_view(String wanyuan_profit_total_view) {
        this.wanyuan_profit_total_view = wanyuan_profit_total_view;
    }

    @JsonProperty("time_limit_num")
    public void setTime_limit_num(String time_limit_num) {
        this.time_limit_num = time_limit_num;
    }


    public String getTime_limit_unit() {
        return time_limit_unit;
    }

    @JsonProperty("time_limit_unit")
    public void setTime_limit_unit(String time_limit_unit) {
        this.time_limit_unit = time_limit_unit;
    }

    public String getWanyuan_profit_view() {
        return wanyuan_profit_view;
    }

    @JsonProperty("wanyuan_profit_view")
    public void setWanyuan_profit_view(String wanyuan_profit_view) {
        this.wanyuan_profit_view = wanyuan_profit_view;
    }

    public int getWanyuan_profit() {
        return wanyuan_profit;
    }

    @JsonProperty("wanyuan_profit")
    public void setWanyuan_profit(int wanyuan_profit) {
        this.wanyuan_profit = wanyuan_profit;
    }

    public FinanceInfoData() {

    }

    public FinanceInfoData(String me) {

    }

    public String getSafety_guarantee_tip() {
        return safety_guarantee_tip;
    }

    @JsonProperty("safety_guarantee_tip")
    public void setSafety_guarantee_tip(String safety_guarantee_tip) {
        this.safety_guarantee_tip = safety_guarantee_tip;
    }

    public String getPrj_business_type_name() {
        return prj_business_type_name;
    }

    @JsonProperty("prj_business_type_name")
    public void setPrj_business_type_name(String prj_business_type_name) {
        this.prj_business_type_name = prj_business_type_name;
    }

    public String getPrj_id() {
        return prj_id;
    }

    @JsonProperty("prj_id")
    public void setPrj_id(String prj_id) {
        this.prj_id = prj_id;
    }

    public String getPrj_no() {
        return prj_no;
    }

    @JsonProperty("prj_no")
    public void setPrj_no(String prj_no) {
        this.prj_no = prj_no;
    }

    public String getPrj_series() {
        return prj_series;
    }

    @JsonProperty("prj_series")
    public void setPrj_series(String prj_series) {
        this.prj_series = prj_series;
    }

    public String getPrj_name() {
        return prj_name;
    }

    @JsonProperty("prj_name")
    public void setPrj_name(String prj_name) {
        this.prj_name = prj_name;
    }

    public String getPrj_type_name() {
        return prj_type_name;
    }

    @JsonProperty("prj_type_name")
    public void setPrj_type_name(String prj_type_name) {
        this.prj_type_name = prj_type_name;
    }

    public String getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate_symbol() {
        return rate_symbol;
    }

    @JsonProperty("rate_symbol")
    public void setRate_symbol(String rate_symbol) {
        this.rate_symbol = rate_symbol;
    }

    public String getRate_type() {
        return rate_type;
    }

    @JsonProperty("rate_type")
    public void setRate_type(String rate_type) {
        this.rate_type = rate_type;
    }

    public String getRate_display() {
        return rate_display;
    }

    @JsonProperty("rate_display")
    public void setRate_display(String rate_display) {
        this.rate_display = rate_display;
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

    public String getRepay_way() {
        return repay_way;
    }

    @JsonProperty("repay_way")
    public void setRepay_way(String repay_way) {
        this.repay_way = repay_way;
    }

    public String getAddcredit() {
        return addcredit;
    }

    @JsonProperty("addcredit")
    public void setAddcredit(String addcredit) {
        this.addcredit = addcredit;
    }

    public String getGuarantor() {
        return guarantor;
    }

    @JsonProperty("guarantor")
    public void setGuarantor(String guarantor) {
        this.guarantor = guarantor;
    }

    public String getSafeguard() {
        return safeguard;
    }

    @JsonProperty("safeguard")
    public void setSafeguard(String safeguard) {
        this.safeguard = safeguard;
    }

    public String getValue_date() {
        return value_date;
    }

    @JsonProperty("value_date")
    public void setValue_date(String value_date) {
        this.value_date = value_date;
    }

    public String getDemand_amount() {
        return demand_amount;
    }

    @JsonProperty("demand_amount")
    public void setDemand_amount(String demand_amount) {
        this.demand_amount = demand_amount;
    }

    public String getSchedule() {
        return schedule;
    }

    @JsonProperty("schedule")
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRemaining_amount() {
        return remaining_amount;
    }

    @JsonProperty("remaining_amount")
    public void setRemaining_amount(String remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public String getIs_biding() {
        return is_biding;
    }

    @JsonProperty("is_biding")
    public void setIs_biding(String is_biding) {
        this.is_biding = is_biding;
    }

    public String getStart_bid_time_diff() {
        return start_bid_time_diff;
    }

    @JsonProperty("start_bid_time_diff")
    public void setStart_bid_time_diff(String start_bid_time_diff) {
        this.start_bid_time_diff = start_bid_time_diff;
    }

    public String getEnd_bid_time_diff() {
        return end_bid_time_diff;
    }

    @JsonProperty("end_bid_time_diff")
    public void setEnd_bid_time_diff(String end_bid_time_diff) {
        this.end_bid_time_diff = end_bid_time_diff;
    }

    public String getRepay_time() {
        return repay_time;
    }

    @JsonProperty("repay_time")
    public void setRepay_time(String repay_time) {
        this.repay_time = repay_time;
    }

    public String getIs_balance_less() {
        return is_balance_less;
    }

    @JsonProperty("is_balance_less")
    public void setIs_balance_less(String is_balance_less) {
        this.is_balance_less = is_balance_less;
    }

    public String getStep_bid_amount() {
        return step_bid_amount;
    }

    @JsonProperty("step_bid_amount")
    public void setStep_bid_amount(String step_bid_amount) {
        this.step_bid_amount = step_bid_amount;
    }

    public String getBid_status_display() {
        return bid_status_display;
    }

    @JsonProperty("bid_status_display")
    public void setBid_status_display(String bid_status_display) {
        this.bid_status_display = bid_status_display;
    }

    public String getYear_rate() {
        return year_rate;
    }

    @JsonProperty("year_rate")
    public void setYear_rate(String year_rate) {
        this.year_rate = year_rate;
    }

    public String getRemind_id() {
        return remind_id;
    }

    public void setRemind_id(String remind_id) {
        this.remind_id = remind_id;
    }

    public String getIs_available() {
        return is_available;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }

    public String getBid_status() {
        return bid_status;
    }

    @JsonProperty("bid_status")
    public void setBid_status(String bid_status) {
        this.bid_status = bid_status;
    }

    public String getMax_bid_amount() {
        return max_bid_amount;
    }

    @JsonProperty("max_bid_amount")
    public void setMax_bid_amount(String max_bid_amount) {
        this.max_bid_amount = max_bid_amount;
    }

    public String getMin_bid_amount() {
        return min_bid_amount;
    }

    @JsonProperty("min_bid_amount")
    public void setMin_bid_amount(String min_bid_amount) {
        this.min_bid_amount = min_bid_amount;
    }

    public String getPrj_type() {
        return prj_type;
    }

    @JsonProperty("prj_type")
    public void setPrj_type(String prj_type) {
        this.prj_type = prj_type;
    }

    public String getPrj_type_display() {
        return prj_type_display;
    }

    @JsonProperty("prj_type_display")
    public void setPrj_type_display(String prj_type_display) {
        this.prj_type_display = prj_type_display;
    }

    public String getIs_transfer() {
        return is_transfer;
    }

    @JsonProperty("is_transfer")
    public void setIs_transfer(String is_transfer) {
        this.is_transfer = is_transfer;
    }

    public String getIs_have_repay_plan() {
        return is_have_repay_plan;
    }

    @JsonProperty("is_have_repay_plan")
    public void setIs_have_repay_plan(String is_have_repay_plan) {
        this.is_have_repay_plan = is_have_repay_plan;
    }

    public String getValue_date_display() {
        return value_date_display;
    }

    @JsonProperty("value_date_display")
    public void setValue_date_display(String value_date_display) {
        this.value_date_display = value_date_display;
    }

    public String getMin_bid_amount_raw() {
        return min_bid_amount_raw;
    }

    @JsonProperty("min_bid_amount_raw")
    public void setMin_bid_amount_raw(String min_bid_amount_raw) {
        this.min_bid_amount_raw = min_bid_amount_raw;
    }

    public String getReward_money_rate() {
        return reward_money_rate;
    }

    @JsonProperty("reward_money_rate")
    public void setReward_money_rate(String reward_money_rate) {
        this.reward_money_rate = reward_money_rate;
    }

    public String getGuarantor_num() {
        return guarantor_num;
    }

    @JsonProperty("guarantor_num")
    public void setGuarantor_num(String guarantor_num) {
        this.guarantor_num = guarantor_num;
    }

    public String getIs_pre_sale() {
        return is_pre_sale;
    }

    @JsonProperty("is_pre_sale")
    public void setIs_pre_sale(String is_pre_sale) {
        this.is_pre_sale = is_pre_sale;
    }

    public String getPre_sale_schedule() {
        return pre_sale_schedule;
    }

    @JsonProperty("pre_sale_schedule")
    public void setPre_sale_schedule(String pre_sale_schedule) {
        this.pre_sale_schedule = pre_sale_schedule;
    }

    public String getRest_remaind_amount() {
        return rest_remaind_amount;
    }

    @JsonProperty("rest_remaind_amount")
    public void setRest_remaind_amount(String rest_remaind_amount) {
        this.rest_remaind_amount = rest_remaind_amount;
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

    public String getInvest_count() {
        return invest_count;
    }

    @JsonProperty("invest_count")
    public void setInvest_count(String invest_count) {
        this.invest_count = invest_count;
    }

    public RemindData getRemind() {
        return remind;
    }

    @JsonProperty("remind")
    public void setRemind(RemindData remind) {
        this.remind = remind;
    }

    public String getCan_read() {
        return can_read;
    }

    @JsonProperty("can_read")
    public void setCan_read(String can_read) {
        this.can_read = can_read;
    }

    public String getCan_read_msg() {
        return can_read_msg;
    }

    @JsonProperty("can_read_msg")
    public void setCan_read_msg(String can_read_msg) {
        this.can_read_msg = can_read_msg;
    }

    public String getPrj_name_show() {
        return prj_name_show;
    }

    @JsonProperty("prj_name_show")
    public void setPrj_name_show(String prj_name_show) {
        this.prj_name_show = prj_name_show;
    }
}
