package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.SearchConditionImpl;
import com.lcshidai.lc.model.finance.SearchConditionJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

/**
 * 
 * @author 001355
 */
public class HttpSearchConditionService implements HttpServiceURL {
	TRJActivity mpa;
	SearchConditionImpl ai;

	public HttpSearchConditionService(TRJActivity mpa, SearchConditionImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainSearchCondition() {
		if (null == mpa)
			return;
		RequestParams params = new RequestParams();
		
		LCHttpClient.get(mpa,SEARCH_CONDITION, params,
				new BaseJsonHandler<SearchConditionJson>(mpa) {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String rawJsonResponse,
							SearchConditionJson response) {
						ai.gainSearchConditionSuccess(response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, String rawJsonData,
							SearchConditionJson errorResponse) {
						ai.gainSearchConditionFail();
					}

					@Override
					protected SearchConditionJson parseResponse(
							String rawJsonData, boolean isFailure)
							throws Throwable {
						super.parseResponse(rawJsonData, isFailure);
						return new XHHMapper().readValues(
								new JsonFactory().createParser(rawJsonData),
								SearchConditionJson.class).next();
					}

				});
	}
}
