package com.lcshidai.lc.model.more;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InviteData extends BaseData {
	private String content;
	private String ur;
	private String title;
	private String desc;
	private String qrcode;
	private String[] ext_data;
	private String recommend_code;
	private String logo;
	private String[] ext_url;
	// added by randy at 2017/04/24
	private String recommend_money;// 推荐收益
	private String recommend_zd;// 推荐在贷
	
	public String getRecommend_code() {
		return recommend_code;
	}
	@JsonProperty("recommend_code")
	public void setRecommend_code(String recommend_code) {
		this.recommend_code = recommend_code;
	}

	public InviteData() {

	}

	public InviteData(String me) {

	}

	public String getLogo() {
		return logo;
	}
	@JsonProperty("logo")
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getContent() {
		return content;
	}

	@JsonProperty("content")
	public void setContent(String content) {
		this.content = content;
	}

	public String getUr() {
		return ur;
	}

	@JsonProperty("ur")
	public void setUr(String ur) {
		this.ur = ur;
	}

	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	@JsonProperty("desc")
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getQrcode() {
		return qrcode;
	}

	@JsonProperty("qrcode")
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String[] getExt_data() {
		return ext_data;
	}

	@JsonProperty("ext_data")
	public void setExt_data(String[] ext_data) {
		this.ext_data = ext_data;
	}
	public String[] getExt_url() {
		return ext_url;
	}
	public void setExt_url(String[] ext_url) {
		this.ext_url = ext_url;
	}

	public String getRecommend_money() {
		return recommend_money;
	}

	public void setRecommend_money(String recommend_money) {
		this.recommend_money = recommend_money;
	}

	public String getRecommend_zd() {
		return recommend_zd;
	}

	public void setRecommend_zd(String recommend_zd) {
		this.recommend_zd = recommend_zd;
	}
}
