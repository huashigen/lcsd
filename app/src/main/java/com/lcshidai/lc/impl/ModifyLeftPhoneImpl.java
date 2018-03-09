package com.lcshidai.lc.impl;

import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 16/7/6.
 */
public interface ModifyLeftPhoneImpl {

    void modifyLeftPhoneSuccess(BaseJson response);

    void modifyLeftPhoneFailed(BaseJson errorResponse);

    void gainModifyLeftPhoneSmsCodeSuccess(BaseJson response);

    void gainModifyLeftPhoneSmsCodeFailed(BaseJson errorResponse);
}
