package com.lcshidai.lc.service.more;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.more.InviteImpl;
import com.lcshidai.lc.model.more.InviteJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;

public class HttpInviteService implements HttpServiceURL {
	TRJActivity mpa;
	InviteImpl ai;

	public HttpInviteService(TRJActivity mpa, InviteImpl ai) {
		this.mpa = mpa;
		this.ai = ai;
	}

	public void gainInvite() {
		if (null == mpa)
			return;
		RequestParams rp = new RequestParams();

		mpa.post(INVITE_URL, rp, new BaseJsonHandler<InviteJson>(mpa) {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String rawJsonResponse, InviteJson response) {
				ai.getInviteInfoSuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, String rawJsonData,
					InviteJson errorResponse) {
				ai.getInviteInfoFailed();
			}

			@Override
			protected InviteJson parseResponse(String rawJsonData,
					boolean isFailure) throws Throwable {
				super.parseResponse(rawJsonData, isFailure);
				return new XHHMapper().readValues(
						new JsonFactory().createParser(rawJsonData),
						InviteJson.class).next();
			}

		});
	}
}
