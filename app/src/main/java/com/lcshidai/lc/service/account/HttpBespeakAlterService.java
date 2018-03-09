package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.BespeakAlterImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpBespeakAlterService implements HttpServiceURL{
	TRJActivity mpa;
	BespeakAlterImpl ai;
	public HttpBespeakAlterService(TRJActivity mpa,
			BespeakAlterImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}
	
	public void gainBespeakAlter(String bespeak_id_new, String pwd, String min_rate, String min_time, String min_money, String prj_type_a, String prj_type_b, String prj_type_f, String prj_type_h) {
		if(null==mpa)return;
		RequestParams params=new RequestParams();
		params.put("appoint_id", bespeak_id_new);
		params.put("safe_pwd", pwd);
		params.put("appoint_rate", min_rate);
		params.put("appoint_day", min_time);
		params.put("appoint_money", min_money);
		params.put("prj_type_a", prj_type_a);
		params.put("prj_type_b", prj_type_b);
		params.put("prj_type_f", prj_type_f);
		params.put("prj_type_h", prj_type_h);
		params.put("is_agree_agreement", "1");
		mpa.post(BESPEAK_ALTER, params, new BaseJsonHandler<BaseJson>(mpa){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, BaseJson response) {
				ai.gainBespeakAltersuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					BaseJson errorResponse) {
				ai.gainBespeakAlterfail();
			}

			@Override
			protected BaseJson parseResponse(String rawJsonData,
					boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData,isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BaseJson.class).next();
			}
			
		});
	}
}
