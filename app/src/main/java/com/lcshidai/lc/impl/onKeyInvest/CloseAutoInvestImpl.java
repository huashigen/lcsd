package com.lcshidai.lc.impl.onKeyInvest;

import com.lcshidai.lc.model.BaseJson;

public interface CloseAutoInvestImpl {
    void closeAutoInvestSuccess(BaseJson response);

    void closeAutoInvestFailed();
}
