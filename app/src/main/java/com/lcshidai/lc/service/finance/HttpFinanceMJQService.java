package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.FinanceMJQImpl;
import com.lcshidai.lc.model.finance.FinanceMJQJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpFinanceMJQService implements HttpServiceURL {

	FinanceMJQImpl impl;
	TRJActivity mActivity;

	public HttpFinanceMJQService(FinanceMJQImpl impl, TRJActivity mActivity) {
		this.impl = impl;
		this.mActivity = mActivity;
	}

	public void getPrjTickets(String prj_id, String amount, int isCollection) {
		RequestParams rp = new RequestParams();
		rp.put("prj_id", prj_id);
		rp.put("amount", amount);
		rp.put("is_collection", isCollection);
		mActivity.post(GET_PRJ_TICKETS_LIST, rp, new BaseJsonHandler<FinanceMJQJson>(mActivity) {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String rawJsonResponse,
							FinanceMJQJson response) {
						impl.gainFinaceTicketsSuccess(response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, String rawJsonData,
							FinanceMJQJson errorResponse) {
						impl.gainFinaceTicketsFail();
					}

					@Override
					protected FinanceMJQJson parseResponse(
							String rawJsonData, boolean isFailure)
							throws Throwable {
						super.parseResponse(rawJsonData, isFailure);
						return new XHHMapper().readValues(
								new JsonFactory().createParser(rawJsonData),
								FinanceMJQJson.class).next();
					}

				});

	}
}
