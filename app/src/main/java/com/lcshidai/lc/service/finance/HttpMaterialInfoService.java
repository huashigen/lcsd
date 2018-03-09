package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.MaterialInfoImpl;
import com.lcshidai.lc.model.finance.MaterialInfoListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
/**
 * 
 * @author 001355
 */
public class HttpMaterialInfoService implements HttpServiceURL {
	TRJActivity mpa;
	MaterialInfoImpl ai;

	public HttpMaterialInfoService(TRJActivity mpa, MaterialInfoImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainMaterialInfo(String prj_id) {
		if (null == mpa)
			return;
		RequestParams params = new RequestParams();
		params.put("prj_id", prj_id);// prj_id 项目id 
		mpa.post(MATERIAL_INFO, params,
				new BaseJsonHandler<MaterialInfoListJson>(mpa) {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String rawJsonResponse,
							MaterialInfoListJson response) {
						ai.gainMaterialInfosuccess(response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, String rawJsonData,
							MaterialInfoListJson errorResponse) {
						ai.gainMaterialInfofail();
					}

					@Override
					protected MaterialInfoListJson parseResponse(
							String rawJsonData, boolean isFailure)
							throws Throwable {
						super.parseResponse(rawJsonData, isFailure);
						return new XHHMapper().readValues(
								new JsonFactory().createParser(rawJsonData),
								MaterialInfoListJson.class).next();
					}

				});
	}
}
