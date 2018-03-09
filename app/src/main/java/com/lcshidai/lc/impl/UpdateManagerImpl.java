package com.lcshidai.lc.impl;

import android.content.Context;

import com.lcshidai.lc.model.pub.UpdateManagerJson;

public interface UpdateManagerImpl {

	void gainUpdateManagersuccess(UpdateManagerJson response, Context context, int flag);

	void gainUpdateManagerfail();

}
