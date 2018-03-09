package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotInfoMovementData extends BaseData {
	public String id;
	public String name;
	public String start_time;
	public String end_time;
	public String status;
	public String activity_status;
	public String memo;
	public String content;
	public String pc_url;
	public String mobile_url;
	public String list_image;
	public String ctime;
	public String mtime;
	public String image_100 ;
	public String image_39 ;
	public String image_37 ;
	public String image_38 ;
	public String image_1000 ;
	
	
	public String getImage_100() {
		return image_100;
	}
	@JsonProperty("image_100")
	public void setImage_100(String image_100) {
		this.image_100 = image_100;
	}
	
	public String getImage_39() {
		return image_39;
	}
	@JsonProperty("image_39")
	public void setImage_39(String image_39) {
		this.image_39 = image_39;
	}
	
	public String getImage_37() {
		return image_37;
	}
	@JsonProperty("image_37")
	public void setImage_37(String image_37) {
		this.image_37 = image_37;
	}
	
	public String getImage_38() {
		return image_38;
	}
	@JsonProperty("image_38")
	public void setImage_38(String image_38) {
		this.image_38 = image_38;
	}
	
	public String getImage_1000() {
		return image_1000;
	}
	@JsonProperty("image_1000")
	public void setImage_1000(String image_1000) {
		this.image_1000 = image_1000;
	}
	
	public String getId() {
		return id;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	public String getStart_time() {
		return start_time;
	}
	@JsonProperty("start_time")
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}
	@JsonProperty("end_time")
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getStatus() {
		return status;
	}
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getActivity_status() {
		return activity_status;
	}
	@JsonProperty("activity_status")
	public void setActivity_status(String activity_status) {
		this.activity_status = activity_status;
	}

	public String getMemo() {
		return memo;
	}
	@JsonProperty("memo")
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getContent() {
		return content;
	}
	@JsonProperty("content")
	public void setContent(String content) {
		this.content = content;
	}

	public String getPc_url() {
		return pc_url;
	}
	@JsonProperty("pc_url")
	public void setPc_url(String pc_url) {
		this.pc_url = pc_url;
	}

	public String getMobile_url() {
		return mobile_url;
	}
	@JsonProperty("mobile_url")
	public void setMobile_url(String mobile_url) {
		this.mobile_url = mobile_url;
	}

	public String getList_image() {
		return list_image;
	}
	@JsonProperty("list_image")
	public void setList_image(String list_image) {
		this.list_image = list_image;
	}

	public String getCtime() {
		return ctime;
	}
	@JsonProperty("ctime")
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getMtime() {
		return mtime;
	}
	@JsonProperty("mtime")
	public void setMtime(String mtime) {
		this.mtime = mtime;
	}

}
