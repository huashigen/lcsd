package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.FinanceHBImpl;
import com.lcshidai.lc.model.finance.FinanceHBJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpFinanceHBService implements HttpServiceURL {

	FinanceHBImpl impl;
	TRJActivity mActivity;

	public HttpFinanceHBService(FinanceHBImpl impl, TRJActivity mActivity) {
		this.impl = impl;
		this.mActivity = mActivity;
	}

	public void getPrjBonus(String prj_id, String amount, int isCollection) {
		RequestParams rp = new RequestParams();
		rp.put("prj_id", prj_id);
		rp.put("amount", amount);
		rp.put("is_collection", isCollection);
		mActivity.post(GET_PRJ_BONUS_LIST, rp, new BaseJsonHandler<FinanceHBJson>(mActivity) {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String rawJsonResponse,
							FinanceHBJson response) {
						impl.gainFinaceBonusSuccess(response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, String rawJsonData,
							FinanceHBJson errorResponse) {
						impl.gainFinaceBonusFail();
					}

					@Override
					protected FinanceHBJson parseResponse(
							String rawJsonData, boolean isFailure)
							throws Throwable {
						super.parseResponse(rawJsonData, isFailure);
						return new XHHMapper().readValues(
								new JsonFactory().createParser(rawJsonData),
								FinanceHBJson.class).next();
					}

				});

	}
}
