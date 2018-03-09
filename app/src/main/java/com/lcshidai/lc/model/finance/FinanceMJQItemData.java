package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceMJQItemData extends BaseData{
	/**
     * id : 4932
     * disp_name : 满减券10000-68
     * ctime : 2016-08-25
     * amount : 68
     * use_tip : 仅限购买90天及以上项目
     * user_bonus_status : 1
     * start_time : 2016-08-25
     * end_time : 2016-11-22
     * source : 注册送满减券(大礼包)
     * remark : 满10000减68
     * rate : null
     * type : 1
     * prj_name : null
     * usedtime : 1970-01-01
     * disp_value : 10000
     * use_rule : 充值9932元即可购买投资金额1万元的项目
     * is_usable : 0
     */

    private String id;
    private String disp_name;
    private String ctime;
    private String amount;
    private String use_tip;
    private String user_bonus_status;
    private String start_time;
    private String end_time;
    private String source;
    private String remark;
    private Object rate;
    private String type;
    private Object prj_name;
    private String usedtime;
    private int disp_value;
    private String use_rule;
    private int is_usable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisp_name() {
        return disp_name;
    }

    public void setDisp_name(String disp_name) {
        this.disp_name = disp_name;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUse_tip() {
        return use_tip;
    }

    public void setUse_tip(String use_tip) {
        this.use_tip = use_tip;
    }

    public String getUser_bonus_status() {
        return user_bonus_status;
    }

    public void setUser_bonus_status(String user_bonus_status) {
        this.user_bonus_status = user_bonus_status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getRate() {
        return rate;
    }

    public void setRate(Object rate) {
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getPrj_name() {
        return prj_name;
    }

    public void setPrj_name(Object prj_name) {
        this.prj_name = prj_name;
    }

    public String getUsedtime() {
        return usedtime;
    }

    public void setUsedtime(String usedtime) {
        this.usedtime = usedtime;
    }

    public int getDisp_value() {
        return disp_value;
    }

    public void setDisp_value(int disp_value) {
        this.disp_value = disp_value;
    }

    public String getUse_rule() {
        return use_rule;
    }

    public void setUse_rule(String use_rule) {
        this.use_rule = use_rule;
    }

    public int getIs_usable() {
        return is_usable;
    }

    public void setIs_usable(int is_usable) {
        this.is_usable = is_usable;
    }
}
