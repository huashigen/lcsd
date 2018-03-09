package com.lcshidai.lc.service.transfer;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.transfer.CashCheckImpl;
import com.lcshidai.lc.model.transfer.CashCheckJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpCashCheckService implements HttpServiceURL {
	TRJActivity mpa;
	CashCheckImpl ai;

	public HttpCashCheckService(TRJActivity mpa, CashCheckImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainCashCheckResult(String prj_order_id, String cash_money, String cash_rate) {
		if (null == mpa)
			return;
		RequestParams params = new RequestParams();
		params.put("prj_order_id", prj_order_id);//
		params.put("cash_money", cash_money);//
		params.put("cash_rate", cash_rate);//
		mpa.post(CASH_CHECK_URL, params, new BaseJsonHandler<CashCheckJson>(mpa) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, CashCheckJson response) {
				ai.gainCashCheckSuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
					CashCheckJson errorResponse) {
				ai.gainCashCheckFail();
			}

			@Override
			protected CashCheckJson parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData, isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), CashCheckJson.class)
						.next();
			}

		});
	}
}