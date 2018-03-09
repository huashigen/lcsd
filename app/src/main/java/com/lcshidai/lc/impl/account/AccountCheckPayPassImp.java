package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.BaseJson;

/**
 * Created by Allin on 2016/7/9.
 */
public interface AccountCheckPayPassImp {

    void checkPayPassSuccess(BaseJson response);

    void checkPayPassFail();

}
