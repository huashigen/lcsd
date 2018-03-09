package com.lcshidai.lc.utils;

import java.lang.ref.WeakReference;

import com.lcshidai.lc.impl.finance.RewardCheckImpl;

/**
 * 奖励检查工具类
 */
public class RewardCheckUtil {
    // 这个值选择性重置
    private String reward_type = null;// 奖励类型（1-红包、2-满减券、3-加息券）
    private String bouns_rate = null;
    private String bouns_prj_term = null;
    private String mjq_id = null;// 满减券ID
    private String amount = null;
    private String jxq_id = null;// 加息券ID
    private String jx_rate = null;// 加息券利率
    // 这个值不能重置
    private boolean is_perform_clicked = false;// 是否进行了选择券红包的动作（包括选择和取消选择）

    private WeakReference<RewardCheckImpl> rewardCheckReference;

    private static RewardCheckUtil instance = new RewardCheckUtil();

    private RewardCheckUtil() {

    }

    public static RewardCheckUtil getInstance() {
        return instance == null ? new RewardCheckUtil() : instance;
    }

    /**
     * 重置红包满减券
     */
    public void resetHbMjq() {
        reward_type = null;
        bouns_rate = null;
        bouns_prj_term = null;
        mjq_id = null;
        amount = null;
    }

    /**
     * 重置加息券
     */
    public void resetJxq() {
        jxq_id = null;
        jx_rate = null;
    }

    /**
     * 重置所有
     */
    public void resetAll() {
        reward_type = null;
        bouns_rate = null;
        bouns_prj_term = null;
        mjq_id = null;
        amount = null;
        jxq_id = null;
        jx_rate = null;
    }

    public String getReward_type() {
        return reward_type;
    }

    public void setReward_type(String reward_type) {
        this.reward_type = reward_type;
    }

    public String getBouns_rate() {
        return bouns_rate;
    }

    public void setBouns_rate(String bouns_rate) {
        this.bouns_rate = bouns_rate;
    }

    public String getBouns_prj_term() {
        return bouns_prj_term;
    }

    public void setBouns_prj_term(String bouns_prj_term) {
        this.bouns_prj_term = bouns_prj_term;
    }

    public String getMjq_id() {
        return mjq_id;
    }

    public void setMjq_id(String mjq_id) {
        this.mjq_id = mjq_id;
    }

    public String getJxq_id() {
        return jxq_id;
    }

    public void setJxq_id(String jxq_id) {
        this.jxq_id = jxq_id;
    }

    public String getJx_rate() {
        return jx_rate;
    }

    public void setJx_rate(String jx_rate) {
        this.jx_rate = jx_rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean is_perform_clicked() {
        return is_perform_clicked;
    }

    public void setIs_perform_clicked(boolean is_perform_clicked) {
        this.is_perform_clicked = is_perform_clicked;
    }

    public WeakReference<RewardCheckImpl> getHBCheckReference() {
        return rewardCheckReference;
    }

    public void setHBCheckReference(WeakReference<RewardCheckImpl> hBCheckReference) {
        this.rewardCheckReference = hBCheckReference;
    }

    @Override
    public String toString() {
        return String.format("reward_type = %s, bouns_rate = %s, bouns_prj_term = %s, mjq_id = %s， amount = %s",
                reward_type, bouns_rate, bouns_prj_term, mjq_id, amount);
    }

}
