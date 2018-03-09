package com.lcshidai.lc.service.account;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountSettingImpl;
import com.lcshidai.lc.model.account.AccountSettingJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpAccountSettingService implements HttpServiceURL {
	TRJActivity mpa;
	AccountSettingImpl ai;

	public HttpAccountSettingService(TRJActivity mpa,
									 AccountSettingImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainAccountSetting(String uid) {
		if (null == mpa) return;
		RequestParams rp = new RequestParams();
		mpa.post(GET_ACCOUNT_SETTING, rp, new BaseJsonHandler<AccountSettingJson>(mpa) {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
								  String rawJsonResponse, AccountSettingJson response) {
				ai.gainAccountSettingSuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
								  Throwable throwable, String rawJsonData,
								  AccountSettingJson errorResponse) {
				ai.gainAccountSettingFail();
			}

			@Override
			protected AccountSettingJson parseResponse(String rawJsonData,
													   boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData, isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData),
						AccountSettingJson.class).next();
			}

		});
	}
}
