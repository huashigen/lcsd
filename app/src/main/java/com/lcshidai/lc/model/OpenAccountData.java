package com.lcshidai.lc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by RandyZhang on 16/7/7.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAccountData extends BaseData {
    /**
     * "data": {
     "certId": "0017",
     "instId": "TRJ",
     "requestId": "B00000161",
     "Cstno": "70870",
     "certType": 1,
     "reqDataNo": "oc_70870",
     "date": 1468397228,
     "operType": "OCReq",
     "version": "1.0.0",
     "accountName": "",
     "certNo": "",
     "mobile": "",
     "extension": ""
     "sign":""
     }
     */

    private String certId;
    private String instId;
    private String requestId;
    private String cstno;
    private String certType; // 证件类型
    private String reqDataNo;
    private String date;
    private String operType;
    private String version;
    private String accountName; // 客户名称
    private String certNo; // 证件号码
    private String mobile; // 预留手机号
    private String extention;
    private String sign;

    public String getCertId() {
        return certId;
    }

    @JsonProperty("certId")
    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getInstId() {
        return instId;
    }
    @JsonProperty("instId")
    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getRequestId() {
        return requestId;
    }
    @JsonProperty("requestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCstno() {
        return cstno;
    }
    @JsonProperty("Cstno")
    public void setCstno(String cstno) {
        this.cstno = cstno;
    }

    public String getReqDataNo() {
        return reqDataNo;
    }
    @JsonProperty("")
    public void setReqDataNo(String reqDataNo) {
        this.reqDataNo = reqDataNo;
    }

    public String getDate() {
        return date;
    }
    @JsonProperty("")
    public void setDate(String date) {
        this.date = date;
    }

    public String getOperType() {
        return operType;
    }
    @JsonProperty("operType")
    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getVersion() {
        return version;
    }
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public String getAccountName() {
        return accountName;
    }

    @JsonProperty("accountName")
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCertType() {
        return certType;
    }

    @JsonProperty("certType")
    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    @JsonProperty("certNo")
    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getMobile() {
        return mobile;
    }

    @JsonProperty("mobile")
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getExtention() {
        return extention;
    }

    @JsonProperty("extention")
    public void setExtention(String extention) {
        this.extention = extention;
    }

    public String getSign() {
        return sign;
    }

    @JsonProperty("sign")
    public void setSign(String sign) {
        this.sign = sign;
    }
}
