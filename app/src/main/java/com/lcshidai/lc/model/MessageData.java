package com.lcshidai.lc.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageData implements Serializable {

	private String type;
	private String uid;
	private int count;
	private int sequence;
	private List<MessageType> messages;

	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	public String getUid() {
		return uid;
	}

	@JsonProperty("uid")
	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getCount() {
		return count;
	}
	
	@JsonProperty("count")
	public void setCount(int count) {
		this.count = count;
	}

	public int getSequence() {
		return sequence;
	}

	@JsonProperty("sequence")
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public List<MessageType> getMessages() {
		return messages;
	}

	@JsonProperty("messages")
	public void setMessages(List<MessageType> messages) {
		this.messages = messages;
	}

}
