package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.BespeakApplyInfoImpl;
import com.lcshidai.lc.model.account.BespeakApplyInfoJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpBespeakApplyInfoService implements HttpServiceURL{
	TRJActivity mpa;
	BespeakApplyInfoImpl ai;
	public HttpBespeakApplyInfoService(TRJActivity mpa,
			BespeakApplyInfoImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}
	
	public void gainBespeakApplyInfo(String bespeak_id_new) {
		if(null==mpa)return;
		RequestParams rp=new RequestParams();
		rp.put("appoint_id", bespeak_id_new);
		mpa.post(BESEPAK_APPLY_INFO_URL, rp, new BaseJsonHandler<BespeakApplyInfoJson>(mpa){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, BespeakApplyInfoJson response) {
				ai.gainBespeakApplyInfosuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					BespeakApplyInfoJson errorResponse) {
				ai.gainBespeakApplyInfofail();
			}

			@Override
			protected BespeakApplyInfoJson parseResponse(String rawJsonData,
					boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData,isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BespeakApplyInfoJson.class).next();
			}
			
		});
	}
}
