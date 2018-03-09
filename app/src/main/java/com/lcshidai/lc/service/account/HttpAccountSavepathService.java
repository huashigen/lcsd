package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountSavepathImpl;
import com.lcshidai.lc.model.account.AccountSavepathJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpAccountSavepathService  implements HttpServiceURL{
	TRJActivity mpa;
	AccountSavepathImpl ai;
	public HttpAccountSavepathService(TRJActivity mpa,
			AccountSavepathImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}
	
	public void gainAccountSavepath(String path) {
		if(null==mpa)return;
		RequestParams params = new RequestParams();
		params.put("path", path);
		mpa.post(URL_SAVE_PATH, params, new BaseJsonHandler<AccountSavepathJson>(mpa){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, AccountSavepathJson response) {
				ai.gainAccountSavepathsuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					AccountSavepathJson errorResponse) {
				ai.gainAccountSavepathfail();
			}

			@Override
			protected AccountSavepathJson parseResponse(String rawJsonData,
					boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData,isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), AccountSavepathJson.class).next();
			}
			
		});
	}
}
