package com.lcshidai.lc.impl;

import com.lcshidai.lc.model.MessageJson;

public interface MessageImpl {
	void gainMessageSuccess(MessageJson response);

	void gainMessageFail();
}
