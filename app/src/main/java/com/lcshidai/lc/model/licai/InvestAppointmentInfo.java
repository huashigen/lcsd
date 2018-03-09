package com.lcshidai.lc.model.licai;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/9/29.
 */

public class InvestAppointmentInfo implements Serializable {

    /**
     * {"order_id":"1","fund_name":"理财时代号","fund_type":"1","fund_type_str":"固收","status":"1","status_str":"待处理","amount":"100","manager":"张小龙","phone":"18072939755","time":"2016-09-01 12:00"},{"order_id":"2","fund_name":"理财时代2号","fund_type":"1","fund_type_str":"固收","status":"2","status_str":"处理中","amount":"100","manager":"张大龙","phone":"18072939755","time":"2016-09-01 12:00"}
     */

    private String order_id;
    private String fund_name;
    private String fund_type;
    private String fund_type_str;
    private String status;
    private String status_str;
    private String amount;
    private String manager;
    private String phone;
    private String time;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_str() {
        return status_str;
    }

    public void setStatus_str(String status_str) {
        this.status_str = status_str;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
