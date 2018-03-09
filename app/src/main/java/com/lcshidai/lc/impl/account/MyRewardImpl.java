package com.lcshidai.lc.impl.account;

import com.lcshidai.lc.model.account.MyRewardRecordJson;

public interface MyRewardImpl {

	void gainMyRewardsuccess(MyRewardRecordJson response);

	void gainMyRewardfail();
	
//	void gainMyRewardRecordsuccess(MyRewardRecordJson response);
//
//	void gainMyRewardRecordfail();

}
