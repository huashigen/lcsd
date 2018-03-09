package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by RandyZhang on 2016/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClsAuditListBean implements Serializable {
    private String lcs_audi;
    private String lcs_audit_str;

    public String getLcs_audi() {
        return lcs_audi;
    }

    @JsonProperty("cls_audit")
    public void setLcs_audi(String lcs_audi) {
        this.lcs_audi = lcs_audi;
    }

    public String getLcs_audit_str() {
        return lcs_audit_str;
    }

    @JsonProperty("cls_audit_str")
    public void setLcs_audit_str(String lcs_audit_str) {
        this.lcs_audit_str = lcs_audit_str;
    }
}
