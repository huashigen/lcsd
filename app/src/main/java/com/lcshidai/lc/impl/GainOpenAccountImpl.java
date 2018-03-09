package com.lcshidai.lc.impl;

import com.lcshidai.lc.model.OpenAccountJson;

/**
 * Created by RandyZhang on 16/7/13.
 */
public interface GainOpenAccountImpl {

    void getOpenAccountInfoSuccess(OpenAccountJson response);

    void getOpenAccountInfoFailed();
}
