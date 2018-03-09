package com.lcshidai.lc.model.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

import android.os.Parcel;
import android.os.Parcelable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CashCheckAddData extends BaseData implements Parcelable {
    public String prj_order_id;// 38008,
    public String prj_id;// "9479",
    public String rate_tips;// "请输入变现利率为6.28%-9.42%之间的值!",
    public String curr_cash_money;// "变现金额",
    public String balance_rate;// 平衡利率,
    public String max_cash_money;// 最大可变现金额,
    public String payable_interest;// 到期应付利息,
    public String sdt_left_time_money;// 剩余资产到期价值,
    public String service_fee;// 平台服务费,
    public String real_into_account_money;// 实际到账金额,
    public String original_show_prj_name;// "企益融 - qqxyb2014120303",
    public String sdt_prj_name;// "SDT2015030310006",
    public String sdt_show_prj_name;// "速转让 - SDT2015030310006",
    public String real_income;//实际获得收益
    public String repay_way_name;//repay_way_name
    public String protocol_name;//
    public String protocol_url;//

    public CashCheckAddData() {

    }

    public CashCheckAddData(String me) {

    }

    public String getPrj_order_id() {
        return prj_order_id;
    }

    @JsonProperty("prj_order_id")
    public void setPrj_order_id(String prj_order_id) {
        this.prj_order_id = prj_order_id;
    }

    public String getPrj_id() {
        return prj_id;
    }

    @JsonProperty("prj_id")
    public void setPrj_id(String prj_id) {
        this.prj_id = prj_id;
    }

    public String getRate_tips() {
        return rate_tips;
    }

    @JsonProperty("rate_tips")
    public void setRate_tips(String rate_tips) {
        this.rate_tips = rate_tips;
    }

    public String getCurr_cash_money() {
        return curr_cash_money;
    }

    @JsonProperty("curr_cash_money")
    public void setCurr_cash_money(String curr_cash_money) {
        this.curr_cash_money = curr_cash_money;
    }

    public String getBalance_rate() {
        return balance_rate;
    }

    @JsonProperty("balance_rate")
    public void setBalance_rate(String balance_rate) {
        this.balance_rate = balance_rate;
    }

    public String getMax_cash_money() {
        return max_cash_money;
    }

    @JsonProperty("max_cash_money")
    public void setMax_cash_money(String max_cash_money) {
        this.max_cash_money = max_cash_money;
    }

    public String getPayable_interest() {
        return payable_interest;
    }

    @JsonProperty("payable_interest")
    public void setPayable_interest(String payable_interest) {
        this.payable_interest = payable_interest;
    }

    public String getSdt_left_time_money() {
        return sdt_left_time_money;
    }

    @JsonProperty("sdt_left_time_money")
    public void setSdt_left_time_money(String sdt_left_time_money) {
        this.sdt_left_time_money = sdt_left_time_money;
    }

    public String getService_fee() {
        return service_fee;
    }

    @JsonProperty("service_fee")
    public void setService_fee(String service_fee) {
        this.service_fee = service_fee;
    }

    public String getReal_into_account_money() {
        return real_into_account_money;
    }

    @JsonProperty("real_into_account_money")
    public void setReal_into_account_money(String real_into_account_money) {
        this.real_into_account_money = real_into_account_money;
    }

    public String getOriginal_show_prj_name() {
        return original_show_prj_name;
    }

    @JsonProperty("original_show_prj_name")
    public void setOriginal_show_prj_name(String original_show_prj_name) {
        this.original_show_prj_name = original_show_prj_name;
    }

    public String getSdt_prj_name() {
        return sdt_prj_name;
    }

    @JsonProperty("sdt_prj_name")
    public void setSdt_prj_name(String sdt_prj_name) {
        this.sdt_prj_name = sdt_prj_name;
    }

    public String getSdt_show_prj_name() {
        return sdt_show_prj_name;
    }

    @JsonProperty("sdt_show_prj_name")
    public void setSdt_show_prj_name(String sdt_show_prj_name) {
        this.sdt_show_prj_name = sdt_show_prj_name;
    }

    public String getReal_income() {
        return real_income;
    }

    @JsonProperty("real_income")
    public void setReal_income(String real_income) {
        this.real_income = real_income;
    }

    public String getRepay_way_name() {
        return repay_way_name;
    }

    @JsonProperty("repay_way_name")
    public void setRepay_way_name(String repay_way_name) {
        this.repay_way_name = repay_way_name;
    }

    public String getProtocol_name() {
        return protocol_name;
    }

    @JsonProperty("protocol_name")
    public void setProtocol_name(String protocol_name) {
        this.protocol_name = protocol_name;
    }

    public String getProtocol_url() {
        return protocol_url;
    }

    @JsonProperty("protocol_url")
    public void setProtocol_url(String protocol_url) {
        this.protocol_url = protocol_url;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prj_order_id);
        dest.writeString(prj_id);
        dest.writeString(rate_tips);
        dest.writeString(curr_cash_money);
        dest.writeString(balance_rate);
        dest.writeString(max_cash_money);
        dest.writeString(payable_interest);
        dest.writeString(sdt_left_time_money);
        dest.writeString(service_fee);
        dest.writeString(real_into_account_money);
        dest.writeString(original_show_prj_name);
        dest.writeString(sdt_prj_name);
        dest.writeString(sdt_show_prj_name);
        dest.writeString(real_income);
        dest.writeString(repay_way_name);
        dest.writeString(protocol_name);
        dest.writeString(protocol_url);
    }

    public static final Parcelable.Creator<CashCheckAddData> CREATOR = new Creator<CashCheckAddData>() {

        @Override
        public CashCheckAddData createFromParcel(Parcel source) {
            CashCheckAddData info = new CashCheckAddData();
            info.prj_order_id = source.readString();
            info.prj_id = source.readString();
            info.rate_tips = source.readString();
            info.curr_cash_money = source.readString();
            info.balance_rate = source.readString();
            info.max_cash_money = source.readString();
            info.payable_interest = source.readString();
            info.sdt_left_time_money = source.readString();
            info.service_fee = source.readString();
            info.real_into_account_money = source.readString();
            info.original_show_prj_name = source.readString();
            info.sdt_prj_name = source.readString();
            info.sdt_show_prj_name = source.readString();
            info.real_income = source.readString();
            info.repay_way_name = source.readString();
            info.protocol_name = source.readString();
            info.protocol_url = source.readString();
            return info;
        }

        @Override
        public CashCheckAddData[] newArray(int size) {
            return new CashCheckAddData[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }


}
