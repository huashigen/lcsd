package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IsShowCfyData implements Serializable {
    private int cfy_show;

    public int getCfy_show() {
        return cfy_show;
    }

    @JsonProperty("cfy_show")
    public void setCfy_show(int cfy_show) {
        this.cfy_show = cfy_show;
    }
}
