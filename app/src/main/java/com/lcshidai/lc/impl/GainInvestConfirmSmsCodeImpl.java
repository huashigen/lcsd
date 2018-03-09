package com.lcshidai.lc.impl;

import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 16/7/8.
 */
public interface GainInvestConfirmSmsCodeImpl {
    void gainInvestConfirmSmsCodeSuccess(BaseJson response);

    void gainInvestConfirmSmsCodeFailed(BaseJson errorResponse);
}
