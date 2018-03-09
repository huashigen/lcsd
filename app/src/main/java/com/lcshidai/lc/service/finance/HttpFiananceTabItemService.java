package com.lcshidai.lc.service.finance;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.http.BaseJsonHandler;
import com.lcshidai.lc.impl.finance.FinanceTabItemImpl;
import com.lcshidai.lc.model.finance.FinanceTabItemJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.http.XHHMapper;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.StringUtils;

public class HttpFiananceTabItemService implements HttpServiceURL{
	FinanceTabItemImpl impl;
	TRJActivity mpa;
	
	public HttpFiananceTabItemService(TRJActivity mpa,
			FinanceTabItemImpl impl) {
		this.mpa = mpa;
		this.impl = impl;
	}
	
	public void gainPrjList(boolean isSu,String homeFlag,boolean isVinvest,String mCurrentPType,String mMyorder,String searchStr,String filter,int p,int pageSize, String search_param){
		if(mpa==null)return;
		RequestParams params=new RequestParams();
		if(search_param != null){
			params.put("search_param", search_param);
		}
		if (mCurrentPType != null)
			params.put("prj_series", mCurrentPType);// prjtype 产品类型 1-短期宝 2-企益升
													// 3-稳益保 4-抵益融 0或者不传代表所有
		params.put("order", mMyorder.equals("4") ? "3" : mMyorder);//
		if (isSu) {
			if (mMyorder.equals("4")) {
				params.put("sort", "asc");//
			} else {
				params.put("sort", "desc");//
			}
			if (searchStr != null)
				params.put("prj_name", searchStr);//
		}
		if (mCurrentPType != null && mCurrentPType.equals("7")) {// yq付页签
			params.put("filter", "97");
		} else if (!filter.equals("")) {
			params.put("filter", filter);
		}
		params.put("p", String.valueOf(p));// p 分页页数
		params.put("page_size", pageSize + "");// 每页多少条 ，不传默认每页10条
		mpa.post(StringUtils.isEmpty(homeFlag) ? (isVinvest ? GET_EXPERIENCE_PROJECT_LIST
				: GET_PROJECT_LIST) : TOGGLE_RECOMMEND, params, new BaseJsonHandler<FinanceTabItemJson>(mpa){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						String rawJsonResponse, FinanceTabItemJson response) {
					// TODO Auto-generated method stub
					impl.gainFinanceTabItemSuccess(response);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, String rawJsonData,
						FinanceTabItemJson errorResponse) {
					// TODO Auto-generated method stub
					impl.gainFinanceTabItemFail();
				}

				@Override
				protected FinanceTabItemJson parseResponse(String rawJsonData,
						boolean isFailure) throws Throwable {
					// TODO Auto-generated method stub
					super.parseResponse(rawJsonData,isFailure);
					return new XHHMapper().readValues(new JsonFactory().createParser(rawJsonData), FinanceTabItemJson.class).next();
				}
				
			});
	}
}
