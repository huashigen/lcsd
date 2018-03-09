package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.ScoreRecordJson;

public interface ScoreRecordImpl {

    void onGetScoreListSuccess(ScoreRecordJson response);

    void onGetScoreListFailed();
}
