package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.NavigationImpl;
import com.lcshidai.lc.model.finance.FinaceNavigationJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

/**
 * 
 * @author 001355
 */
public class HttpNavigationService implements HttpServiceURL {
	TRJActivity mpa;
	NavigationImpl ai;

	public HttpNavigationService(TRJActivity mpa, NavigationImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainNavigation() {
		if (null == mpa)
			return;
		RequestParams params = new RequestParams();
		
		LCHttpClient.get(mpa,HOME_NAVIGATION, params,
				new BaseJsonHandler<FinaceNavigationJson>(mpa) {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String rawJsonResponse,
							FinaceNavigationJson response) {
						ai.gainNavigationSuccess(response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, String rawJsonData,
							FinaceNavigationJson errorResponse) {
						ai.gainNavigationFail();
					}

					@Override
					protected FinaceNavigationJson parseResponse(
							String rawJsonData, boolean isFailure)
							throws Throwable {
						super.parseResponse(rawJsonData, isFailure);
						return new XHHMapper().readValues(
								new JsonFactory().createParser(rawJsonData),
								FinaceNavigationJson.class).next();
					}

				});
	}
}
