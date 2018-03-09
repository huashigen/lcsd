package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckFundRegisterData implements Serializable {
    private String is_register;

    public String getIs_register() {
        return is_register;
    }

    @JsonProperty("is_register")
    public void setIs_register(String is_register) {
        this.is_register = is_register;
    }
}
