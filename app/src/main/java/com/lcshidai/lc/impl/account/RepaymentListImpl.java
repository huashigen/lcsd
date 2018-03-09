package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.RepaymentListJson;

public interface RepaymentListImpl {

	void gainRepaymentListsuccess(RepaymentListJson response);

	void gainRepaymentListfail();

}
