package com.lcshidai.lc.utils.onekeyshare;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;

public interface ShareContentCustomizeCallback {

	void onShare(Platform platform, ShareParams paramsToShare);

}
