package com.lcshidai.lc.service.more;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.more.RecommendImpl;
import com.lcshidai.lc.model.more.RecommendListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
/**
 * 
 * @author 001355
 * @Description: 推荐应用
 */
public class HttpRecommendService implements HttpServiceURL {
	TRJActivity mpa;
	RecommendImpl ai;

	public HttpRecommendService(TRJActivity mpa, RecommendImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainRecommend(String type) {
		if (null == mpa)
			return;
		RequestParams params = new RequestParams();
		params.put("type", type);
		mpa.post(GET_BANNER_LIST, params, new BaseJsonHandler<RecommendListJson>(mpa) {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, RecommendListJson response) {
				ai.gainRecommendsuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, String rawJsonData,
					RecommendListJson errorResponse) {
				ai.gainRecommendfail();
			}

			@Override
			protected RecommendListJson parseResponse(String rawJsonData,
					boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData, isFailure);
				return new XHHMapper().readValues(
						new JsonFactory().createParser(rawJsonData),
						RecommendListJson.class).next();
			}

		});
	}
}
