package com.lcshidai.lc.model.licai;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/9/28.
 */

public class AttachBean implements Serializable {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
