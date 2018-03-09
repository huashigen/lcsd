package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageFinanceListInfoItem {
	private String icon_activity;// 是否显示活动图标
	private String can_bianxian; // 是否显示变现的图标
	private String order_mtime;// "订单成交日期或者发布日期",
	private String prj_id;// "项目ID",
	private String prj_type;// "项目类型",
	private String prj_series;// 项目系别
	private String rate;// "年利率",
	private String curr_time_limit;// "期限",
	private String repay_way_view;// "回款方式",
	private String left_cash_times;// 可变现次数,
	private String expect_repay_time;// "下一个回款日",
	private String prj_name;// "项目名称",
	private String can_money;// "可变现金额(接口已格式化)"

	private String is_view;// 合同是否可点击(0->不可点击 1->可点击),
	private String plan_cash_money;// "变现金额",
	private String real_cash_money;// "实际变现金额"
	private String type;
	private ManageFinanceListInfo protcol_url;// 合同信息
	private String prj_order_id;//
	private String is_have_repayplan;//

	private String income;// : "收益额度，注意只在已变现中显示",
	private String schedule;// 投资进度
	private String fid;

	private String profit_show;// ": "0.54元",
	private String status_show;// ": "预约投资中", 预约项目处理状态
	private String buy_time_show;// ": "-", 成交时间
	private String server_protocol;// ": "/Index/Protocol/view?id=47", 服务协议链接
	private String rate_show;// ": "10.00%",
	private String status;// ": "1", 1-预售中 2 成功 3 失败
	private String pre_protocol;// ": "/Index/Protocol/view?id=436&prj_id=17086",
								// 借款合同 链接
	private String time_limit_show;// ": "2天", 期限
	private String protocol_id;// ": "436",
	private String id;// ": "17086",
	private String start_bid_time;// ": "1428993000",
	private String oid;// ": "6",
	private String money_show;// ": "1,000.00元", 成交金额
	private String last_repay_date;// ": "1429113600",
	private String ctime_show;// ": "2015-04-14" 预约时间

	private String order_id;// 订单Id(用于转让或查看合同时使用)
	private String date_deal;// 成交日期
	private String date_next_repayment;// 下一回款日(bid_status=5的时候就是最后回款日)

	private String prj_no;// 项目编号
	private String prj_type_display;// 项目类型
	private String repay_way_display;// 回款方式
	private String money;// 投资金额
	private String hongbao;// 红包
	private String reward_type;// 类型
	private String reward_type_tips;// 奖励
	private String incoming;// 本息收益
	private String bid_status;// 投标状态码
	private String bid_status_display;// 状态显示
	private String repay_percent;// 投资进度
	private String is_transfer_active;// 是否可立即转让

	private String icon_limit_amount;// 是否显示限额图标
	private String icon_transfer;// 是否显示转让图标
	private String icon_can_transfer;// 是否显示可转让图标
	private String icon_new;// 是否显示新客图标
	private String is_have_contract;// 是否显示合同
	private String icon_bianxian; // 是否可以立即变现
	private String transfer_id;// 转让Id
	private String expire_time_limit;//
	private String icon_only_iphone;
	private String is_pre_sale;// - 是否预售
	private String benxi;// - 本息
	private String rewardMoney;// - 奖励
    private String canInvest;    private String prj_business_type_name;	
	private ManageFinanceListInfoItemExta ext;

	private String time_limit_view;// ": "2天", 期限
	private String is_float;//1 是超赚
	private String worth_view;//当前净值
	private String last_worth_view ; //  最新净值
	private String reward_money_rate ; // 包含奖励率

	private String jxq_rate_view;		//投资记录增加加息券利率
	
	
	
	
	public String getReward_money_rate() {
		return reward_money_rate;
	}
	@JsonProperty("reward_money_rate")
	public void setReward_money_rate(String reward_money_rate) {
		this.reward_money_rate = reward_money_rate;
	}
	public String getLast_worth_view() {
		return last_worth_view;
	}
	@JsonProperty("last_worth_view")
	public void setLast_worth_view(String last_worth_view) {
		this.last_worth_view = last_worth_view;
	}

	public String getTime_limit_view() {
		return time_limit_view;
	}

	@JsonProperty("time_limit_view")
	public void setTime_limit_view(String time_limit_view) {
		this.time_limit_view = time_limit_view;
	}



	public ManageFinanceListInfoItemExta getExt() {
		return ext;
	}

	
	
	public String getPrj_business_type_name() {
		return prj_business_type_name;
	}


	@JsonProperty("prj_business_type_name")
	public void setPrj_business_type_name(String prj_business_type_name) {
		this.prj_business_type_name = prj_business_type_name;
	}



	@JsonProperty("activity_ext_info")
	public void setExt(ManageFinanceListInfoItemExta ext) {
		this.ext = ext;
	}

	public String getIcon_activity() {
		return icon_activity;
	}

	@JsonProperty("icon_activity")
	public void setIcon_activity(String icon_activity) {
		this.icon_activity = icon_activity;
	}

	public String getCan_bianxian() {
		return can_bianxian;
	}

	@JsonProperty("can_bianxian")
	public void setCan_bianxian(String can_bianxian) {
		this.can_bianxian = can_bianxian;
	}

	public String getOrder_mtime() {
		return order_mtime;
	}

	@JsonProperty("order_mtime")
	public void setOrder_mtime(String order_mtime) {
		this.order_mtime = order_mtime;
	}

	public String getPrj_id() {
		return prj_id;
	}

	@JsonProperty("prj_id")
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}

	public String getPrj_type() {
		return prj_type;
	}

	@JsonProperty("prj_type")
	public void setPrj_type(String prj_type) {
		this.prj_type = prj_type;
	}

	public String getPrj_series() {
		return prj_series;
	}

	@JsonProperty("prj_series")
	public void setPrj_series(String prj_series) {
		this.prj_series = prj_series;
	}

	public String getRate() {
		return rate;
	}

	@JsonProperty("rate")
	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getCurr_time_limit() {
		return curr_time_limit;
	}

	@JsonProperty("curr_time_limit")
	public void setCurr_time_limit(String curr_time_limit) {
		this.curr_time_limit = curr_time_limit;
	}

	public String getRepay_way_view() {
		return repay_way_view;
	}

	@JsonProperty("repay_way_view")
	public void setRepay_way_view(String repay_way_view) {
		this.repay_way_view = repay_way_view;
	}

	public String getLeft_cash_times() {
		return left_cash_times;
	}

	@JsonProperty("left_cash_times")
	public void setLeft_cash_times(String left_cash_times) {
		this.left_cash_times = left_cash_times;
	}

	public String getExpect_repay_time() {
		return expect_repay_time;
	}

	@JsonProperty("expect_repay_time")
	public void setExpect_repay_time(String expect_repay_time) {
		this.expect_repay_time = expect_repay_time;
	}

	public String getPrj_name() {
		return prj_name;
	}

	@JsonProperty("prj_name")
	public void setPrj_name(String prj_name) {
		this.prj_name = prj_name;
	}

	public String getCan_money() {
		return can_money;
	}

	@JsonProperty("can_money")
	public void setCan_money(String can_money) {
		this.can_money = can_money;
	}

	public String getIs_view() {
		return is_view;
	}

	@JsonProperty("is_view")
	public void setIs_view(String is_view) {
		this.is_view = is_view;
	}

	public String getPlan_cash_money() {
		return plan_cash_money;
	}

	@JsonProperty("plan_cash_money")
	public void setPlan_cash_money(String plan_cash_money) {
		this.plan_cash_money = plan_cash_money;
	}

	public String getReal_cash_money() {
		return real_cash_money;
	}

	@JsonProperty("real_cash_money")
	public void setReal_cash_money(String real_cash_money) {
		this.real_cash_money = real_cash_money;
	}

	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	public ManageFinanceListInfo getProtcol_url() {
		return protcol_url;
	}

	@JsonProperty("protcol_url")
	public void setProtcol_url(ManageFinanceListInfo protcol_url) {
		this.protcol_url = protcol_url;
	}

	public String getPrj_order_id() {
		return prj_order_id;
	}

	@JsonProperty("prj_order_id")
	public void setPrj_order_id(String prj_order_id) {
		this.prj_order_id = prj_order_id;
	}

	public String getIs_have_repayplan() {
		return is_have_repayplan;
	}

	@JsonProperty("is_have_repayplan")
	public void setIs_have_repayplan(String is_have_repayplan) {
		this.is_have_repayplan = is_have_repayplan;
	}

	public String getIncome() {
		return income;
	}

	@JsonProperty("income")
	public void setIncome(String income) {
		this.income = income;
	}

	public String getSchedule() {
		return schedule;
	}

	@JsonProperty("schedule")
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getFid() {
		return fid;
	}

	@JsonProperty("fid")
	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getProfit_show() {
		return profit_show;
	}

	@JsonProperty("profit_show")
	public void setProfit_show(String profit_show) {
		this.profit_show = profit_show;
	}

	public String getStatus_show() {
		return status_show;
	}

	@JsonProperty("status_show")
	public void setStatus_show(String status_show) {
		this.status_show = status_show;
	}

	public String getBuy_time_show() {
		return buy_time_show;
	}

	@JsonProperty("buy_time_show")
	public void setBuy_time_show(String buy_time_show) {
		this.buy_time_show = buy_time_show;
	}

	public String getServer_protocol() {
		return server_protocol;
	}

	@JsonProperty("server_protocol")
	public void setServer_protocol(String server_protocol) {
		this.server_protocol = server_protocol;
	}

	public String getRate_show() {
		return rate_show;
	}

	@JsonProperty("rate_show")
	public void setRate_show(String rate_show) {
		this.rate_show = rate_show;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getPre_protocol() {
		return pre_protocol;
	}

	@JsonProperty("pre_protocol")
	public void setPre_protocol(String pre_protocol) {
		this.pre_protocol = pre_protocol;
	}

	public String getTime_limit_show() {
		return time_limit_show;
	}

	@JsonProperty("time_limit_show")
	public void setTime_limit_show(String time_limit_show) {
		this.time_limit_show = time_limit_show;
	}

	public String getProtocol_id() {
		return protocol_id;
	}

	@JsonProperty("protocol_id")
	public void setProtocol_id(String protocol_id) {
		this.protocol_id = protocol_id;
	}

	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getStart_bid_time() {
		return start_bid_time;
	}

	@JsonProperty("start_bid_time")
	public void setStart_bid_time(String start_bid_time) {
		this.start_bid_time = start_bid_time;
	}

	public String getOid() {
		return oid;
	}

	@JsonProperty("oid")
	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getMoney_show() {
		return money_show;
	}

	@JsonProperty("money_show")
	public void setMoney_show(String money_show) {
		this.money_show = money_show;
	}

	public String getLast_repay_date() {
		return last_repay_date;
	}

	@JsonProperty("last_repay_date")
	public void setLast_repay_date(String last_repay_date) {
		this.last_repay_date = last_repay_date;
	}

	public String getCtime_show() {
		return ctime_show;
	}

	@JsonProperty("ctime_show")
	public void setCtime_show(String ctime_show) {
		this.ctime_show = ctime_show;
	}

	public String getOrder_id() {
		return order_id;
	}

	@JsonProperty("order_id")
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getDate_deal() {
		return date_deal;
	}

	@JsonProperty("date_deal")
	public void setDate_deal(String date_deal) {
		this.date_deal = date_deal;
	}

	public String getDate_next_repayment() {
		return date_next_repayment;
	}

	@JsonProperty("date_next_repayment")
	public void setDate_next_repayment(String date_next_repayment) {
		this.date_next_repayment = date_next_repayment;
	}

	public String getPrj_no() {
		return prj_no;
	}

	@JsonProperty("prj_no")
	public void setPrj_no(String prj_no) {
		this.prj_no = prj_no;
	}

	public String getPrj_type_display() {
		return prj_type_display;
	}

	@JsonProperty("prj_type_display")
	public void setPrj_type_display(String prj_type_display) {
		this.prj_type_display = prj_type_display;
	}

	public String getRepay_way_display() {
		return repay_way_display;
	}

	@JsonProperty("repay_way_display")
	public void setRepay_way_display(String repay_way_display) {
		this.repay_way_display = repay_way_display;
	}

	public String getMoney() {
		return money;
	}

	@JsonProperty("money")
	public void setMoney(String money) {
		this.money = money;
	}

	public String getIncoming() {
		return incoming;
	}

	@JsonProperty("incoming")
	public void setIncoming(String incoming) {
		this.incoming = incoming;
	}

	public String getBid_status() {
		return bid_status;
	}

	@JsonProperty("bid_status")
	public void setBid_status(String bid_status) {
		this.bid_status = bid_status;
	}

	public String getBid_status_display() {
		return bid_status_display;
	}

	@JsonProperty("bid_status_display")
	public void setBid_status_display(String bid_status_display) {
		this.bid_status_display = bid_status_display;
	}

	public String getRepay_percent() {
		return repay_percent;
	}

	@JsonProperty("repay_percent")
	public void setRepay_percent(String repay_percent) {
		this.repay_percent = repay_percent;
	}

	public String getIs_transfer_active() {
		return is_transfer_active;
	}

	@JsonProperty("is_transfer_active")
	public void setIs_transfer_active(String is_transfer_active) {
		this.is_transfer_active = is_transfer_active;
	}

	public String getIcon_limit_amount() {
		return icon_limit_amount;
	}

	@JsonProperty("icon_limit_amount")
	public void setIcon_limit_amount(String icon_limit_amount) {
		this.icon_limit_amount = icon_limit_amount;
	}

	public String getIcon_transfer() {
		return icon_transfer;
	}

	@JsonProperty("icon_transfer")
	public void setIcon_transfer(String icon_transfer) {
		this.icon_transfer = icon_transfer;
	}

	public String getIcon_can_transfer() {
		return icon_can_transfer;
	}

	@JsonProperty("icon_can_transfer")
	public void setIcon_can_transfer(String icon_can_transfer) {
		this.icon_can_transfer = icon_can_transfer;
	}

	public String getIcon_new() {
		return icon_new;
	}

	@JsonProperty("icon_new")
	public void setIcon_new(String icon_new) {
		this.icon_new = icon_new;
	}

	public String getIs_have_contract() {
		return is_have_contract;
	}

	@JsonProperty("is_have_contract")
	public void setIs_have_contract(String is_have_contract) {
		this.is_have_contract = is_have_contract;
	}

	public String getIcon_bianxian() {
		return icon_bianxian;
	}

	@JsonProperty("icon_bianxian")
	public void setIcon_bianxian(String icon_bianxian) {
		this.icon_bianxian = icon_bianxian;
	}

	public String getTransfer_id() {
		return transfer_id;
	}

	@JsonProperty("transfer_id")
	public void setTransfer_id(String transfer_id) {
		this.transfer_id = transfer_id;
	}

	public String getExpire_time_limit() {
		return expire_time_limit;
	}

	@JsonProperty("expire_time_limit")
	public void setExpire_time_limit(String expire_time_limit) {
		this.expire_time_limit = expire_time_limit;
	}

	public String getIcon_only_iphone() {
		return icon_only_iphone;
	}

	@JsonProperty("icon_only_iphone")
	public void setIcon_only_iphone(String icon_only_iphone) {
		this.icon_only_iphone = icon_only_iphone;
	}

	public String getIs_pre_sale() {
		return is_pre_sale;
	}

	@JsonProperty("is_pre_sale")
	public void setIs_pre_sale(String is_pre_sale) {
		this.is_pre_sale = is_pre_sale;
	}

	public String getBenxi() {
		return benxi;
	}

	@JsonProperty("benxi")
	public void setBenxi(String benxi) {
		this.benxi = benxi;
	}

	public String getRewardMoney() {
		return rewardMoney;
	}

	@JsonProperty("rewardMoney")
	public void setRewardMoney(String rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

	
	public String getReward_type() {
		return reward_type;
	}
	
	@JsonProperty("reward_type")
	public void setReward_type(String reward_type) {
		this.reward_type = reward_type;
	}


	public String getReward_type_tips() {
		return reward_type_tips;
	}

	@JsonProperty("reward_type_tips")
	public void setReward_type_tips(String reward_type_tips) {
		this.reward_type_tips = reward_type_tips;
	}

	public String getHongbao() {
		return hongbao;
	}

	@JsonProperty("hongbao")
	public void setHongbao(String hongbao) {
		this.hongbao = hongbao;
	}

    public String getCanInvest() {
		return canInvest;
	}

	@JsonProperty("NovEleventhActivity")
	public void setCanInvest(String canInvest) {
		this.canInvest = canInvest;
	}

	public String getIs_float() {
		return is_float;
	}
	@JsonProperty("is_float")
	public void setIs_float(String is_float) {
		this.is_float = is_float;
	}

	public String getWorth_view() {
		return worth_view;
	}
	@JsonProperty("worth_view")
	public void setWorth_view(String worth_view) {
		this.worth_view = worth_view;
	}

	public String getJxq_rate_view() {
		return jxq_rate_view;
	}
	@JsonProperty("jxq_rate_view")
	public void setJxq_rate_view(String jxq_rate_view) {
		this.jxq_rate_view = jxq_rate_view;
	}
}
