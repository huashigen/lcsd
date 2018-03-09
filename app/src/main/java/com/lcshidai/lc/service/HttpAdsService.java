package com.lcshidai.lc.service;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.AdsImpl;
import com.lcshidai.lc.model.AdsJson;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpAdsService implements HttpServiceURL {
	TRJActivity mpa;
	AdsImpl ai;

	public HttpAdsService(TRJActivity mpa, AdsImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void getAdsByUid() {

		if (null == mpa)
			return;
		RequestParams rq = new RequestParams();
		mpa.post(ADS_BY_UID, rq, new BaseJsonHandler<AdsJson>(mpa) {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, AdsJson response) {
				ai.getAdsSuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
					AdsJson errorResponse) {
				ai.getAdsFail();
			}
			
			@Override
			protected AdsJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData, isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), AdsJson.class).next();
			}

		});
	}
}