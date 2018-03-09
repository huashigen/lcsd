package com.lcshidai.lc.http;

import com.socks.library.KLog;
import com.lcshidai.lc.AtrApplication;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.utils.ToastUtil;

/**
 * Created by RandyZhang on 2017/5/10.
 */

public abstract class BaseCallback<T> {
    protected void onStart() {

    }

    protected abstract void onRightData(T response);

    protected void onWrongData(T response) {
        try {
            BaseJson result = (BaseJson) response;
            String strm = "暂无数据,请登入";
            String message = result.getMessage();
            String boolen = result.getBoolen();
            if (!boolen.equals("1") && message != null && !message.trim().equals("")) {
                if (!message.contains(strm)) {
                    ToastUtil.showToast(AtrApplication.getInstance(), message);
                }
            }
            KLog.e(getClass().getSimpleName(), result.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onError(String message) {

    }
}
