package com.lcshidai.lc.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class MessageLocalData implements Serializable {

	private String type;
	private String uid;
	private int count;
	private int sequence;
	private Map<String , MessageTypeNew> map ;
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getCount() {
		int count = this.count;
		Set<String> set = this.getMap().keySet();
		for (String k : set) {
			MessageTypeNew type = this.getMap().get(k);
			List<MsgNew> msgNews = type.getMessages();
			for (MsgNew msgNew : msgNews) {
				if(msgNew.isDirty()){
					--count;
				}
			}
		}
		
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Map<String, MessageTypeNew> getMap() {
		return map;
	}

	public void setMap(Map<String, MessageTypeNew> map) {
		this.map = map;
	}


}
