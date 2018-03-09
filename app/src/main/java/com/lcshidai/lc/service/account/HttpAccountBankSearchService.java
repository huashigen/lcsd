package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountBankSearchImpl;
import com.lcshidai.lc.model.account.AccountBankSearchJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpAccountBankSearchService implements HttpServiceURL{
	TRJActivity mpa;
	AccountBankSearchImpl ai;
	public HttpAccountBankSearchService(TRJActivity mpa,
			AccountBankSearchImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}
	public void gainAccountBankSearch(String mCode, String pcode, String ccode, String key) {
		if(null==mpa)return;
		RequestParams rp=new RequestParams();
		rp.put("bank_code", mCode);
		rp.put("temp_pcode", pcode);
		rp.put("code", ccode);
		rp.put("key_word", key);
		mpa.post(URL_GET_LIST, rp, new BaseJsonHandler<AccountBankSearchJson>(mpa){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, AccountBankSearchJson response) {
				ai.gainAccountBankSearchsuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					AccountBankSearchJson errorResponse) {
				ai.gainAccountBankSearchfail();
			}

			@Override
			protected AccountBankSearchJson parseResponse(String rawJsonData,
					boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData,isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), AccountBankSearchJson.class).next();
			}
			
		});
	}
}
