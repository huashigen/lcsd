package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.UserEscrowInfoJson;

/**
 * Created by Allin on 2016/7/9.
 */
public interface AccountUserEscrowInfoImp {

    void gainUserEscrowInfoSuccess(UserEscrowInfoJson response);

    void gainUserEscrowInfoFail();

}
