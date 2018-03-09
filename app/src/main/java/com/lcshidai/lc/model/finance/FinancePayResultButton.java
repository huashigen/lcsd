package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Allin on 2016/8/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancePayResultButton implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 6145934871387671708L;


    /**
     * name : 继续投资
     * hrefType : jxtz
     * pic : https://test1.tourongjia.com/public/images/mobile/invest_succes_1.png
     * href : #
     */

    private String name;
    private String hrefType;
    private String pic;
    private String href;
    private String need_header;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHrefType() {
        return hrefType;
    }

    public void setHrefType(String hrefType) {
        this.hrefType = hrefType;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getNeed_header() {
        return need_header;
    }

    public void setNeed_header(String need_header) {
        this.need_header = need_header;
    }
}
