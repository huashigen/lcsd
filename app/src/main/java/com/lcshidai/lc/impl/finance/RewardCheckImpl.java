package com.lcshidai.lc.impl.finance;

/**
 * 奖励检查回调接口
 */
public interface RewardCheckImpl {

    /**
     * 更新红包、满减券信息
     */
    void updateHbMjq();


    /**
     * 更新使用加息券时得到的收益
     */
    void updateJxq(boolean isUpdateIncome);

}
