package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.lcshidai.lc.http.LCHttpClient;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.BespeakRecordListImpl;
import com.lcshidai.lc.model.account.BespeakRecordListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpBespeakRecordListService implements HttpServiceURL{
	TRJActivity mpa;
	BespeakRecordListImpl ai;
	public HttpBespeakRecordListService(TRJActivity mpa,
			BespeakRecordListImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}
	public void gainBespeakRecordListData(int nowPage, int avePage) {
		if(null==mpa)return;
		RequestParams rp=new RequestParams();
		rp.put("page", String.valueOf(nowPage));
		rp.put("num", String.valueOf(avePage));
		rp.put("status", "");
		LCHttpClient.get(mpa, BESPEAK_RECORD_LIST, rp,  new BaseJsonHandler<BespeakRecordListJson>(mpa){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, BespeakRecordListJson response) {
				ai.gainBespeakRecordListsuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					BespeakRecordListJson errorResponse) {
				ai.gainBespeakRecordListfail();
			}

			@Override
			protected BespeakRecordListJson parseResponse(String rawJsonData,
					boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData,isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), BespeakRecordListJson.class).next();
			}
			
		});
	}
}
