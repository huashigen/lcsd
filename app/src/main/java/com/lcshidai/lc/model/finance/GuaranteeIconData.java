package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GuaranteeIconData extends BaseData {
	private String name;
	private String pic;
	private String name_view_1;
	private String name_view_2;

	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	@JsonProperty("pic")
	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getName_view_1() {
		return name_view_1;
	}

	@JsonProperty("name_view_1")
	public void setName_view_1(String name_view_1) {
		this.name_view_1 = name_view_1;
	}

	public String getName_view_2() {
		return name_view_2;
	}

	@JsonProperty("name_view_2")
	public void setName_view_2(String name_view_2) {
		this.name_view_2 = name_view_2;
	}
	
}
