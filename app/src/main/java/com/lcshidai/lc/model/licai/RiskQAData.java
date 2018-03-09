package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskQAData implements Serializable {
    private String question_id;
    private String title;

    private ArrayList<AnswerItem> answerItemArrayList;

    public String getQuestion_id() {
        return question_id;
    }

    @JsonProperty("question_id")
    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<AnswerItem> getAnswerItemArrayList() {
        return answerItemArrayList;
    }

    @JsonProperty("item")
    public void setAnswerItemArrayList(ArrayList<AnswerItem> answerItemArrayList) {
        this.answerItemArrayList = answerItemArrayList;
    }
}
