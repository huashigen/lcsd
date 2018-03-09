package com.lcshidai.lc.model.more;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionTabDataItem    {
	public String q;
	public String a;
	 
	public QuestionTabDataItem() {

	}

	public QuestionTabDataItem(String me) {

	}

	public String getQ() {
		return q;
	}

	@JsonProperty("q")
	public void setQ(String q) {
		this.q = q;
	}

	public String getA() {
		return a;
	}

	@JsonProperty("a")
	public void setA(String a) {
		this.a = a;
	}

	

}
