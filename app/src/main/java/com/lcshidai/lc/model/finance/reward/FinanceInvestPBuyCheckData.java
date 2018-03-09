package com.lcshidai.lc.model.finance.reward;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceInvestPBuyCheckData {
    private String addInterestView; //加息券利率%比
    private String addInterestId;// 优选加息券id
    private String addInterestAmount;//加息券计算金额
    private String addInterestCount;//可使用加息券数量
    private String money;            // 投资金额
    private String prjid;
    private String yearrate;
    private boolean isRepay;
    private String income;
    private String protocol_id;
    private String protocol_name;
    private String tatalReward;
    private String usemoney;
    private String benxi;
    private String server_protocol;//
    // added by randy at 2017/04/21
    private String cfd_name;
    private String cfd_protocol;// 长富贷协议
    private String is_tips;
    private String tips_error;//
    private String total_rate;
    private RewardInfoModel rewardInfo;

    public String getAddInterestView() {
        return addInterestView;
    }

    public void setAddInterestView(String addInterestView) {
        this.addInterestView = addInterestView;
    }

    public String getAddInterestId() {
        return addInterestId;
    }

    public void setAddInterestId(String addInterestId) {
        this.addInterestId = addInterestId;
    }

    public String getAddInterestAmount() {
        return addInterestAmount;
    }

    public void setAddInterestAmount(String addInterestAmount) {
        this.addInterestAmount = addInterestAmount;
    }

    public String getAddInterestCount() {
        return addInterestCount;
    }

    public void setAddInterestCount(String addInterestCount) {
        this.addInterestCount = addInterestCount;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPrjid() {
        return prjid;
    }

    public void setPrjid(String prjid) {
        this.prjid = prjid;
    }

    public String getYearrate() {
        return yearrate;
    }

    public void setYearrate(String yearrate) {
        this.yearrate = yearrate;
    }

    public boolean isRepay() {
        return isRepay;
    }

    public void setRepay(boolean isRepay) {
        this.isRepay = isRepay;
    }

    public String getIncome() {
        return income;
    }

    @JsonProperty("income")
    public void setIncome(String income) {
        this.income = income;
    }

    public String getProtocol_id() {
        return protocol_id;
    }

    @JsonProperty("protocol_id")
    public void setProtocol_id(String protocol_id) {
        this.protocol_id = protocol_id;
    }

    public String getProtocol_name() {
        return protocol_name;
    }

    @JsonProperty("protocol_name")
    public void setProtocol_name(String protocol_name) {
        this.protocol_name = protocol_name;
    }

    public String getTatalReward() {
        return tatalReward;
    }

    @JsonProperty("tatalReward")
    public void setTatalReward(String tatalReward) {
        this.tatalReward = tatalReward;
    }

    public String getUsemoney() {
        return usemoney;
    }

    @JsonProperty("usemoney")
    public void setUsemoney(String usemoney) {
        this.usemoney = usemoney;
    }

    public String getBenxi() {
        return benxi;
    }

    @JsonProperty("benxi")
    public void setBenxi(String benxi) {
        this.benxi = benxi;
    }

    public String getServer_protocol() {
        return server_protocol;
    }

    @JsonProperty("server_protocol")
    public void setServer_protocol(String server_protocol) {
        this.server_protocol = server_protocol;
    }

    public String getCfd_name() {
        return cfd_name;
    }

    @JsonProperty("cfd_name")
    public void setCfd_name(String cfd_name) {
        this.cfd_name = cfd_name;
    }

    public String getCfd_protocol() {
        return cfd_protocol;
    }

    @JsonProperty("cfd_protocol")
    public void setCfd_protocol(String cfd_protocol) {
        this.cfd_protocol = cfd_protocol;
    }

    public String getIs_tips() {
        return is_tips;
    }

    @JsonProperty("is_tips")
    public void setIs_tips(String is_tips) {
        this.is_tips = is_tips;
    }

    public String getTips_error() {
        return tips_error;
    }

    @JsonProperty("tips_error")
    public void setTips_error(String tips_error) {
        this.tips_error = tips_error;
    }

    public RewardInfoModel getRewardInfo() {
        return rewardInfo;
    }

    @JsonProperty("reward_info")
    public void setRewardInfo(RewardInfoModel rewardInfo) {
        this.rewardInfo = rewardInfo;
    }

    public String getTotal_rate() {
        return total_rate;
    }

    @JsonProperty("total_rate")
    public void setTotal_rate(String total_rate) {
        this.total_rate = total_rate;
    }


}
