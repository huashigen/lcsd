package com.lcshidai.lc.model.more;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;
import com.lcshidai.lc.model.account.AccountSettingImg;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendImgData extends BaseData {
	AccountSettingImg attach;

	public RecommendImgData() {

	}

	public RecommendImgData(String me) {

	}


	public AccountSettingImg getAttach() {
		return attach;
	}

	@JsonProperty("attach")
	public void setAttach(AccountSettingImg attach) {
		this.attach = attach;
	}

	 

}
