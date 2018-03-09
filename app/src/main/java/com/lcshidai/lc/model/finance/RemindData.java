package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemindData extends BaseData {

	public String remind_id;// -> 提醒Id（留用户切换是否提醒时用） remind 子元素
	public String is_available;// -> 是否提醒（1: 是, 0: 否）remind 子元素

	public RemindData() {

	}

	public RemindData(String me) {

	}

	public String getIs_available() {
		return is_available;
	}

	@JsonProperty("is_available")
	public void setIs_available(String is_available) {
		this.is_available = is_available;
	}

	public String getRemind_id() {
		return remind_id;
	}

	@JsonProperty("remind_id")
	public void setRemind_id(String remind_id) {
		this.remind_id = remind_id;
	}

}
