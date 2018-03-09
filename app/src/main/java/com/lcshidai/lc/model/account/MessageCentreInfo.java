package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageCentreInfo extends BaseData{
	private String id;
	private String remind_title;
	private String remind_message;
	private String show_time;
	private String remind_type;
	private String prj_type; // 速转让-H
	private String prj_id;

	public MessageCentreInfo() {

	}

	public MessageCentreInfo(String me) {

	}
	
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getRemind_title() {
		return remind_title;
	}

	@JsonProperty("remind_title")
	public void setRemind_title(String remind_title) {
		this.remind_title = remind_title;
	}

	public String getRemind_message() {
		return remind_message;
	}

	@JsonProperty("remind_message")
	public void setRemind_message(String remind_message) {
		this.remind_message = remind_message;
	}

	public String getShow_time() {
		return show_time;
	}

	@JsonProperty("show_time")
	public void setShow_time(String show_time) {
		this.show_time = show_time;
	}

	public String getRemind_type() {
		return remind_type;
	}

	@JsonProperty("remind_type")
	public void setRemind_type(String remind_type) {
		this.remind_type = remind_type;
	}

	public String getPrj_type() {
		return prj_type;
	}

	@JsonProperty("prj_type")
	public void setPrj_type(String prj_type) {
		this.prj_type = prj_type;
	}

	public String getPrj_id() {
		return prj_id;
	}

	@JsonProperty("prj_id")
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}

}
