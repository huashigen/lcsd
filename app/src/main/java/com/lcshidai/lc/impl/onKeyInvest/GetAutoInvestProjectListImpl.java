package com.lcshidai.lc.impl.onKeyInvest;

import com.lcshidai.lc.model.oneKeyInvest.GetAutoInvestProjectListJson;

public interface GetAutoInvestProjectListImpl {
    void getAutoInvestProListSuccess(GetAutoInvestProjectListJson response);

    void getAutoInvestProListFailed();
}
