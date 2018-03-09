package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceHBItemData extends BaseData{
	/**
     * total_amount : 20.00
     * rate : 1
     * rest_amount : 20.00
     * end_time : 1474646399
     * use_amount : 1.00
     * bonus_name : 注册红包
     * use_tip : 可投资任意项目
     * start_time : 1472054400
     * is_usable : 1
     * prj_term : 0
     * ct : 1
     * end_time_date : 2016-09-23
     * start_time_date : 2016-08-25
     */

    private String total_amount;
    private float rate;
    private String rest_amount;
    private int end_time;
    private String use_amount;
    private String bonus_name;
    private String use_tip;
    private int start_time;
    private int is_usable;
    private String prj_term;
    private int ct;
    private String end_time_date;
    private String start_time_date;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getRest_amount() {
        return rest_amount;
    }

    public void setRest_amount(String rest_amount) {
        this.rest_amount = rest_amount;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getUse_amount() {
        return use_amount;
    }

    public void setUse_amount(String use_amount) {
        this.use_amount = use_amount;
    }

    public String getBonus_name() {
        return bonus_name;
    }

    public void setBonus_name(String bonus_name) {
        this.bonus_name = bonus_name;
    }

    public String getUse_tip() {
        return use_tip;
    }

    public void setUse_tip(String use_tip) {
        this.use_tip = use_tip;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getIs_usable() {
        return is_usable;
    }

    public void setIs_usable(int is_usable) {
        this.is_usable = is_usable;
    }

    public String getPrj_term() {
        return prj_term;
    }

    public void setPrj_term(String prj_term) {
        this.prj_term = prj_term;
    }

    public int getCt() {
        return ct;
    }

    public void setCt(int ct) {
        this.ct = ct;
    }

    public String getEnd_time_date() {
        return end_time_date;
    }

    public void setEnd_time_date(String end_time_date) {
        this.end_time_date = end_time_date;
    }

    public String getStart_time_date() {
        return start_time_date;
    }

    public void setStart_time_date(String start_time_date) {
        this.start_time_date = start_time_date;
    }
}
