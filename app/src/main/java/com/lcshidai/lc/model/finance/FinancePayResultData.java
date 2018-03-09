package com.lcshidai.lc.model.finance;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancePayResultData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3723882416024124960L;

	private String prj_name;
	private String prj_type_display;
	private String money;
	private String date_incoming;
	private String date_first_repayment;
	private String free_money;
	private String order_id;
	private String can_shake;
	private String is_pre_sale;
	private String start_bid_date;
	private String is_has_appoint;
	private String if_open;
	private String is_view_auto_icon;
	private String open_url_title;
	private String open_url;
	private String prj_business_type_name;

	private String free_tixian_times;
	private String remaining_amount;
	private String invest_count;
	private String repay_way;
	private String last_repay_date;
	private String bonus_money;
	private String addon_desc;
	
	private String bonustitle;
	private String shareurl;
	private String status;
	private String sharecontent;
	private String sharelogoimg;
	private String sharelogoico;

	private List<FinancePayResultButton> button;
	private String pic;
	private String pic_link;

	public String getFree_tixian_times() {
		return free_tixian_times;
	}

	@JsonProperty("free_tixian_times")
	public void setFree_tixian_times(String free_tixian_times) {
		this.free_tixian_times = free_tixian_times;
	}

	public String getRemaining_amount() {
		return remaining_amount;
	}

	@JsonProperty("remaining_amount")
	public void setRemaining_amount(String remaining_amount) {
		this.remaining_amount = remaining_amount;
	}

	public String getInvest_count() {
		return invest_count;
	}

	@JsonProperty("invest_count")
	public void setInvest_count(String invest_count) {
		this.invest_count = invest_count;
	}

	public String getRepay_way() {
		return repay_way;
	}

	@JsonProperty("repay_way")
	public void setRepay_way(String repay_way) {
		this.repay_way = repay_way;
	}

	public String getLast_repay_date() {
		return last_repay_date;
	}

	@JsonProperty("last_repay_date")
	public void setLast_repay_date(String last_repay_date) {
		this.last_repay_date = last_repay_date;
	}

	public String getBonus_money() {
		return bonus_money;
	}

	@JsonProperty("bonus_money")
	public void setBonus_money(String bonus_money) {
		this.bonus_money = bonus_money;
	}

	public String getAddon_desc() {
		return addon_desc;
	}

	@JsonProperty("addon_desc")
	public void setAddon_desc(String addon_desc) {
		this.addon_desc = addon_desc;
	}

	public String getPrj_business_type_name() {
		return prj_business_type_name;
	}

	@JsonProperty("prj_business_type_name")
	public void setPrj_business_type_name(String prj_business_type_name) {
		this.prj_business_type_name = prj_business_type_name;
	}

	public String getPrj_name() {
		return prj_name;
	}

	@JsonProperty("prj_name")
	public void setPrj_name(String prj_name) {
		this.prj_name = prj_name;
	}

	public String getPrj_type_display() {
		return prj_type_display;
	}

	@JsonProperty("prj_type_display")
	public void setPrj_type_display(String prj_type_display) {
		this.prj_type_display = prj_type_display;
	}

	public String getMoney() {
		return money;
	}

	@JsonProperty("money")
	public void setMoney(String money) {
		this.money = money;
	}

	public String getDate_incoming() {
		return date_incoming;
	}

	@JsonProperty("date_incoming")
	public void setDate_incoming(String date_incoming) {
		this.date_incoming = date_incoming;
	}

	public String getDate_first_repayment() {
		return date_first_repayment;
	}

	@JsonProperty("date_first_repayment")
	public void setDate_first_repayment(String date_first_repayment) {
		this.date_first_repayment = date_first_repayment;
	}

	public String getFree_money() {
		return free_money;
	}

	@JsonProperty("free_money")
	public void setFree_money(String free_money) {
		this.free_money = free_money;
	}

	public String getOrder_id() {
		return order_id;
	}

	@JsonProperty("order_id")
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getCan_shake() {
		return can_shake;
	}

	@JsonProperty("can_shake")
	public void setCan_shake(String can_shake) {
		this.can_shake = can_shake;
	}

	public String getIs_pre_sale() {
		return is_pre_sale;
	}

	@JsonProperty("is_pre_sale")
	public void setIs_pre_sale(String is_pre_sale) {
		this.is_pre_sale = is_pre_sale;
	}

	public String getStart_bid_date() {
		return start_bid_date;
	}

	@JsonProperty("start_bid_date")
	public void setStart_bid_date(String start_bid_date) {
		this.start_bid_date = start_bid_date;
	}

	public String getIs_has_appoint() {
		return is_has_appoint;
	}

	@JsonProperty("is_has_appoint")
	public void setIs_has_appoint(String is_has_appoint) {
		this.is_has_appoint = is_has_appoint;
	}

	public String getIf_open() {
		return if_open;
	}

	@JsonProperty("if_open")
	public void setIf_open(String if_open) {
		this.if_open = if_open;
	}

	public String getIs_view_auto_icon() {
		return is_view_auto_icon;
	}

	@JsonProperty("is_view_auto_icon")
	public void setIs_view_auto_icon(String is_view_auto_icon) {
		this.is_view_auto_icon = is_view_auto_icon;
	}

	public String getOpen_url_title() {
		return open_url_title;
	}

	@JsonProperty("open_url_title")
	public void setOpen_url_title(String open_url_title) {
		this.open_url_title = open_url_title;
	}

	public String getOpen_url() {
		return open_url;
	}

	@JsonProperty("open_url")
	public void setOpen_url(String open_url) {
		this.open_url = open_url;
	}

	public String getBonustitle() {
		return bonustitle;
	}

	@JsonProperty("bonustitle")
	public void setBonustitle(String bonustitle) {
		this.bonustitle = bonustitle;
	}

	public String getShareurl() {
		return shareurl;
	}

	@JsonProperty("shareurl")
	public void setShareurl(String shareurl) {
		this.shareurl = shareurl;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getSharecontent() {
		return sharecontent;
	}

	@JsonProperty("sharecontent")
	public void setSharecontent(String sharecontent) {
		this.sharecontent = sharecontent;
	}

	public String getSharelogoimg() {
		return sharelogoimg;
	}

	@JsonProperty("sharelogoimg")
	public void setSharelogoimg(String sharelogoimg) {
		this.sharelogoimg = sharelogoimg;
	}

	public String getSharelogoico() {
		return sharelogoico;
	}

	@JsonProperty("sharelogoico")
	public void setSharelogoico(String sharelogoico) {
		this.sharelogoico = sharelogoico;
	}

	public List<FinancePayResultButton> getButton() {
		return button;
	}

	@JsonProperty("button")
	public void setButton(List<FinancePayResultButton> button) {
		this.button = button;
	}

	public String getPic() {
		return pic;
	}

	@JsonProperty("pic")
	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPic_link() {
		return pic_link;
	}

	@JsonProperty("pic_link")
	public void setPic_link(String pic_link) {
		this.pic_link = pic_link;
	}
}
