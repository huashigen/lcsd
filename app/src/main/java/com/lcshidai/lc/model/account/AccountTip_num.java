package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountTip_num extends BaseData implements Serializable{
	private String tip_chongzhi; // 充值角标
	private String tip_tixian; // 提现角标
	private String tip_yuyue; // 预约角标
	private String tip_bianxian; // 变现角标
	private String tip_touzi; // 投资记录角标
	private String tip_zijinjilu; // 资金记录角标
	private String tip_qifuxin; // yq付角标
	private String tip_xiaoxi; // 消息中心角标

	public AccountTip_num() {

	}

	public AccountTip_num(String me) {

	}
	
	public String getTip_chongzhi() {
		return tip_chongzhi;
	}

	@JsonProperty("tip_chongzhi")
	public void setTip_chongzhi(String tip_chongzhi) {
		this.tip_chongzhi = tip_chongzhi;
	}

	public String getTip_tixian() {
		return tip_tixian;
	}

	@JsonProperty("tip_tixian")
	public void setTip_tixian(String tip_tixian) {
		this.tip_tixian = tip_tixian;
	}

	public String getTip_yuyue() {
		return tip_yuyue;
	}

	@JsonProperty("tip_yuyue")
	public void setTip_yuyue(String tip_yuyue) {
		this.tip_yuyue = tip_yuyue;
	}

	public String getTip_bianxian() {
		return tip_bianxian;
	}

	@JsonProperty("tip_bianxian")
	public void setTip_bianxian(String tip_bianxian) {
		this.tip_bianxian = tip_bianxian;
	}

	public String getTip_touzi() {
		return tip_touzi;
	}

	@JsonProperty("tip_touzi")
	public void setTip_touzi(String tip_touzi) {
		this.tip_touzi = tip_touzi;
	}

	public String getTip_zijinjilu() {
		return tip_zijinjilu;
	}

	@JsonProperty("tip_zijinjilu")
	public void setTip_zijinjilu(String tip_zijinjilu) {
		this.tip_zijinjilu = tip_zijinjilu;
	}

	public String getTip_qifuxin() {
		return tip_qifuxin;
	}

	@JsonProperty("tip_qifuxin")
	public void setTip_qifuxin(String tip_qifuxin) {
		this.tip_qifuxin = tip_qifuxin;
	}

	public String getTip_xiaoxi() {
		return tip_xiaoxi;
	}

	@JsonProperty("xiaoxi")
	public void setTip_xiaoxi(String tip_xiaoxi) {
		this.tip_xiaoxi = tip_xiaoxi;
	}

}
