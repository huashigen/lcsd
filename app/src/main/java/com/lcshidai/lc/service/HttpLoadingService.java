package com.lcshidai.lc.service;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.LoadingImpl;
import com.lcshidai.lc.model.Loading.LoadingListJson;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpLoadingService implements HttpServiceURL {
	TRJActivity mpa;
	LoadingImpl ai;

	public HttpLoadingService(TRJActivity mpa, LoadingImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainAccountBankSearch( ) {

		if (null == mpa)
			return;
		RequestParams params = new RequestParams();
		params.put("platform", "1");
		mpa.post(LOADING_URL, params, new BaseJsonHandler<LoadingListJson>(mpa) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, LoadingListJson response) {
				ai.gainLoadingSuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
					LoadingListJson errorResponse) {
				ai.gainLoadingFailed();
			}

			@Override
			protected LoadingListJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData, isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), LoadingListJson.class)
						.next();
			}

		});
	}
}