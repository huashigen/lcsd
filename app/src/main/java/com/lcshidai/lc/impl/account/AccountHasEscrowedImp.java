package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.AccountHasEscrowedJson;

/**
 * Created by Allin on 2016/7/9.
 */
public interface AccountHasEscrowedImp {

    void gainHasEscrowedSuccess(AccountHasEscrowedJson response);

    void gainHasEscrowedFail();

}
