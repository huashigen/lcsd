package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RandyZhang on 2016/10/9.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingListData implements Serializable {
    private List<BookingListItemBean> bookingList;
    private String page_count;

    public List<BookingListItemBean> getBookingList() {
        return bookingList;
    }

    @JsonProperty("rows")
    public void setBookingList(List<BookingListItemBean> bookingList) {
        this.bookingList = bookingList;
    }

    public String   getPage_count() {
        return page_count;
    }

    @JsonProperty("page_count")
    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }
}
