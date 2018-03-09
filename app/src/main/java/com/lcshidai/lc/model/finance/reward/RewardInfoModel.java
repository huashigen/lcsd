package com.lcshidai.lc.model.finance.reward;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RewardInfoModel {
	
	private String reward_type;//选中的奖励类型
    private String reward_id;//如果选中的加息券 加息券id
    private int reward_num;
    private String bouns_rate;
    private String bouns_prj_term;
    
    private HongBaoModel hongbao;
    
    private ManjianTicketModel manjianTicket;

    private JiaXiTicketModel jiaXiTicketModel;
    
    private NoUseModel noUseModel;
    
	public String getReward_type() {
		return reward_type;
	}

	@JsonProperty("reward_type")
	public void setReward_type(String reward_type) {
		this.reward_type = reward_type;
	}

	public String getReward_id() {
		return reward_id;
	}

	@JsonProperty("reward_id")
	public void setReward_id(String reward_id) {
		this.reward_id = reward_id;
	}

	public HongBaoModel getHongbao() {
		return hongbao;
	}

	@JsonProperty("hongbao")
	public void setHongbao(HongBaoModel hongbao) {
		this.hongbao = hongbao;
	}

	public ManjianTicketModel getManjianTicket() {
		return manjianTicket;
	}

	@JsonProperty("manjian_ticket")
	public void setManjianTicket(ManjianTicketModel manjian_ticket) {
		this.manjianTicket = manjian_ticket;
	}

	public JiaXiTicketModel getJiaXiTicketModel() {
		return jiaXiTicketModel;
	}

	@JsonProperty("jiaxi")
	public void setJiaXiTicketModel(JiaXiTicketModel jiaXiTicketModel) {
		this.jiaXiTicketModel = jiaXiTicketModel;
	}

	public NoUseModel getNoUseModel() {
		return noUseModel;
	}

	@JsonProperty("no_use")
	public void setNoUseModel(NoUseModel noUseModel) {
		this.noUseModel = noUseModel;
	}

	public int getReward_num() {
		return reward_num;
	}

	public void setReward_num(int reward_num) {
		this.reward_num = reward_num;
	}

	public String getBouns_rate() {
		return bouns_rate;
	}

	public void setBouns_rate(String bouns_rate) {
		this.bouns_rate = bouns_rate;
	}

	public String getBouns_prj_term() {
		return bouns_prj_term;
	}

	public void setBouns_prj_term(String bouns_prj_term) {
		this.bouns_prj_term = bouns_prj_term;
	}
	
}
