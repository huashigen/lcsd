package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceProjectInvestRecordItem {
	public String money;
	public String uname_org;
	public String ctime;
	public String uname;
	public String is_appoint;
	public String is_pre_sale;

	public String getMoney() {
		return money;
	}

	@JsonProperty("money")
	public void setMoney(String money) {
		this.money = money;
	}

	public String getUname_org() {
		return uname_org;
	}

	@JsonProperty("uname_org")
	public void setUname_org(String uname_org) {
		this.uname_org = uname_org;
	}

	public String getCtime() {
		return ctime;
	}

	@JsonProperty("ctime")
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getUname() {
		return uname;
	}

	@JsonProperty("uname")
	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getIs_appoint() {
		return is_appoint;
	}

	@JsonProperty("is_appoint")
	public void setIs_appoint(String is_appoint) {
		this.is_appoint = is_appoint;
	}

	public String getIs_pre_sale() {
		return is_pre_sale;
	}
	@JsonProperty("is_pre_sale")
	public void setIs_pre_sale(String is_pre_sale) {
		this.is_pre_sale = is_pre_sale;
	}

}
