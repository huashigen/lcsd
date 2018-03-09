package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceHomePrjListData extends BaseData {
	 
 private FinaceHomeNewbieItem recommend;

	public FinaceHomeNewbieItem getRecommend() {
		return recommend;
	}

	public void setRecommend(FinaceHomeNewbieItem recommend) {
		this.recommend = recommend;
	}
}
