package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.BalancePaymentsImpl;
import com.lcshidai.lc.model.account.BalancePaymentsJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpBalancePaymentsService implements HttpServiceURL{
	TRJActivity mpa;
	BalancePaymentsImpl ai;
	public HttpBalancePaymentsService(TRJActivity mpa,
			BalancePaymentsImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}
	public void gainBalancePayments(int selectStatus, int currentPage,
			int perPage) {
		if(null==mpa)return;
		RequestParams rp=new RequestParams();
		if(selectStatus != -1){
			rp.put("status", String.valueOf(selectStatus));
		}
		rp.put("p", String.valueOf(currentPage));
		rp.put("perpage", String.valueOf(perPage));
		mpa.post(BALANCE_PAYMENTS_URL, rp, new BaseJsonHandler<BalancePaymentsJson>(mpa){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, BalancePaymentsJson response) {
				ai.gainBalancePaymentssuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					BalancePaymentsJson errorResponse) {
				ai.gainBalancePaymentsfail();
			}

			@Override
			protected BalancePaymentsJson parseResponse(String rawJsonData,
					boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData,isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BalancePaymentsJson.class).next();
			}
			
		});
	}
}
