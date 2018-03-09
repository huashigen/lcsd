package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.SignJson;

/**
 * Created by RandyZhang on 16/7/13.
 */
public interface SignImpl {

    void signSuccess(SignJson response);

    void signFailed();
}
