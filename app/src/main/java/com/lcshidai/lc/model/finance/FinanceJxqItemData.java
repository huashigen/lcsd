package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceJxqItemData extends BaseData {

    /**
     * id : 24138
     * amount : 2
     * rate_view : 2%
     * disp_value : 100
     * disp_name : 加息券
     * start_time : 2017-01-06
     * end_time : 2017-04-05
     * ctime : 2017-01-06
     * usedtime : 1970-01-01
     * subTile : 积分商城
     * source : 积分商城
     * fulltip : 仅限购买60天及以上项目
     * use_tip : 仅限购买60天及以上项目
     * user_bonus_status : 1
     * is_usable : 0
     */

    private String id;
    private int amount;
    private String rate_view;
    private int disp_value;
    private String disp_name;
    private String start_time;
    private String end_time;
    private String ctime;
    private String usedtime;
    private String subTile;
    private String source;
    private String fulltip;
    private String use_tip;
    private String user_bonus_status;
    private int is_usable;

    // added by randy at 2017/04/24
    private String prj_day_limit_view;
    private String money_limit_view;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getRate_view() {
        return rate_view;
    }

    public void setRate_view(String rate_view) {
        this.rate_view = rate_view;
    }

    public int getDisp_value() {
        return disp_value;
    }

    public void setDisp_value(int disp_value) {
        this.disp_value = disp_value;
    }

    public String getDisp_name() {
        return disp_name;
    }

    public void setDisp_name(String disp_name) {
        this.disp_name = disp_name;
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

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUsedtime() {
        return usedtime;
    }

    public void setUsedtime(String usedtime) {
        this.usedtime = usedtime;
    }

    public String getSubTile() {
        return subTile;
    }

    public void setSubTile(String subTile) {
        this.subTile = subTile;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFulltip() {
        return fulltip;
    }

    public void setFulltip(String fulltip) {
        this.fulltip = fulltip;
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

    public int getIs_usable() {
        return is_usable;
    }

    public void setIs_usable(int is_usable) {
        this.is_usable = is_usable;
    }

    public String getPrj_day_limit_view() {
        return prj_day_limit_view;
    }

    public void setPrj_day_limit_view(String prj_day_limit_view) {
        this.prj_day_limit_view = prj_day_limit_view;
    }

    public String getMoney_limit_view() {
        return money_limit_view;
    }

    public void setMoney_limit_view(String money_limit_view) {
        this.money_limit_view = money_limit_view;
    }
}
