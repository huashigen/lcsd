package com.lcshidai.lc.model.licai;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/9/28.
 */

public class FundManagerBean implements Serializable {
    private String xname;
    private String work_year;
    private String graduate_college;
    private String describes;

    public String getXname() {
        return xname;
    }

    public void setXname(String xname) {
        this.xname = xname;
    }

    public String getWork_year() {
        return work_year;
    }

    public void setWork_year(String work_year) {
        this.work_year = work_year;
    }

    public String getGraduate_college() {
        return graduate_college;
    }

    public void setGraduate_college(String graduate_college) {
        this.graduate_college = graduate_college;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }
}
