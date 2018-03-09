package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundAccountInfo implements Serializable {
    private String amount;// 累计预约金额
    private String amount_done;// 累计成交金额
    private String manager;// 我的专属理财师姓名
    private String manager_phone;// 我的专属理财师电话
    private String order_amount_total;// 资产配置-在投总资产（万）
    private String lcs_audit;
    private String lcs_audit_str;
    private String invest_level;// 投资者属性：投资者水平鉴定，0：未鉴定，1：保守型，2：稳健性，3：进取型
    private String invest_level_str;
    private List<OrderAmountDetailBean> order_amount_detail;// 资产配置-明细

    /**
     * province : 浙江省
     * city : 杭州市
     */

    private String province;
    private String city;
    /**
     * invest_level_expire_date : 2018-01-01
     */

    private String invest_level_expire_date;

    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount_done() {
        return amount_done;
    }

    @JsonProperty("amount_done")
    public void setAmount_done(String amount_done) {
        this.amount_done = amount_done;
    }

    public String getManager() {
        return manager;
    }

    @JsonProperty("manager")
    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManager_phone() {
        return manager_phone;
    }

    @JsonProperty("manager_phone")
    public void setManager_phone(String manager_phone) {
        this.manager_phone = manager_phone;
    }

    public String getOrder_amount_total() {
        return order_amount_total;
    }

    @JsonProperty("order_amount_total")
    public void setOrder_amount_total(String order_amount_total) {
        this.order_amount_total = order_amount_total;
    }

    public String getLcs_audit() {
        return lcs_audit;
    }

    @JsonProperty("lcs_audit")
    public void setLcs_audit(String lcs_audit) {
        this.lcs_audit = lcs_audit;
    }

    public String getLcs_audit_str() {
        return lcs_audit_str;
    }

    @JsonProperty("lcs_audit_str")
    public void setLcs_audit_str(String lcs_audit_str) {
        this.lcs_audit_str = lcs_audit_str;
    }

    public String getInvest_level() {
        return invest_level;
    }

    @JsonProperty("invest_level")
    public void setInvest_level(String invest_level) {
        this.invest_level = invest_level;
    }

    public String getInvest_level_str() {
        return invest_level_str;
    }

    @JsonProperty("invest_level_str")
    public void setInvest_level_str(String invest_level_str) {
        this.invest_level_str = invest_level_str;
    }

    public List<OrderAmountDetailBean> getOrder_amount_detail() {
        return order_amount_detail;
    }

    @JsonProperty("order_amount_detail")
    public void setOrder_amount_detail(List<OrderAmountDetailBean> order_amount_detail) {
        this.order_amount_detail = order_amount_detail;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInvest_level_expire_date() {
        return invest_level_expire_date;
    }

    public void setInvest_level_expire_date(String invest_level_expire_date) {
        this.invest_level_expire_date = invest_level_expire_date;
    }
}
