package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakTypeData extends BaseData {
	private BespeakType A;
	private BespeakType H;
	private BespeakType F;
	private BespeakType B;

	public BespeakType getA() {
		return A;
	}

	@JsonProperty("A")
	public void setA(BespeakType a) {
		A = a;
	}

	public BespeakType getH() {
		return H;
	}

	@JsonProperty("H")
	public void setH(BespeakType h) {
		H = h;
	}

	public BespeakType getF() {
		return F;
	}

	@JsonProperty("F")
	public void setF(BespeakType f) {
		F = f;
	}

	public BespeakType getB() {
		return B;
	}

	@JsonProperty("B")
	public void setB(BespeakType b) {
		B = b;
	}

}
