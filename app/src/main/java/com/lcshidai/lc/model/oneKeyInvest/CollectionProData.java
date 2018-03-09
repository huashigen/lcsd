package com.lcshidai.lc.model.oneKeyInvest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by RandyZhang on 2017/4/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectionProData implements Serializable{

    /**
     * prj_id : 11241
     * prj_no : 3D42017032000002
     * demand_amount : 5000000
     * remaining_amount : 0
     * prj_type : D4
     * bid_status : "1"
     */

    private String prj_id;
    private String prj_no;
    private String demand_amount;
    private String remaining_amount;
    private String prj_type;
    private String bid_status;

    public String getPrj_id() {
        return prj_id;
    }

    public void setPrj_id(String prj_id) {
        this.prj_id = prj_id;
    }

    public String getPrj_no() {
        return prj_no;
    }

    public void setPrj_no(String prj_no) {
        this.prj_no = prj_no;
    }

    public String getDemand_amount() {
        return demand_amount;
    }

    public void setDemand_amount(String demand_amount) {
        this.demand_amount = demand_amount;
    }

    public String getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(String remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public String getPrj_type() {
        return prj_type;
    }

    public void setPrj_type(String prj_type) {
        this.prj_type = prj_type;
    }

    public String getBid_status() {
        return bid_status;
    }

    public void setBid_status(String bid_status) {
        this.bid_status = bid_status;
    }
}
