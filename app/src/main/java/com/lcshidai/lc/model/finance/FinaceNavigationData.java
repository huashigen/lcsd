package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinaceNavigationData extends BaseData {
	private String name;
	private String hrefType;
	private String href;
	private String pic;
	private String need_header;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHrefType() {
		return hrefType;
	}

	public void setHrefType(String hrefType) {
		this.hrefType = hrefType;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getNeed_header() {
		return need_header;
	}

	public void setNeed_header(String need_header) {
		this.need_header = need_header;
	}
}
