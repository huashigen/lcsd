package com.lcshidai.lc.model;

import java.io.Serializable;

/**
 * 本地支付实体
 * Created by RandyZhang on 2017/2/10.
 */

public class LocalInvestPayModel implements Serializable {

    private String investAmount;                // 投资金额
    private String investPrjId;                 // 投资项目
    private String investJxqId;                 // 投资使用加息券ID
    private String payPwd;                      // 支付密码
    private boolean isRepay;                    // 是否预售
    private boolean isXXB;                      // 是否新秀标
    private String investMobileCode;            // 存管用户投资短信验证码
    private String investUsedRewardType;        // 使用奖励的类型（1红包，2满减券）
    private String investHbBonusRate;           // 红包使用百分比
    private String investHbBonusRateTimeLimit;  // 红包包使用项目期限限制
    private String investHbOrMjqId;             // 投资使用红包或满减券ID

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getInvestPrjId() {
        return investPrjId;
    }

    public void setInvestPrjId(String investPrjId) {
        this.investPrjId = investPrjId;
    }

    public String getInvestJxqId() {
        return investJxqId;
    }

    public void setInvestJxqId(String investJxqId) {
        this.investJxqId = investJxqId;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public boolean isRepay() {
        return isRepay;
    }

    public void setRepay(boolean repay) {
        isRepay = repay;
    }

    public boolean isXXB() {
        return isXXB;
    }

    public void setXXB(boolean XXB) {
        isXXB = XXB;
    }

    public String getInvestMobileCode() {
        return investMobileCode;
    }

    public void setInvestMobileCode(String investMobileCode) {
        this.investMobileCode = investMobileCode;
    }

    public String getInvestUsedRewardType() {
        return investUsedRewardType;
    }

    public void setInvestUsedRewardType(String investUsedRewardType) {
        this.investUsedRewardType = investUsedRewardType;
    }

    public String getInvestHbBonusRate() {
        return investHbBonusRate;
    }

    public void setInvestHbBonusRate(String investHbBonusRate) {
        this.investHbBonusRate = investHbBonusRate;
    }

    public String getInvestHbBonusRateTimeLimit() {
        return investHbBonusRateTimeLimit;
    }

    public void setInvestHbBonusRateTimeLimit(String investHbBonusRateTimeLimit) {
        this.investHbBonusRateTimeLimit = investHbBonusRateTimeLimit;
    }

    public String getInvestHbOrMjqId() {
        return investHbOrMjqId;
    }

    public void setInvestHbOrMjqId(String investHbOrMjqId) {
        this.investHbOrMjqId = investHbOrMjqId;
    }
}
