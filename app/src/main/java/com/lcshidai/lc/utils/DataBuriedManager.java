package com.lcshidai.lc.utils;

import android.content.Context;

import com.socks.library.KLog;
import com.lcshidai.lc.service.ApiService;
import com.lcshidai.lc.http.BaseCallback;
import com.lcshidai.lc.http.ProJsonHandler;
import com.lcshidai.lc.model.BaseJson;

/**
 * 数据埋点管理类
 * Created by RandyZhang on 2017/6/28.
 */

public class DataBuriedManager {
    public static void onEventTag(Context context, String tag, String eventType) {
        ApiService.dataBuriedPoint(new ProJsonHandler<>(new BaseCallback<BaseJson>() {
            @Override
            protected void onRightData(BaseJson response) {
                KLog.i("DataBuried", "埋点成功");
            }
        }, context), context, tag, eventType, "");
    }
}
