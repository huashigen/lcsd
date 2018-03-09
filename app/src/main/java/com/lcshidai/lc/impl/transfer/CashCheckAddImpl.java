package com.lcshidai.lc.impl.transfer;

import com.lcshidai.lc.model.transfer.CashCheckAddJson;

public interface CashCheckAddImpl {
	void gainCashCheckAddSuccess(CashCheckAddJson response);

	void gainCashCheckAddFail();
}
