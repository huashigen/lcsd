package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskTestQuestionData extends BaseJson {
    private List<RiskQAData> questionList;

    public List<RiskQAData> getQuestionList() {
        return questionList;
    }

    @JsonProperty("question_rows")
    public void setQuestionList(List<RiskQAData> questionList) {
        this.questionList = questionList;
    }
}
