package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyBookingListJson extends BaseJson {

    private BookingListData data;

    public BookingListData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(BookingListData data) {
        this.data = data;
    }
}
