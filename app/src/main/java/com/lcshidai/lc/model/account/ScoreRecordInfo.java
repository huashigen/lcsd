package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoreRecordInfo extends BaseData implements Serializable {
    String id;
    String score;
    String time;
    String describe;
    String inOrOut;//收入还是指出积分1 收入+,0 支出－

    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    @JsonProperty("change_score")
    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    @JsonProperty("ctime")
    public void setTime(String time) {
        this.time = time;
    }

    public String getDescribe() {
        return describe;
    }

    @JsonProperty("adec")
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getInOrOut() {
        return inOrOut;
    }

    @JsonProperty("in_or_out")
    public void setInOrOut(String inOrOut) {
        this.inOrOut = inOrOut;
    }
}

