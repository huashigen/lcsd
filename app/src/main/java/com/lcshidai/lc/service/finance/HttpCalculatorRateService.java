package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.CalculatorRateImpl;
import com.lcshidai.lc.model.finance.CalculatorRateJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpCalculatorRateService implements HttpServiceURL{
	TRJActivity mpa;
	CalculatorRateImpl ai;
	public HttpCalculatorRateService(TRJActivity mpa,
			CalculatorRateImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}
	
	public void gainCalculatorRate(String prj_id, String money) {
		if(null==mpa)return;
		RequestParams rp=new RequestParams();
		rp.put("prj_id", prj_id);
		rp.put("money", money);
		mpa.post(CALCULATOR_URL, rp, new BaseJsonHandler<CalculatorRateJson>(mpa){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, CalculatorRateJson response) {
				ai.gainCalculatorRatesuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					CalculatorRateJson errorResponse) {
				ai.gainCalculatorRatefail();
			}

			@Override
			protected CalculatorRateJson parseResponse(String rawJsonData,
					boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData,isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), CalculatorRateJson.class).next();
			}
			
		});
	}
}
