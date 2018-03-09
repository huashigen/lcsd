package com.lcshidai.lc.impl;

import com.lcshidai.lc.model.GetHomePopJson;

public interface GetHomePopImpl {
    void getHomePopSuccess(GetHomePopJson response);

    void getHomePopFail();
}
