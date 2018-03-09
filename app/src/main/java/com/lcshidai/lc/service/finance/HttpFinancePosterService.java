package com.lcshidai.lc.service.finance;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.FinancePosterImpl;
import com.lcshidai.lc.model.finance.FinancePosterJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;

import org.apache.http.Header;

/**
 * 
 * @author 001355
 */
public class HttpFinancePosterService implements HttpServiceURL {
	TRJActivity mpa;
	FinancePosterImpl ai;

	public HttpFinancePosterService(TRJActivity mpa, FinancePosterImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainPoster() {
		if (null == mpa)
			return;
		RequestParams params = new RequestParams();
		params.put("device", CommonUtil.getDeviceId(mpa));
		LCHttpClient.get(mpa,FINANCE_POSTER, params,
				new BaseJsonHandler<FinancePosterJson>(mpa) {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String rawJsonResponse,
										  FinancePosterJson response) {
						ai.gainFinancePostersuccess(response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, String rawJsonData,
										  FinancePosterJson errorResponse) {
						ai.gainFinancePosterfail();
					}

					@Override
					protected FinancePosterJson parseResponse(
							String rawJsonData, boolean isFailure)
							throws Throwable {
						super.parseResponse(rawJsonData, isFailure);
						return new XHHMapper().readValues(
								new JsonFactory().createParser(rawJsonData),
								FinancePosterJson.class).next();
					}

				});
	}
}
