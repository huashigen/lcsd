package com.lcshidai.lc.impl;

import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;

/**
 * 弹出框的检查和购买请求成功回调
 * @author 000814
 *
 */
public interface PIPWCallBack {
	void doInvestCheckSuccess(boolean success, FinanceInvestPBuyCheckData model);
	void callPayCheckBack(boolean success);
	void doInvestSuccess(boolean success);
}
