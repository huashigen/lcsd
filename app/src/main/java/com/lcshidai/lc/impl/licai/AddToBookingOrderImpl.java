package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 16/10/8.
 */
public interface AddToBookingOrderImpl {

    void addToBookingOrderSuccess(BaseJson response);

    void addToBookingOrderFailed(BaseJson errorResponse);
}
