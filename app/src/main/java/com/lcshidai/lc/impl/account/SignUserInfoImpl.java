package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.SignUserInfoJson;

/**
 * Created by RandyZhang on 16/7/13.
 */
public interface SignUserInfoImpl {

    void getSignUserInfoSuccess(SignUserInfoJson response);

    void getSignUserInfoFailed();
}
