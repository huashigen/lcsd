package com.lcshidai.lc.model.pub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateManagerData extends BaseData {
	private String update;//
	private String product_trash;//
	private String crc;//
	private String download;//
	private String version;//
	private String size;//
	private String android_remark;

	public String getUpdate() {
		return update;
	}

	@JsonProperty("update")
	public void setUpdate(String update) {
		this.update = update;
	}

	public String getProduct_trash() {
		return product_trash;
	}

	@JsonProperty("product_trash")
	public void setProduct_trash(String product_trash) {
		this.product_trash = product_trash;
	}

	public String getCrc() {
		return crc;
	}

	@JsonProperty("crc")
	public void setCrc(String crc) {
		this.crc = crc;
	}

	public String getDownload() {
		return download;
	}

	@JsonProperty("download")
	public void setDownload(String download) {
		this.download = download;
	}

	public String getVersion() {
		return version;
	}

	@JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
	}

	public String getSize() {
		return size;
	}

	@JsonProperty("size")
	public void setSize(String size) {
		this.size = size;
	}

	public String getAndroid_remark() {
		return android_remark;
	}

	@JsonProperty("android_remark")
	public void setAndroid_remark(String android_remark) {
		this.android_remark = android_remark;
	}

}
