package com.lcshidai.lc.model.finance.reward;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class JiaXiTicketModel {
	
	 private List<RateTicket> rateTicketList;
	 
	 public List<RateTicket> getRateTicketList() {
			return rateTicketList;
		}

	@JsonProperty("list")
	public void setRateTicketList(List<RateTicket> rateTicketList) {
		this.rateTicketList = rateTicketList;
	}
}
