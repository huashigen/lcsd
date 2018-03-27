package com.lcshidai.lc.service;

import java.net.URL;

/**
 * 接口地址（全部）
 *
 * @author 000814
 */
public interface HttpServiceURL {
    /**
     * 首页
     */
    // 获取首页四个图标
    String HOME_NAVIGATION = "Mobile2/Index/indexNavigation";
    // 首页->热门活动->获取热门活动列表
    String GET_HOT_ACTIVITY_LIST = "Mobile2/HotActivity/getHotActivityList";
    // 检查版本更新
    String CHECK_UPDATE = "Mobile2/MobileVersion/checkUpdate";


    /**
     * 用户相关接口
     */
    // 获取账户信息
    String GET_ACCOUNT_INFO = "Mobile2/User/account";
    // 获取用户设置信息
    String GET_ACCOUNT_SETTING = "Mobile2/User/userInfo";
    // 修改用户名
    String UPDATE_USERNAME = "Mobile2/User/editUname";
    // 修改登录密码
    String UPDATE_LOGIN_PSW = "Mobile2/User/updatePwd";
    // 修改支付密码
    String UPDATE_PAY_PSW = "Mobile2/User/updatePayPwd";
    // 开启或关闭手势密码时验证接口
    String URL_CHECK_PWD = "Mobile2/User/check_pwd";
    // 是否设置安全问题
    String CHECK_IS_SETTING_URL = "Mobile2/User/is_set_sqa";
    // 设置安全问题
    String SET_SAFE_QUESTION_URL = "Mobile2/User/set_sqa";
    // 验证安全问题
    String VERIFY_SAFE_QUESTION_URL = "Mobile2/User/set_sqa_check";
    //
    String URL_SEND_DATA = "Mobile2/User/editMobile1";
    // 手机修改第一步发送动态码
    String URL_SEND_EDIT_CODE = "Mobile2/User/sendCheckOldMobileCode";
    //
    String URL_SEND_AUTH = "Mobile2/User/mobileAuth";
    //
    String URL_SEND_UPDATE2 = "Mobile2/User/editMobile2";
    // 手机认证发送动态码
    String URL_SEND_AUTH_CODE = "Mobile2/User/sendMobileAuthCode";
    // 手机修改第二步发送动态码
    String URL_SEND_EDIT_CODE2 = "Mobile2/User/sendEditMobileCode";
    //
    String URL_SEND_MAIL_DATA = "Mobile2/User/editEmail1";
    // 认证邮箱更新
    String URL_SEND_MAIL_UPDATE2 = "Mobile2/User/editemail2";
    // 邮箱认证
    String URL_SEND_MIAL_AUTH = "Mobile2/Auth/authMail";
    //
    String GET_QUESTION_LIST_URL = "Mobile2/User/get_sqa_list";
    //
    String URL_CHECK_PAY_PWD = "Mobile2/User/checkPayPass";
    //
    String URL_SAVE_PATH = "Mobile2/User/uploadAva";
    // 绑定个推cid接口
    String BIND_UID_CID = "Mobile2/User/tuiSongProc";
    //
    String CANCEL_WITHDRAWALS_URL = "Mobile2/PayAccount/cancelCashout";
    //
    String URL_GET_LIST = "Mobile2/PayAccount/getBankListByKey";
    //
    String BALANCE_PAYMENTS_URL = "Mobile2/PayAccount/getMyRecordList";
    //
    String URL_GET_CITY_LIST = "Mobile2/PayAccount/getSftCity";
    //
    String URL_GET_CITYS_LIST = "Mobile2/PayAccount/getSftCitysById";
    //
    String MY_BONUS_HAVE_URL = "Mobile2/MyTyjBonus/getMyBonusList";
    //
    String MY_BONUS_RECORD_URL = "Mobile2/MyTyjBonus/getMyTyjBonusRecord";
    //
    String MY_BONUS_RULE_URL = "Mobile2/MyBonus/getBonusRule";
    //
    String GET_BANK_LIST_URL = "Mobile2/PayAccount/getCashoutFundList";
    //
    String BESPEAK_INDEX_DATA = "Mobile2/Appoint/index";
    //
    String BESPEAK_RECORD_LIST = "Mobile2/Appoint/appoint_list";
    //
    String BESPEAK_CANCEL = "Mobile2/Appoint/cancel";
    //
    String CASH_COUNT_URL = "Mobile2/FastCash/FastCashListCount";
    //
    String FASTCASHLIST = "Mobile2/FastCash/FastCashList";
    // transfer
    String CASH_CHECK_URL = "Mobile2/FastCash/settingRate";
    //
    String CASH_ADD_URL = "Mobile2/FastCash/addCashInfo";
    // 申请变现
    String DO_CASH = "Mobile2/FastCash/doAddCash";
    //
    String urlCancelCash = "Mobile2/FastCash/complete";
    //
    String WITHDRAWALS_LIST_URL = "Mobile2/PayAccount/getUserCashoutRecord";
    //
    String WITHDRAWALS_INFO_URL = "Mobile2/PayAccount/getUserCashoutDetail";
    //
    String URL_GETTYJ_DATA = "Mobile2/MyTyjBonus/MyTyjBonus";
    //
    String BANNAER_URL = "Mobile2/MyTyjBonus/ReceiveBonus";
    //
    String URL_LICAI = "Mobile2/MyTyjBonus/getMyBonusList";
    //
    String URL_TOUZI = "Mobile2/MyTyjBonus/getMyTyjBonusRecord";
    //
    String BESPEAK_APPLY_URL = "Mobile2/Appoint/apply";
    //
    String BESEPAK_INFO_URL = "Mobile2/Appoint/view";
    //
    String BESPEAK_ALTER = "Mobile2/Appoint/doEditAppoint";
    //
    String BESEPAK_APPLY_INFO_URL = "Mobile2/Appoint/editAppoint";
    //
    String BESPEAK_TYPE_URL = "Mobile2/Appoint/canAppointType";
    //
    String BESPEAK_IS = "Mobile2/Appoint/is_trigger_tips";
    //
    String RECHARGE_INFO_URL = "Mobile2/PayAccount/getUserRechargeDetail";
    //
    String BALANCE_URL = "Mobile2/PayAccount/getApplyCashout";
    //
    String URL_ECW_CASHOUT_MONEY = "Mobile2/PayAccount/getEcwCashoutMoney";
    //
    String ECW_SMS_CODE_URL = "Mobile2/PayAccount/sendEcwSmsCode";  //充值提现发送短信验证码
    //
    String CASH_OUT_CONFIRM = "Mobile2/PayAccount/cashOutConfirmPay";
    //
    String URL_PAY_PWD_UPDATE = "Mobile2/PayPassword/setPassword";
    //
    String URL_GET_EDIT_CODE = "Mobile2/PayPassword/getMobileCode";// 发送动态码
    //
    String URL_CHECK_EDIT_CODE = "Mobile2/PayPassword/checkMobileCode";// 验证发送动态码
    //
    String AUTH_VERIFY_URL = "Mobile2/PayPassword/checkIdentity";
    //
    String BANKCARD_VERIFY_URL = "Mobile2/PayPassword/checkBank";
    //
    String SAFE_VERIFY_URL = "Mobile2/PayPassword/checkSQA";
    //
    String GET_QUESTION_URL = "Mobile2/PayPassword/getUserSqa";
    //
    String GET_COUNTS_BY_STATYS_URL = "Mobile2/FinancList/getCountsByStatus";
    //
    String FINANCLIST = "Mobile2/FinancList/ilist";
    //
    String AUTH_VERIFY_URL_PWD = "Mobile2/Password/checkIdentity";
    //
    String BANKCARD_VERIFY_URL_PWD = "Mobile2/Password/checkBank";
    //
    String SAFE_VERIFY_URL_PWD = "Mobile2/Password/checkSQA";
    //
    String GET_QUESTION_URL_PWD = "Mobile2/Password/getUserSqa";
    //
    String SMS_CODE = "Mobile2/Password/getMobileCode";
    // 忘记密码->找回密码时验证手机号
    String NEXT_TO_SECOND = "Mobile2/Password/checkMobile";
    // 重置密码页面（有多重重置方式，通过set_type来判断）
    String RESET_PWD_URL = "Mobile2/Password/setPassword";
    /**
     * 项目投资->自动投标
     */
    // 是否开通自动投资
    String IS_OPEN_ONE_KEY_INVEST = "Mobile2/Escrow/hasAutoBiding";
    // 开通自动投资
    String OPEN_ONE_KEY_INVEST = "Mobile2/AutoBid/openAutoBiding";
    // 关闭一键投资
    String CLOSE_ONE_KEY_INVEST = "Mobile2/AutoBid/closeAutoBiding";
    // 获取集合表项目列表
    String GET_COLLECTION_PROJECTS = "Mobile2/Invest/getCollectionList";
    // 自动投标发送短信验证
    String GET_ONE_KEY_INVEST_SMS_CODE = "Mobile2/AutoBid/sendOpenBidMobileCode";
    /**
     * 项目投资相关
     */
    // 获取项目筛选信息列表（无参数，无需登录）
    String SEARCH_CONDITION = "Mobile2/Index/searchCondition";
    // 投资->获取项目列表显示的海报
    String FINANCE_POSTER = "Mobile2/Act/haiBao";
    // 获取项目列表
    String GET_PROJECT_LIST = "Mobile2/Invest/plist";
    // 获取项目详情
    String GET_PROJECT_DETAIL = "Mobile2/Invest/pdetail";
    // 获取项目详情页面保证图标
    String GET_GUARANTEE_ICONS = "Mobile2/Index/guaranteeIcons";
    // 获取项目基本信息
    String GET_PROJECT_BASE_INFO = "Mobile2/Invest/prjBaseInfo";
    // 获取项目公示材料
    String MATERIAL_INFO = "Mobile2/Invest/getAuditImage";
    // 获取项目投资记录
    String FINACE_ITEM_INVEST_RECORD = "Mobile2/Invest/invest_history";
    // 获取项目回款计划（by project id)
    String FINACE_ITEM_REPAYMENT_PLAY = "Mobile2/Invest/prj_replay_plan";
    // 获取项目回款计划（by order id)
    String ORDER_REPAY_URL = "Mobile2/Invest/order_replay_plan";
    // 获取体验项目列表
    String GET_EXPERIENCE_PROJECT_LIST = "Mobile2/Vinvest/plist";
    // 普通标投资检查
    String NORMAL_PRJ_INVEST_CHECK = "Mobile2/Invest/pbuyCheck";
    // 普通标投资
    String NORMAL_PRJ_INVEST = "Mobile2/Invest/pbuyN";
    // 集合标一键投资检查
    String COLLECTION_PRJ_INVEST_CHECK = "Mobile2/Invest/cbuyCheck";
    // 集合标投资
    String COLLECTION_PRJ_INVEST = "Mobile2/Invest/cbuyN";
    // 获取最大可投资金额
    String GET_MAX_INVEST_MONEY = "Mobile2/Invest/getMaxInvestMoney";
    // 购买（投资）体验项目
    String BUY_EXPERIENCE_PROJECT = "Mobile2/Vinvest/buy";
    // 首页->获取推荐项目
    String TOGGLE_RECOMMEND = "Mobile2/Invest/recommend";
    // 首页->获取推荐项目（新）
    String TOGGLE_RECOMMEND_NEW = "Mobile2/Invest/recommendNew";
    //
    String WITHDRAWALS_MONEY_URL = "Mobile2/PayAccount/getCashoutFee";//实际到账金额获取
    //
    String WITHDRAWALS_URL = "Mobile2/PayAccount/applyCashout";
    //
    String PAY_PWD_CHECK_URL = "Mobile2/PayPassword/checkPayPassword";
    //
    String CALCULATOR_URL = "Mobile2/Invest/calculator";//?prj_id=%@&money=%@
    // 获取红包列表
    String GET_PRJ_BONUS_LIST = "Mobile2/Invest/ajaxGetMyPrjBonusList";
    // 获取满减券列表
    String GET_PRJ_TICKETS_LIST = "Mobile2/Invest/ajaxGetMyPrjTicketsList";
    // 获取加息券列表
    String GET_PRJ_JXQ_LIST = "Mobile2/Invest/ajaxGetMyAddInterestList";
    //
    String INVITE_URL = "Mobile2/Share/getInvite";
    //
    String URL_GET_DATA_SHARE = "Mobile2/Share/getShareBehavior";// getInvite
    //
    String SHAKE_URL = "Mobile2/Shake/getSharkBonusNum";
    //
    String GET_MESSAGE_URL = "Mobile2/Push/getPushMessage";
    // 是否登录（正常的Api请求）
    String IS_LOGIN = "Mobile2/Auth/isLogin";
    // 是否登录（未登录会返回logined字段，BaseJsonHandler根据这个来进行登录跳转）
    String TEST_LOGIN_URL = "Mobile2/Auth/isLogin2";
    /**
     * ShareBonus
     */
    // 登录或注册 第一步
    String IS_MOBILE_REGISTER = "Mobile2/ShareBonus/loginOrRegister";
    // 用户登录
    String LOGIN = "Mobile2/ShareBonus/login";
    // 用户注册
    String REGISTER_URL = "Mobile2/ShareBonus/register";
    // 设置登录密码进行注册动作
    String SETPWDNEW = "Mobile2/ShareBonus/setPasswordNew";
    // 获取注册验证码（活动用户）
    String GET_ACT_USER_SMS_CODE = "Mobile2/ShareBonus/getActiveMobile";
    // 退出登录
    String LOGOUT = "Mobile2/Auth/logout";
    //
    String IS_MOBILE_REG = "Mobile2/Auth/isMobileRegister";
    // 获取注册奖励信息（如{"data":{"data":"0","mess":"注册即送500元满减券","num":"500"}}）
    String GET_REG_REWARD_INFO = "Mobile2/Auth/isNeedCode";
    // 登录（传参与ShareBonus不同，且多返回一个字段is_allow_escrow）
    String LOGIN_URL = "Mobile2/Auth/login";
    // 获取注册验证码（旧）
    String SEND_CODE = "Mobile2/Auth/getMobileCodeRegister";
    // 获取注册验证码（防止攻击的版本）
    String SEND_CODE_PRENT_ATTACK = "Mobile2/Auth/getMobileCodeRegisterPreventAttack";
    //
    String URL_AUTH_STATUS = "Mobile2/Auth/authStatus";
    //
    String URL_SAVE_IMG = "Public/Upload/uploadImg";
    //
    String URL_STATS_A1 = "Mobile2/Stats/a1";
    //
    String URL_STATS_A2 = "Mobile2/Stats/a2";
    // YouMi
    String YOUMI_URL = "Mobile2/AppStats/iGetConfig";

    /**
     * Public（公共接口）
     */
    // 获取图形验证码
    String VERIFY = "Mobile2/Public/verify";
    // loading
    String LOADING_URL = "Mobile2/Public/monthActivityBanner";
    // 发送崩溃日志
    String EXCEPTION_LOG = "Mobile2/Public/exceptionLog";
    // 获取banner图片
    String GET_BANNER_LIST = "Mobile2/Public/banner";
    // 数据埋点
    String DATA_BURIED_POINT = "Mobile2/Public/webLog";

    // 获取小红点消息
    String GET_ALL_NEW_MSG_LIST = "Mobile2/AppStats/iGetRMessage";
    /**
     * 存管
     */
    // 获取存管用户信息
    String GET_ESCROW_USER_INFO = "Mobile2/Escrow/userEscrowInfo";
    // 是否开通存管
    String HAS_ESCROWED = "Mobile2/Escrow/hasEscrowed";
    // 是否浙商卡片
    String IS_ZHESHANG_CARD = "Mobile2/Escrow/isZheshangCard";
    // 获取是否已经提醒
    String GET_ESCROW_REMIND = "Mobile2/Escrow/getEscrowRemind";
    // 设置已经提醒
    String SET_ESCROW_REMIND = "Mobile2/Escrow/setEscrowRemind";
    // 修改银行预留手机号时获取验证码
    String GET_ESCROW_SMS_CODE = "Mobile2/Escrow/sendSMS";
    // 修改预留手机号
    String UPDATE_ESCROW_LEFT_PHONE = "Mobile2/Escrow/modifyMobile";
    // 获取开户所需要的信息
    String GET_OPEN_ACCOUNT_INFO = "Mobile2/Escrow/applyBankAccount";
    // 投资发送短信验证码
    String GAIN_INVEST_CONFIRM_SMS_CODE = "Mobile2/Invest/ecwBuySendSms";
    // 首页弹窗信息接口
    String GET_HOME_POP_MESSAGE = "Mobile2/Mobile/getIndexPopups";
    //理财投资者提示框显示状态
    String LCS_USER_ALERT = "Lcs/User/getAppAlert";
    //理财投资者认证
    String LCS_USER_CHECKIN = "Lcs/User/checkInApp";
    // 积分记录
    String SCORE_RECORD_LIST = "Mobile2/Sign/recordlist";
    // 签到
    String SIGN = "Mobile2/Sign/index";
    // 签到积分信息
    String SIGN_SCORE_INFO = "Mobile2/Sign/userinfo";
    // 广告
    String ADS_BY_UID = "Mobile2/Ads/getAdsByUid";
    // 是否显示长富云理财
    String IS_CFY_SHOW = "Mobile2/Invest/getCfyShow";
    // 获取理财师用户中心信息
    String GET_LICAI_UC_INFO = "Mobile2/Passport/getUcById";
    // 长富云请求头
    //"http://lcstest.rongshifu.com";//"http://ywapi.cfylicai.com";
    String CFY_HEAD = "http://api.cfylicai.com";
    // 获取风险测试列表
    String GET_RISK_TEST_QUESTION_LIST = CFY_HEAD + "/index.php/ApiInvestor/User/GetQuestionList";
    // 保存风险测试答案
    String SAVE_RISK_TEST_ANSWER = CFY_HEAD + "/index.php/ApiInvestor/User/SaveAnswer";
    // 获取Access Token
    String GET_ACCESS_TOKEN = CFY_HEAD + "/index.php/ApiInvestor/Public/GetAccessToken";
    // 理财登录
    String FUND_LOGIN = CFY_HEAD + "/index.php/ApiInvestor/User/Login";
    // 获取基金类型列表
    String GET_FUND_TYPE_LIST = CFY_HEAD + "/index.php/ApiInvestor/Public/IndexInit";
    // 我的预约列表
    String MY_BOOKING_LIST = CFY_HEAD + "/index.php/ApiInvestor/TradingCenter/BookingOrder";
    // 产品预约
    String ADD_TO_BOOKING_ORDER = CFY_HEAD + "/index.php/ApiInvestor/FundCenter/AddToBookingOrder";
    // 基金列表
    String GET_FUND_LIST = CFY_HEAD + "/index.php/ApiInvestor/FundCenter/FundList";
    // 基金详情
    String GET_FUND_DETAIL_INFO = CFY_HEAD + "/index.php/ApiInvestor/FundCenter/FundDetail";
    // 修改认证条件
    String MODIFY_AUDIT = CFY_HEAD + "/index.php/ApiInvestor/User/ModifyAudit";
    // 获取用户交易资料
    String GET_FUND_ACCOUNT_INFO = CFY_HEAD + "/index.php/ApiInvestor/TradingCenter/GetAccountInfo";
    // 修改区域
    String MODIFY_AREA = CFY_HEAD + "/index.php/ApiInvestor/User/ModifyArea";
    // 检查基金账户是否注册
    String CHECK_FUND_REGISTER = CFY_HEAD + "/index.php/ApiInvestor/User/checkRegister";
    // 获取项目借款人记录
    String FINACE_ITEM_BORROWER_RECORD = "Mobile2/Invest/get_borrower_list";

    //判断用户是否以及进行风险评估
    String IS_INVEST_RISK_ENVALUATION = "Mobile2/Auth/riskAssessStatus";

    //风险评估链接
    String RISK_ENVALUATION_URL = "http://16p5614y04.iask.in:41648/#/rankEvaluation";
}
