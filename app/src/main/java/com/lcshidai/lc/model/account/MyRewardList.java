package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyRewardList extends BaseData {
	private String id;
	private String type; // 红包类型：1-收入，2-支出
	private String amount; // 金额
	private String serial_no; // 流水号
	private String ctime; // 时间
	private String status; // 1-有效，2-失效
	private String fetch_time; // 失效时间
	private String obj_id; // 查看规则使用
	private String obj_type; // 查看规则使用
	private String memo; // 红包流水记录的备注
	private MyRewardListExt ext;

	public MyRewardList() {

	}

	public MyRewardList(String me) {

	}

	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSerial_no() {
		return serial_no;
	}

	@JsonProperty("serial_no")
	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}

	public String getCtime() {
		return ctime;
	}

	@JsonProperty("ctime")
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getFetch_time() {
		return fetch_time;
	}

	@JsonProperty("fetch_time")
	public void setFetch_time(String fetch_time) {
		this.fetch_time = fetch_time;
	}

	public String getObj_id() {
		return obj_id;
	}

	@JsonProperty("obj_id")
	public void setObj_id(String obj_id) {
		this.obj_id = obj_id;
	}

	public String getObj_type() {
		return obj_type;
	}

	@JsonProperty("obj_type")
	public void setObj_type(String obj_type) {
		this.obj_type = obj_type;
	}

	public MyRewardListExt getExt() {
		return ext;
	}

	@JsonProperty("ext")
	public void setExt(MyRewardListExt ext) {
		this.ext = ext;
	}

	public String getMemo() {
		return memo;
	}

	@JsonProperty("memo")
	public void setMemo(String memo) {
		this.memo = memo;
	}

}
