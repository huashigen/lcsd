package com.lcshidai.lc.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown=true)
public class MessageType implements Serializable{
	
	private String type;
	private int sequence;
	private int count ;
	private List<String> messages;

	public String getType() {
		return type;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	public int getSequence() {
		return sequence;
	}
	@JsonProperty("sequence")
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getCount() {
		return count;
	}
	@JsonProperty("count")
	public void setCount(int count) {
		this.count = count;
	}
	public List<String> getMessages() {
		return messages;
	}
	@JsonProperty("messages")
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	
	
}
