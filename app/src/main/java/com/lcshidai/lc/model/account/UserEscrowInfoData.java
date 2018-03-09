package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lcshidai.lc.model.BaseData;

/**
 * Created by Allin on 2016/7/11.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEscrowInfoData extends BaseData{
    /**
     * account_name : 夜**
     * cert_type : 1
     * cert_no : 5335********0238
     * ecw_mobile : 156****0146
     * account_type : 0
     * ecard_no : 6210********1375
     * bank_single : 1000000
     * bank_day : 1000000
     * bankcard_no : 6226********6410
     * bankname : 民生银行
     * sub_bankname : 中国民生银行股份有限公司杭州分行清算中心
     * bank_icon : https://escrow.tourongjia.com/public/images/bankicon/%E6%B0%91%E7%94%9F%E9%93%B6%E8%A1%8C.png
     */

    private String account_name;
    private String cert_type;
    private String cert_no;
    private String ecw_mobile;
    private String account_type;
    private String ecard_no;
    private String bank_single;
    private String bank_day;
    private String bankcard_no;
    private String bankname;
    private String sub_bankname;
    private String bank_icon;

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getCert_type() {
        return cert_type;
    }

    public void setCert_type(String cert_type) {
        this.cert_type = cert_type;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getEcw_mobile() {
        return ecw_mobile;
    }

    public void setEcw_mobile(String ecw_mobile) {
        this.ecw_mobile = ecw_mobile;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getEcard_no() {
        return ecard_no;
    }

    public void setEcard_no(String ecard_no) {
        this.ecard_no = ecard_no;
    }

    public String getBank_single() {
        return bank_single;
    }

    public void setBank_single(String bank_single) {
        this.bank_single = bank_single;
    }

    public String getBank_day() {
        return bank_day;
    }

    public void setBank_day(String bank_day) {
        this.bank_day = bank_day;
    }

    public String getBankcard_no() {
        return bankcard_no;
    }

    public void setBankcard_no(String bankcard_no) {
        this.bankcard_no = bankcard_no;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getSub_bankname() {
        return sub_bankname;
    }

    public void setSub_bankname(String sub_bankname) {
        this.sub_bankname = sub_bankname;
    }

    public String getBank_icon() {
        return bank_icon;
    }

    public void setBank_icon(String bank_icon) {
        this.bank_icon = bank_icon;
    }

}
