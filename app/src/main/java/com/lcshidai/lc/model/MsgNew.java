package com.lcshidai.lc.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MsgNew implements Serializable {
	private String msg;
	private boolean isDirty;
	
	public MsgNew() {
		super();
	}

	public MsgNew(String msg, boolean isDirty) {
		super();
		this.msg = msg;
		this.isDirty = isDirty;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

}
