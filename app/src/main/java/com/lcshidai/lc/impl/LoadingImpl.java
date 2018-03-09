package com.lcshidai.lc.impl;

import com.lcshidai.lc.model.Loading.LoadingListJson;

public interface LoadingImpl {
	void gainLoadingSuccess(LoadingListJson loadinglistJson);
	void gainLoadingFailed();
}
