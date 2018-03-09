package com.lcshidai.lc.impl.licai;

import com.lcshidai.lc.model.licai.FundTypeListJson;

/**
 * Created by RandyZhang on 2016/10/17.
 */
public interface GetFundTypeListImpl {

    void getFundTypeListSuccess(FundTypeListJson response);

    void getFundTypeListFailed(FundTypeListJson errorResponse);

}
