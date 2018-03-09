package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.BindUidCidJson;

/**
 * Created by RandyZhang on 16/7/13.
 */
public interface BindUidCidImpl {

    void bindUidCidSuccess(BindUidCidJson response);

    void bindUidCidFailed();
}
