package com.lcshidai.lc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHomePopData extends BaseData {
    private int is_open;
    private String img;
    private String url;
    private String button_img;
    private String button_text;
    private String set_open_url;

    public int getIs_open() {
        return is_open;
    }

    public void setIs_open(int is_open) {
        this.is_open = is_open;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getButton_img() {
        return button_img;
    }

    public void setButton_img(String button_img) {
        this.button_img = button_img;
    }

    public String getButton_text() {
        return button_text;
    }

    public void setButton_text(String button_text) {
        this.button_text = button_text;
    }

    public String getSet_open_url() {
        return set_open_url;
    }

    public void setSet_open_url(String set_open_url) {
        this.set_open_url = set_open_url;
    }
}
