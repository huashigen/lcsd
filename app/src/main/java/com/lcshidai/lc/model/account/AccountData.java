package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountData extends BaseData implements Serializable {
    private String Account_usable;//
    private String totalAccount_view;// 账户资产显示的字段
    private String top_amount_view;// 账户余额
    private String freeze_money;// 冻结金额
    private String profit_view;// 净收益、已赚收益
    private String total_profit;// 预期总收益
    private String incoming_monthly;// 近一个月收益

    private String money_debt;// 我的变现
    private String money_invest;// 我的投资
    private String totalAccount;// 账户总资产
    private String amount_view;// 可用余额
    private String buy_freeze_money_view;// 投标中金额【已冻结】
    private String cash_freeze_money_view;// 提现中金额【已冻结】
    private String will_profit_view;// 待收利息、预期收益
    private String will_principal_view;// 待收本金
    private String totalAccountView;// 资产总额
    private String pendingAmountView;// 银行处理中
    private String investing_prj_count;// 投资中-笔数
    private String investing_prj_money_view;// 投资中-金额
    private String willrepay_prj_count;// 待回款-笔数
    private String willrepay_prj_money_view;// 待回款-金额
    private String repayed_prj_count;// 回款完毕-笔数
    private String repayed_prj_money_view;// 回款完毕-金额
    private String totalInvestCount;// 全部笔数
    private String totalInvestView;// 全部金额
    private String reward_money_view;// 活动奖励金额可提现
    private String total_reward_money;// 活动奖励金额可提现
    private String invest_reward_money_view;// 活动奖励金额投资后可提现
    private String total_reward_money_view;// 活动奖金总金额
    private String repayFreezeMoneyView;//银行还款处理中
    private String repayIn_count;// 回款中-笔数
    private String repayIn_money_view;// 回款中-金额
    private String repay_total_amount;// 下一回款日待收本息
    private String repay_prj_number;// 下个回款项目总数
    private String repay_repay_date;// 下个回款日期
    private String lottery_num;
    private String reward_sum;// 已经赚取红包奖励
    private String totalPropertyView;// 预期总资产

    public String getReward_sum() {
        return reward_sum;
    }

    @JsonProperty("reward_sum")
    public void setReward_sum(String reward_sum) {
        this.reward_sum = reward_sum;
    }

    private String safe_level_label;// 安全等级, a: 低, b: 中, c: 高
    private String count_transfer;// 可转让数
    private String current_month;//
    private String will_repay_total_amont;//
    private AccountTip_num tip_num; // 角标信息
    private String totalReward; // 红包
    AccountChildData my_invest_info;

    private String recharge_reward;

    private String source;
    private String is_safety_ins;// 账户资金安全险 (0未开通,1已开通,2快到期)
    //"https://waptest.toubaojia.com/#/bankSecurity/15601949622/1010001/2"
    private String safety_url;// 对应的投保家跳转地址

    public String getRepayFreezeMoneyView() {
        return repayFreezeMoneyView;
    }

    public void setRepayFreezeMoneyView(String repayFreezeMoneyView) {
        this.repayFreezeMoneyView = repayFreezeMoneyView;
    }

    public AccountData() {

    }

    public AccountData(String me) {

    }

    public String getTotalPropertyView() {
        return totalPropertyView;
    }

    @JsonProperty("totalPropertyView")
    public void setTotalPropertyView(String totalPropertyView) {
        this.totalPropertyView = totalPropertyView;
    }

    public String getAccount_usable() {
        return Account_usable;
    }

    @JsonProperty("Account_usable")
    public void setAccount_usable(String account_usable) {
        Account_usable = account_usable;
    }

    public String getTotalAccount_view() {
        return totalAccount_view;
    }

    @JsonProperty("totalAccount_view")
    public void setTotalAccount_view(String totalAccount_view) {
        this.totalAccount_view = totalAccount_view;
    }

    public String getTop_amount_view() {
        return top_amount_view;
    }

    @JsonProperty("top_amount_view")
    public void setTop_amount_view(String top_amount_view) {
        this.top_amount_view = top_amount_view;
    }

    public String getFreeze_money() {
        return freeze_money;
    }

    @JsonProperty("freeze_money")
    public void setFreeze_money(String freeze_money) {
        this.freeze_money = freeze_money;
    }

    public String getProfit_view() {
        return profit_view;
    }

    @JsonProperty("profit_view")
    public void setProfit_view(String profit_view) {
        this.profit_view = profit_view;
    }

    public String getTotal_profit() {
        return total_profit;
    }

    @JsonProperty("total_profit")
    public void setTotal_profit(String total_profit) {
        this.total_profit = total_profit;
    }

    public String getIncoming_monthly() {
        return incoming_monthly;
    }

    @JsonProperty("incoming_monthly")
    public void setIncoming_monthly(String incoming_monthly) {
        this.incoming_monthly = incoming_monthly;
    }

    public String getMoney_debt() {
        return money_debt;
    }

    @JsonProperty("money_debt")
    public void setMoney_debt(String money_debt) {
        this.money_debt = money_debt;
    }

    public String getMoney_invest() {
        return money_invest;
    }

    @JsonProperty("money_invest")
    public void setMoney_invest(String money_invest) {
        this.money_invest = money_invest;
    }

    public String getTotalAccount() {
        return totalAccount;
    }

    @JsonProperty("totalAccount")
    public void setTotalAccount(String totalAccount) {
        this.totalAccount = totalAccount;
    }

    public String getAmount_view() {
        return amount_view;
    }

    @JsonProperty("amount_view")
    public void setAmount_view(String amount_view) {
        this.amount_view = amount_view;
    }

    public String getBuy_freeze_money_view() {
        return buy_freeze_money_view;
    }

    @JsonProperty("buy_freeze_money_view")
    public void setBuy_freeze_money_view(String buy_freeze_money_view) {
        this.buy_freeze_money_view = buy_freeze_money_view;
    }

    public String getCash_freeze_money_view() {
        return cash_freeze_money_view;
    }

    @JsonProperty("cash_freeze_money_view")
    public void setCash_freeze_money_view(String cash_freeze_money_view) {
        this.cash_freeze_money_view = cash_freeze_money_view;
    }

    public String getWill_profit_view() {
        return will_profit_view;
    }

    @JsonProperty("will_profit_view")
    public void setWill_profit_view(String will_profit_view) {
        this.will_profit_view = will_profit_view;
    }

    public String getWill_principal_view() {
        return will_principal_view;
    }

    @JsonProperty("will_principal_view")
    public void setWill_principal_view(String will_principal_view) {
        this.will_principal_view = will_principal_view;
    }

    public String getTotalAccountView() {
        return totalAccountView;
    }

    @JsonProperty("totalAccountView")
    public void setTotalAccountView(String totalAccountView) {
        this.totalAccountView = totalAccountView;
    }

    public String getPendingAmountView() {
        return pendingAmountView;
    }

    @JsonProperty("pendingAmountView")
    public void setPendingAmountView(String pendingAmountView) {
        this.pendingAmountView = pendingAmountView;
    }

    public String getInvesting_prj_count() {
        return investing_prj_count;
    }

    @JsonProperty("investing_prj_count")
    public void setInvesting_prj_count(String investing_prj_count) {
        this.investing_prj_count = investing_prj_count;
    }

    public String getInvesting_prj_money_view() {
        return investing_prj_money_view;
    }

    @JsonProperty("investing_prj_money_view")
    public void setInvesting_prj_money_view(String investing_prj_money_view) {
        this.investing_prj_money_view = investing_prj_money_view;
    }

    public String getWillrepay_prj_count() {
        return willrepay_prj_count;
    }

    @JsonProperty("willrepay_prj_count")
    public void setWillrepay_prj_count(String willrepay_prj_count) {
        this.willrepay_prj_count = willrepay_prj_count;
    }

    public String getWillrepay_prj_money_view() {
        return willrepay_prj_money_view;
    }

    @JsonProperty("willrepay_prj_money_view")
    public void setWillrepay_prj_money_view(String willrepay_prj_money_view) {
        this.willrepay_prj_money_view = willrepay_prj_money_view;
    }

    public String getRepayed_prj_count() {
        return repayed_prj_count;
    }

    @JsonProperty("repayed_prj_count")
    public void setRepayed_prj_count(String repayed_prj_count) {
        this.repayed_prj_count = repayed_prj_count;
    }

    public String getRepayed_prj_money_view() {
        return repayed_prj_money_view;
    }

    @JsonProperty("repayed_prj_money_view")
    public void setRepayed_prj_money_view(String repayed_prj_money_view) {
        this.repayed_prj_money_view = repayed_prj_money_view;
    }

    public String getTotalInvestCount() {
        return totalInvestCount;
    }

    @JsonProperty("totalInvestCount")
    public void setTotalInvestCount(String totalInvestCount) {
        this.totalInvestCount = totalInvestCount;
    }

    public String getTotalInvestView() {
        return totalInvestView;
    }

    @JsonProperty("totalInvestView")
    public void setTotalInvestView(String totalInvestView) {
        this.totalInvestView = totalInvestView;
    }

    public String getReward_money_view() {
        return reward_money_view;
    }

    @JsonProperty("reward_money_view")
    public void setReward_money_view(String reward_money_view) {
        this.reward_money_view = reward_money_view;
    }

    public String getTotal_reward_money() {
        return total_reward_money;
    }

    @JsonProperty("total_reward_money")
    public void setTotal_reward_money(String total_reward_money) {
        this.total_reward_money = total_reward_money;
    }

    public String getInvest_reward_money_view() {
        return invest_reward_money_view;
    }

    @JsonProperty("invest_reward_money_view")
    public void setInvest_reward_money_view(String invest_reward_money_view) {
        this.invest_reward_money_view = invest_reward_money_view;
    }

    public String getTotal_reward_money_view() {
        return total_reward_money_view;
    }

    @JsonProperty("total_reward_money_view")
    public void setTotal_reward_money_view(String total_reward_money_view) {
        this.total_reward_money_view = total_reward_money_view;
    }

    public String getRepayIn_count() {
        return repayIn_count;
    }

    @JsonProperty("repayIn_count")
    public void setRepayIn_count(String repayIn_count) {
        this.repayIn_count = repayIn_count;
    }

    public String getRepayIn_money_view() {
        return repayIn_money_view;
    }

    @JsonProperty("repayIn_money_view")
    public void setRepayIn_money_view(String repayIn_money_view) {
        this.repayIn_money_view = repayIn_money_view;
    }

    public String getRepay_total_amount() {
        return repay_total_amount;
    }

    @JsonProperty("repay_total_amount")
    public void setRepay_total_amount(String repay_total_amount) {
        this.repay_total_amount = repay_total_amount;
    }

    public String getRepay_prj_number() {
        return repay_prj_number;
    }

    @JsonProperty("repay_prj_number")
    public void setRepay_prj_number(String repay_prj_number) {
        this.repay_prj_number = repay_prj_number;
    }

    public String getRepay_repay_date() {
        return repay_repay_date;
    }

    @JsonProperty("repay_repay_date")
    public void setRepay_repay_date(String repay_repay_date) {
        this.repay_repay_date = repay_repay_date;
    }

    public String getLottery_num() {
        return lottery_num;
    }

    @JsonProperty("lottery_num")
    public void setLottery_num(String lottery_num) {
        this.lottery_num = lottery_num;
    }

    public String getSafe_level_label() {
        return safe_level_label;
    }

    @JsonProperty("safe_level_label")
    public void setSafe_level_label(String safe_level_label) {
        this.safe_level_label = safe_level_label;
    }

    public String getCount_transfer() {
        return count_transfer;
    }

    @JsonProperty("count_transfer")
    public void setCount_transfer(String count_transfer) {
        this.count_transfer = count_transfer;
    }

    public AccountTip_num getTip_num() {
        return tip_num;
    }

    @JsonProperty("tip_num")
    public void setTip_num(AccountTip_num tip_num) {
        this.tip_num = tip_num;
    }

    public String getTotalReward() {
        return totalReward;
    }

    @JsonProperty("totalReward")
    public void setTotalReward(String totalReward) {
        this.totalReward = totalReward;
    }

    public String getCurrent_month() {
        return current_month;
    }

    @JsonProperty("current_month")
    public void setCurrent_month(String current_month) {
        this.current_month = current_month;
    }

    public String getWill_repay_total_amont() {
        return will_repay_total_amont;
    }

    @JsonProperty("will_repay_total_amont")
    public void setWill_repay_total_amont(String will_repay_total_amont) {
        this.will_repay_total_amont = will_repay_total_amont;
    }

    public AccountChildData getMy_invest_info() {
        return my_invest_info;
    }

    @JsonProperty("my_invest_info")
    public void setMy_invest_info(AccountChildData my_invest_info) {
        this.my_invest_info = my_invest_info;
    }

    public String getRecharge_reward() {
        return recharge_reward;
    }

    @JsonProperty("recharge_reward")
    public void setRecharge_reward(String recharge_reward) {
        this.recharge_reward = recharge_reward;
    }

    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    public String getIs_safety_ins() {
        return is_safety_ins;
    }

    public void setIs_safety_ins(String is_safety_ins) {
        this.is_safety_ins = is_safety_ins;
    }

    public String getSafety_url() {
        return safety_url;
    }

    public void setSafety_url(String safety_url) {
        this.safety_url = safety_url;
    }
}
