package com.lcshidai.lc.impl.finance;

import com.lcshidai.lc.model.finance.HotInfoMovementJson;

public interface HotInfoMovementImpl {

	void gainHotMovementSuccess(HotInfoMovementJson response);

	void gainHotMovementFail();

}
