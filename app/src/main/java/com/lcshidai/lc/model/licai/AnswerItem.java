package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerItem implements Serializable {
    private String answer_id;
    private String title;

    public String getAnswer_id() {
        return answer_id;
    }

    @JsonProperty("answer_id")
    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }
}
