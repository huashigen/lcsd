package com.lcshidai.lc.model.more;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendDataItem extends BaseData {
	public String title;
	public String href;
	public String img_path;
	public RecommendImgData img;

	public RecommendDataItem() {

	}

	public RecommendDataItem(String me) {

	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	public RecommendImgData getImg() {
		return img;
	}

	@JsonProperty("img")
	public void setImg(RecommendImgData img) {
		this.img = img;
	}

	public String getHref() {
		return href;
	}

	@JsonProperty("href")
	public void setHref(String href) {
		this.href = href;
	}

}
