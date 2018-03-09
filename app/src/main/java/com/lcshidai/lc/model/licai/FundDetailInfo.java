package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RandyZhang on 16/9/28.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundDetailInfo implements Serializable{
    private String fund_name;
    private String net_worth;
    private String net_update_time;
    private String item_fund_type;
    private String item_fund_type_str;
    private String time_limit;
    private String min_buy_amount;
    private String sub_fee_rule;
    private String apply_fee_rule;
    private String manage_fee_rule;
    private String redeem_fee_rule;
    private String reward_rule;
    private String account_holder;
    private String bank_account;
    private String keep_bank;
    private String remark;
    private String fund_type;
    private String fund_type_str;
    private String is_fixed;
    private String fund_status;
    private String fund_status_str;
    private String scale_amount;
    private String collect_amount;
    private String collect_start_time;
    private String collect_end_time;
    private String rate;
    private String size_ratio;
    private String size_ratio_str;
    private String yield_type;
    private String issue_org;
    private String issuer_description;
    private String issuer;
    private String publish_channel;
    private String fund_code;
    private String pay_way;
    private String pay_way_str;
    private String invest_district;
    private String invest_field;
    private String invest_field_str;
    private String structuring_flag;
    private String manager;
    private String fund_manager_xname;
    private String total_net_value;
    private String step_buy_amount;
    private String open_date;
    private String closing_period;
    private String pre_close_period;
    private String build_time;
    private String fund_highlight;
    private String project_detail;
    private String pro_detail_attach;
    private String invest_risk_ctrl;
    private String fund_manage_attach;
    private String other_comments;
    private String invest_scope;
    private String manage_org;
    private String invest_philosophy;
    private String ltv_ratio;
    private String ltv_body;
    private String fund_invest_at;
    private String return_source;
    private String fund_company;
    private String invest_target;
    private String luyan_thumb;
    private String luyan_name;

    private List<FundManagerBean> fundManager;

    private List<YieldBean> yield;

    private List<NetDataBean> net_data;

    private List<Shsz300DataBean> shsz300_data;

    private List<FundItemsBean> fund_items;

    private List<AttachBean> attach;

    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
    }

    public String getNet_worth() {
        return net_worth;
    }

    public void setNet_worth(String net_worth) {
        this.net_worth = net_worth;
    }

    public String getNet_update_time() {
        return net_update_time;
    }

    public void setNet_update_time(String net_update_time) {
        this.net_update_time = net_update_time;
    }

    public String getItem_fund_type() {
        return item_fund_type;
    }

    public void setItem_fund_type(String item_fund_type) {
        this.item_fund_type = item_fund_type;
    }

    public String getItem_fund_type_str() {
        return item_fund_type_str;
    }

    public void setItem_fund_type_str(String item_fund_type_str) {
        this.item_fund_type_str = item_fund_type_str;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public String getMin_buy_amount() {
        return min_buy_amount;
    }

    public void setMin_buy_amount(String min_buy_amount) {
        this.min_buy_amount = min_buy_amount;
    }

    public String getSub_fee_rule() {
        return sub_fee_rule;
    }

    public void setSub_fee_rule(String sub_fee_rule) {
        this.sub_fee_rule = sub_fee_rule;
    }

    public String getApply_fee_rule() {
        return apply_fee_rule;
    }

    public void setApply_fee_rule(String apply_fee_rule) {
        this.apply_fee_rule = apply_fee_rule;
    }

    public String getManage_fee_rule() {
        return manage_fee_rule;
    }

    public void setManage_fee_rule(String manage_fee_rule) {
        this.manage_fee_rule = manage_fee_rule;
    }

    public String getRedeem_fee_rule() {
        return redeem_fee_rule;
    }

    public void setRedeem_fee_rule(String redeem_fee_rule) {
        this.redeem_fee_rule = redeem_fee_rule;
    }

    public String getReward_rule() {
        return reward_rule;
    }

    public void setReward_rule(String reward_rule) {
        this.reward_rule = reward_rule;
    }

    public String getAccount_holder() {
        return account_holder;
    }

    public void setAccount_holder(String account_holder) {
        this.account_holder = account_holder;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getKeep_bank() {
        return keep_bank;
    }

    public void setKeep_bank(String keep_bank) {
        this.keep_bank = keep_bank;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFund_type() {
        return fund_type;
    }

    public void setFund_type(String fund_type) {
        this.fund_type = fund_type;
    }

    public String getFund_type_str() {
        return fund_type_str;
    }

    public void setFund_type_str(String fund_type_str) {
        this.fund_type_str = fund_type_str;
    }

    public String getIs_fixed() {
        return is_fixed;
    }

    public void setIs_fixed(String is_fixed) {
        this.is_fixed = is_fixed;
    }

    public String getFund_status() {
        return fund_status;
    }

    public void setFund_status(String fund_status) {
        this.fund_status = fund_status;
    }

    public String getFund_status_str() {
        return fund_status_str;
    }

    public void setFund_status_str(String fund_status_str) {
        this.fund_status_str = fund_status_str;
    }

    public String getScale_amount() {
        return scale_amount;
    }

    public void setScale_amount(String scale_amount) {
        this.scale_amount = scale_amount;
    }

    public String getCollect_amount() {
        return collect_amount;
    }

    public void setCollect_amount(String collect_amount) {
        this.collect_amount = collect_amount;
    }

    public String getCollect_start_time() {
        return collect_start_time;
    }

    public void setCollect_start_time(String collect_start_time) {
        this.collect_start_time = collect_start_time;
    }

    public String getCollect_end_time() {
        return collect_end_time;
    }

    public void setCollect_end_time(String collect_end_time) {
        this.collect_end_time = collect_end_time;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSize_ratio() {
        return size_ratio;
    }

    public void setSize_ratio(String size_ratio) {
        this.size_ratio = size_ratio;
    }

    public String getSize_ratio_str() {
        return size_ratio_str;
    }

    public void setSize_ratio_str(String size_ratio_str) {
        this.size_ratio_str = size_ratio_str;
    }

    public String getYield_type() {
        return yield_type;
    }

    public void setYield_type(String yield_type) {
        this.yield_type = yield_type;
    }

    public String getIssue_org() {
        return issue_org;
    }

    public void setIssue_org(String issue_org) {
        this.issue_org = issue_org;
    }

    public String getIssuer_description() {
        return issuer_description;
    }

    public void setIssuer_description(String issuer_description) {
        this.issuer_description = issuer_description;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getPublish_channel() {
        return publish_channel;
    }

    public void setPublish_channel(String publish_channel) {
        this.publish_channel = publish_channel;
    }

    public String getFund_code() {
        return fund_code;
    }

    public void setFund_code(String fund_code) {
        this.fund_code = fund_code;
    }

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }

    public String getPay_way_str() {
        return pay_way_str;
    }

    public void setPay_way_str(String pay_way_str) {
        this.pay_way_str = pay_way_str;
    }

    public String getInvest_district() {
        return invest_district;
    }

    public void setInvest_district(String invest_district) {
        this.invest_district = invest_district;
    }

    public String getInvest_field() {
        return invest_field;
    }

    public void setInvest_field(String invest_field) {
        this.invest_field = invest_field;
    }

    public String getInvest_field_str() {
        return invest_field_str;
    }

    public void setInvest_field_str(String invest_field_str) {
        this.invest_field_str = invest_field_str;
    }

    public String getStructuring_flag() {
        return structuring_flag;
    }

    public void setStructuring_flag(String structuring_flag) {
        this.structuring_flag = structuring_flag;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getFund_manager_xname() {
        return fund_manager_xname;
    }

    public void setFund_manager_xname(String fund_manager_xname) {
        this.fund_manager_xname = fund_manager_xname;
    }

    public String getTotal_net_value() {
        return total_net_value;
    }

    public void setTotal_net_value(String total_net_value) {
        this.total_net_value = total_net_value;
    }

    public String getStep_buy_amount() {
        return step_buy_amount;
    }

    public void setStep_buy_amount(String step_buy_amount) {
        this.step_buy_amount = step_buy_amount;
    }

    public String getOpen_date() {
        return open_date;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public String getClosing_period() {
        return closing_period;
    }

    public void setClosing_period(String closing_period) {
        this.closing_period = closing_period;
    }

    public String getPre_close_period() {
        return pre_close_period;
    }

    public void setPre_close_period(String pre_close_period) {
        this.pre_close_period = pre_close_period;
    }

    public String getBuild_time() {
        return build_time;
    }

    public void setBuild_time(String build_time) {
        this.build_time = build_time;
    }

    public String getFund_highlight() {
        return fund_highlight;
    }

    public void setFund_highlight(String fund_highlight) {
        this.fund_highlight = fund_highlight;
    }

    public String getProject_detail() {
        return project_detail;
    }

    public void setProject_detail(String project_detail) {
        this.project_detail = project_detail;
    }

    public String getPro_detail_attach() {
        return pro_detail_attach;
    }

    public void setPro_detail_attach(String pro_detail_attach) {
        this.pro_detail_attach = pro_detail_attach;
    }

    public String getInvest_risk_ctrl() {
        return invest_risk_ctrl;
    }

    public void setInvest_risk_ctrl(String invest_risk_ctrl) {
        this.invest_risk_ctrl = invest_risk_ctrl;
    }

    public String getFund_manage_attach() {
        return fund_manage_attach;
    }

    public void setFund_manage_attach(String fund_manage_attach) {
        this.fund_manage_attach = fund_manage_attach;
    }

    public String getOther_comments() {
        return other_comments;
    }

    public void setOther_comments(String other_comments) {
        this.other_comments = other_comments;
    }

    public String getInvest_scope() {
        return invest_scope;
    }

    public void setInvest_scope(String invest_scope) {
        this.invest_scope = invest_scope;
    }

    public String getManage_org() {
        return manage_org;
    }

    public void setManage_org(String manage_org) {
        this.manage_org = manage_org;
    }

    public String getInvest_philosophy() {
        return invest_philosophy;
    }

    public void setInvest_philosophy(String invest_philosophy) {
        this.invest_philosophy = invest_philosophy;
    }

    public String getLtv_ratio() {
        return ltv_ratio;
    }

    public void setLtv_ratio(String ltv_ratio) {
        this.ltv_ratio = ltv_ratio;
    }

    public String getLtv_body() {
        return ltv_body;
    }

    public void setLtv_body(String ltv_body) {
        this.ltv_body = ltv_body;
    }

    public String getFund_invest_at() {
        return fund_invest_at;
    }

    public void setFund_invest_at(String fund_invest_at) {
        this.fund_invest_at = fund_invest_at;
    }

    public String getReturn_source() {
        return return_source;
    }

    public void setReturn_source(String return_source) {
        this.return_source = return_source;
    }

    public String getFund_company() {
        return fund_company;
    }

    public void setFund_company(String fund_company) {
        this.fund_company = fund_company;
    }

    public String getInvest_target() {
        return invest_target;
    }

    public void setInvest_target(String invest_target) {
        this.invest_target = invest_target;
    }

    public String getLuyan_thumb() {
        return luyan_thumb;
    }

    public void setLuyan_thumb(String luyan_thumb) {
        this.luyan_thumb = luyan_thumb;
    }

    public String getLuyan_name() {
        return luyan_name;
    }

    public void setLuyan_name(String luyan_name) {
        this.luyan_name = luyan_name;
    }

    public List<YieldBean> getYield() {
        return yield;
    }

    public void setYield(List<YieldBean> yield) {
        this.yield = yield;
    }

    public List<NetDataBean> getNet_data() {
        return net_data;
    }

    public void setNet_data(List<NetDataBean> net_data) {
        this.net_data = net_data;
    }

    public List<Shsz300DataBean> getShsz300_data() {
        return shsz300_data;
    }

    public void setShsz300_data(List<Shsz300DataBean> shsz300_data) {
        this.shsz300_data = shsz300_data;
    }

    public List<FundItemsBean> getFund_items() {
        return fund_items;
    }

    public void setFund_items(List<FundItemsBean> fund_items) {
        this.fund_items = fund_items;
    }

    public List<AttachBean> getAttach() {
        return attach;
    }

    public void setAttach(List<AttachBean> attach) {
        this.attach = attach;
    }

    public List<FundManagerBean> getFundManager() {
        return fundManager;
    }

    @JsonProperty("fund_manager")
    public void setFundManager(List<FundManagerBean> fundManager) {
        this.fundManager = fundManager;
    }
}
