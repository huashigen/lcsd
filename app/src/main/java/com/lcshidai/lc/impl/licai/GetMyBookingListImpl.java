package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.licai.MyBookingListJson;

public interface GetMyBookingListImpl {

    void getMyBookingListSuccess(MyBookingListJson response);

    void getMyBookingListFailed();
}
