package com.lcshidai.lc.service.account;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.account.AccountSaveimgImpl;
import com.lcshidai.lc.model.account.AccountSaveimgJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.FileUtils;

public class HttpAccountSaveimgService  implements HttpServiceURL{
	TRJActivity mpa;
	AccountSaveimgImpl ai;
	public HttpAccountSaveimgService(TRJActivity mpa,
			AccountSaveimgImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}
	
	public void gainAccountSaveimg(File result) {
		if(null==mpa)return;
		String attach_type = FileUtils.getFileType(result.getName());
		RequestParams params = new RequestParams();
		try {
			params.put("Filedata", result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		params.put("attach_type", attach_type);
		mpa.post(URL_SAVE_IMG, params, new BaseJsonHandler<AccountSaveimgJson>(mpa){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, AccountSaveimgJson response) {
				ai.gainAccountSaveimgsuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					AccountSaveimgJson errorResponse) {
				ai.gainAccountSaveimgfail();
			}

			@Override
			protected AccountSaveimgJson parseResponse(String rawJsonData,
					boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData,isFailure);
				return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), AccountSaveimgJson.class).next();
			}
			
		});
	}
}
