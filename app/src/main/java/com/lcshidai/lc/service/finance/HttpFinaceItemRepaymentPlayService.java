package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.FinaceItemRepaymentPlayImpl;
import com.lcshidai.lc.model.finance.FinaceItemRepaymentPlayListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

/**
 * 
 * @author 001355
 * @Description: 回款计划
 */
public class HttpFinaceItemRepaymentPlayService implements HttpServiceURL {
	TRJActivity mpa;
	FinaceItemRepaymentPlayImpl ai;

	public HttpFinaceItemRepaymentPlayService(TRJActivity mpa,
			FinaceItemRepaymentPlayImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainFinaceItemRepaymentPlay(String mPrjId) {
		// String url = mIsTransfer ? LOAD_URL_ORD : LOAD_URL_PRJ;
		RequestParams params = new RequestParams();
		params.put("prj_id", mPrjId);// prj_id 项目id
		mpa.post(FINACE_ITEM_REPAYMENT_PLAY, params,
				new BaseJsonHandler<FinaceItemRepaymentPlayListJson>(mpa) {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String rawJsonResponse,
							FinaceItemRepaymentPlayListJson response) {
						ai.gainFinaceItemRepaymentPlaysuccess(response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, String rawJsonData,
							FinaceItemRepaymentPlayListJson errorResponse) {
						ai.gainFinaceItemRepaymentPlayfail();
					}

					@Override
					protected FinaceItemRepaymentPlayListJson parseResponse(
							String rawJsonData, boolean isFailure)
							throws Throwable {
						super.parseResponse(rawJsonData, isFailure);
						return new XHHMapper().readValues(
								new JsonFactory().createParser(rawJsonData),
								FinaceItemRepaymentPlayListJson.class).next();
					}

				});
	}
}
