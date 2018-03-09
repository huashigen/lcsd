package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.licai.FundDetailInfoJson;

/**
 * Created by RandyZhang on 16/10/8.
 */
public interface GetFundDetailInfoImpl {

    void getFundDetailInfoSuccess(FundDetailInfoJson response);

    void getFundDetailInfoFailed();
}
