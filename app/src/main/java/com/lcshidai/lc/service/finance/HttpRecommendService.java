package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;

import com.lcshidai.lc.impl.finance.RecommendInfoImpl;
import com.lcshidai.lc.model.finance.FinaceHomeJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

/**
 * 
 * @author 001355
 */
public class HttpRecommendService implements HttpServiceURL {
	TRJActivity mpa;
	RecommendInfoImpl ai;

	public HttpRecommendService(TRJActivity mpa, RecommendInfoImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainRecommendInfo() {
		if (null == mpa)
			return;
		RequestParams params = new RequestParams();
		
		LCHttpClient.get(mpa,TOGGLE_RECOMMEND_NEW, params,
				new BaseJsonHandler<FinaceHomeJson>(mpa) {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String rawJsonResponse,
							FinaceHomeJson response) {
						ai.gainRecommendsuccess(response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, String rawJsonData,
							FinaceHomeJson errorResponse) {
						ai.gainRecommendfail();
					}

					@Override
					protected FinaceHomeJson parseResponse(
							String rawJsonData, boolean isFailure)
							throws Throwable {
						super.parseResponse(rawJsonData, isFailure);
						return new XHHMapper().readValues(
								new JsonFactory().createParser(rawJsonData),
								FinaceHomeJson.class).next();
					}

				});
	}
}
