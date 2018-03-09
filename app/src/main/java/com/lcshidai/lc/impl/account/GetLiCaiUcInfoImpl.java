package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.LiCaiUcInfoJson;

/**
 * Created by RandyZhang on 2016/10/11.
 */
public interface GetLiCaiUcInfoImpl {
    void getLiCaiUcInfoSuccess(LiCaiUcInfoJson response);

    void getLiCaiUcInfoFailed();
}
