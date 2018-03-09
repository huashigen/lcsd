package com.lcshidai.lc.model.licai;

import java.io.Serializable;

/**
 * Created by RandyZhang on 2016/11/24.
 */

public class ProvinceCityModel implements Serializable {

    private String province;
    private String[] area_list;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String[] getArea_list() {
        return area_list;
    }

    public void setArea_list(String[] area_list) {
        this.area_list = area_list;
    }
}
