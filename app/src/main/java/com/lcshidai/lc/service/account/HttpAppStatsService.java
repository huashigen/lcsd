package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AppStatsImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpAppStatsService implements HttpServiceURL{
	TRJActivity mpa;
	AppStatsImpl ai;
	public HttpAppStatsService(TRJActivity mpa,
			AppStatsImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}
	
	public void gainAppStats(boolean flag) {
		if(null==mpa)return;
		String url;
		if (flag) {
			url = URL_STATS_A1;
		}else {
			url = URL_STATS_A2;
		}
		RequestParams rp=new RequestParams();
		mpa.post(url, rp, new BaseJsonHandler<BaseJson>(mpa){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, BaseJson response) {
				ai.gainAppStatssuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					BaseJson errorResponse) {
				ai.gainAppStatsfail();
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
