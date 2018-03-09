package com.lcshidai.lc.impl.transfer;

import com.lcshidai.lc.model.transfer.CashCheckJson;

public interface CashCheckImpl {
	void gainCashCheckSuccess(CashCheckJson response);

	void gainCashCheckFail();
}
