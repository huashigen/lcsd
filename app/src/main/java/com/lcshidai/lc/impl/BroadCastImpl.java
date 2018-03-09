package com.lcshidai.lc.impl;

/**
 * 广播相关字符串定义
 */
public interface BroadCastImpl {

    String MY_ACTION = "com.trj.hp.action.MyAction";
    String MY_RESON = "com.trj.hp.action.MyReson";
    String EXIT_EXTRA = "TRJActionExitExtra";

    String SYSTEM_REASON = "reason";
    String SYSTEM_HOME_KEY = "homekey";// home key
    String SYSTEM_RECENT_APPS = "recentapps";// long home key

    String MY_RESON_LOGIN_STATUS = "HOME_HOOK_HOME_LOGIN";//广播发送字符串  来源:登录状态改变
    String MY_RESON_FINANCE_SUCCESS_URL = "action_finance_success";

    String ACTION_FIRST_ACTIVITY_DATA = "first_activity_data";

    String ACTION_FINANCE_SEARCH_NO_DATA = "ACTION_FINANCE_SEARCH_NO_DATA";

    String ACTION_MAIN_SWITCH_TAB = "ACTION_MAIN_SWITCH_TAB";

    String ACTION_LOGIN_SUCCEED_OR_FAILED = "ACTION_LOGIN_SUCCEED";

    String ACTION_OPEN_ECW_ACCOUNT = "ACTION_OPEN_ECW_ACCOUNT";

    String ACTION_CLEAR_CHECK_HB = "ACTION_CLEAR_CHECK_HB";
}
