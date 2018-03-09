package com.lcshidai.lc.model.account;

import com.lcshidai.lc.model.BaseJson;

/**
 * Created by Allin on 2016/7/11.
 */
public class UserEscrowInfoJson extends BaseJson {
    private UserEscrowInfoData data;

    public UserEscrowInfoData getData() {
        return data;
    }

    public void setData(UserEscrowInfoData data) {
        this.data = data;
    }

}
