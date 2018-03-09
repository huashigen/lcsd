package com.lcshidai.lc.service;

import org.apache.http.Header;

import android.content.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.impl.CrashHandlerImpl;
import com.lcshidai.lc.model.BaseJson;

public class HttpCrashHandlerService implements HttpServiceURL{
	Context mpa;
	CrashHandlerImpl ai;
	public HttpCrashHandlerService(Context mpa,
			CrashHandlerImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}
	
	public void gainCrashHandler(String model, String release, String versionName, String message) {
		if(null==mpa)return;
		RequestParams rp=new RequestParams();
		rp.put("phone_type", model);
		rp.put("phone_ver", "Android版本" + release+(versionName.equals("")?"":("@ver"+versionName)));
		rp.put("msg", message);
		LCHttpClient.post(mpa,EXCEPTION_LOG, rp, new BaseJsonHandler<BaseJson>(){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, BaseJson response) {
				ai.gainCrashHandlersuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					BaseJson errorResponse) {
				ai.gainCrashHandlerfail();
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
