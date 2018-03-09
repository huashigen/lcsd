package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.BaseJson;

/**
 * Created by Allin on 2016/7/9.
 */
public interface AccountEcwSmsImp {

    void gainEcwSmsCodeSuccess(BaseJson response);

    void gainEcwSmsCodeFail();

}
