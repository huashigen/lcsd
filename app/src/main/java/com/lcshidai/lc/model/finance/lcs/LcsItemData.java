package com.lcshidai.lc.model.finance.lcs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LcsItemData extends BaseData {
    /**
     * fund_id : 2
     * fund_name : 理财时代2号
     * fund_type : 4
     * fund_type_str : 信托产品
     * activity : 0
     * annual_rate :
     * time_limit :
     * net_worth : 8.0000
     * item_fund_type : 1
     * item_fund_type_str : 股票型
     * min_buy_amount : 200
     * invest_field : 4
     * invest_field_str : 房地产类
     * collect_process : 60
     * fund_status : 2
     * fund_status_str : 开放募集中
     */

    private String fund_id;
    private String fund_name;
    private String fund_type;
    private String fund_type_str;
    private String activity;
    private String annual_rate;
    private String time_limit;
    private String net_worth;
    private String item_fund_type;
    private String item_fund_type_str;
    private String min_buy_amount;
    private String invest_field;
    private String invest_field_str;
    private String collect_process;
    private String fund_status;
    private String fund_status_str;
    /**
     * fund_invest_at : 股权投资
     * scale_amount : 41,000.00万
     */

    private String fund_invest_at;
    private String scale_amount;

    public String getFund_id() {
        return fund_id;
    }

    public void setFund_id(String fund_id) {
        this.fund_id = fund_id;
    }

    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAnnual_rate() {
        return annual_rate;
    }

    public void setAnnual_rate(String annual_rate) {
        this.annual_rate = annual_rate;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public String getNet_worth() {
        return net_worth;
    }

    public void setNet_worth(String net_worth) {
        this.net_worth = net_worth;
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

    public String getMin_buy_amount() {
        return min_buy_amount;
    }

    public void setMin_buy_amount(String min_buy_amount) {
        this.min_buy_amount = min_buy_amount;
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

    public String getCollect_process() {
        return collect_process;
    }

    public void setCollect_process(String collect_process) {
        this.collect_process = collect_process;
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

    public String getFund_invest_at() {
        return fund_invest_at;
    }

    public void setFund_invest_at(String fund_invest_at) {
        this.fund_invest_at = fund_invest_at;
    }

    public String getScale_amount() {
        return scale_amount;
    }

    public void setScale_amount(String scale_amount) {
        this.scale_amount = scale_amount;
    }
}
