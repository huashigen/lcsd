package com.lcshidai.lc.service;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.AgreementImpl;
import com.lcshidai.lc.model.pub.AgreementJson;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpAgreementService implements HttpServiceURL {
	TRJActivity mpa;
	AgreementImpl ai;
	private String GET_URL = "Mobile2/FinancList/protocolView";
	private String GET_URL_SDT = "Mobile2/FinancList/getProtocolView";

	public HttpAgreementService(TRJActivity mpa, AgreementImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainAgreementResult(int intent_flag, String mId, String fun, String name, String type) {

		if (null == mpa)
			return;
		String mUrl = (intent_flag == 1) ? GET_URL_SDT : GET_URL;
		RequestParams rq = new RequestParams();
		rq.put("id", mId);
		if (intent_flag == 1) {
			rq.put("fun", fun);
			rq.put("name", name);
			rq.put("type", type);
		}
		mpa.post(mUrl, rq, new BaseJsonHandler<AgreementJson>(mpa) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, AgreementJson response) {
				ai.gainAgreementSuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
					AgreementJson errorResponse) {
				ai.gainAgreementFail();
			}

			@Override
			protected AgreementJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData, isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), AgreementJson.class)
						.next();
			}

		});
	}
}