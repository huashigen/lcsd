package com.lcshidai.lc.utils;

/**
 * Created by RandyZhang on 2017/4/18.
 */

public class Constants {

    public static final int REQUEST_CODE = 104;
    public static final String IS_LOGIN = "is_login";

    // project
    public static class Project {
        public static String PROJECT_ID = "prj_id";
        public static String PROJECT_TYPE= "prj_type";
        public static String PROJECT_NAME = "prj_name";
        public static String IS_COLLECTION = "is_collection";
        public static String LIMIT_MONEY = "limitMoney";
        public static String CAN_INVEST = "canInvest";
        public static String IS_AUTO_INVEST_OPEN = "is_auto_invest_open";// 是否开启自动投标
        public static String IS_CG = "is_cg_account";// 是否是存管用户
    }

    // 登录
    public static final String AGENT_LOGIN = "Login";
    // 注册
    public static final String AGENT_REGISTER = "Register";
    // 提现
    public static final String AGENT_WITHDRAW = "WithDraw";
    // 支付
    public static final String AGENT_PAYBUY = "PayBuy";
    public static final String FUND_DETAIL = "fund_detail";
    public static final String FUND_ID = "fund_id";
    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String FUND_LOGIN_INFO = "fund_login_info";

    public static class DataBuried {
        public static final String REG_NEXT_CLICK = "1001";// 输入手机号，点击下一步
        public static final String REG_BACK_CLICK = "1002";// 输入注册短息验证码页面，点击顶部返回
        public static final String REG_SMS_CODE_CLICK = "1003";// 输入注册短息验证码页面，点击获取验证码
        public static final String REG_SECURITY_VERIFY = "1004";// 输入注册短息验证码页面，滑块验证成功
        public static final String REG_START = "103";
        public static final String REG_SUCCESS_I_KNOW = "1007";// 注册成功，点击我知道了
        public static final String REG_START_IN = "1008-0-0";// 输入手机号，进入
        public static final String REG_START_OUT = "1008-0-1";// 输入手机号，离开
        public static final String REG_SECOND_IN = "1008-1-0";// 输入注册短信验证码，进入
        public static final String REG_SECOND_OUT = "1008-1-1";// 输入注册短信验证码，离开
        public static final String REG_SUCCESS_IN = "1008-2-0";// 注册成功，进入
        public static final String REG_SUCCESS_OUT = "1008-2-1";// 注册成功，离开
        public static final String REG_FINISH = "1009";// 注册成功
    }
}
