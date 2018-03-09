package com.lcshidai.lc.impl.onKeyInvest;

import com.lcshidai.lc.model.oneKeyInvest.IsOpenAutoInvestJson;

public interface IsOpenAutoInvestImpl {
    void getIsOpenAutoInvestSuccess(IsOpenAutoInvestJson response);

    void getIsOpenAutoInvestFailed();
}
