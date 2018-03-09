package com.lcshidai.lc.utils;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.lcshidai.lc.db.UserInfoModel;


/**
 * 内存携带类
 *
 * @author 000814
 */
public class MemorySave {

    public int oldAccountBadgeNum = 0;// 之前账户小红点数量
    public boolean isNeedRefreshItemList = false;
    public boolean isRechargeSuccessToFinance = false;//充值成功跳至理财列表
    public boolean isBidSuccessBack = false;
    public boolean isCashSuccessBack = false;
    public boolean isLoginSendBroadcast = false;    //登陆是否发送过广播
    //	public boolean mIsShowAccountPreview=false;//判斷是否是完成註冊流程
    public boolean showCountRl = true;//账户概况抽奖次数显示
    //	public boolean isMarks;
    public boolean mIsLogin;//是否成功登陆过（用来判断cookie失效显示锁屏）
    //public boolean mIsLoginMark=true;//登錄失敗標記一次   只對我要轉讓模塊
    public boolean mIsLogined;//程序开启是否登录成功过
    public boolean mIsLoginout;//是否退出
    public boolean mIsFristrec = false;//程序开启是否登录成功过
    public boolean mIsRecpop = false;//程序开启是否登录成功过

    public boolean mIsLogining;  //正在登录

    public boolean mIsLoginTest = false;
    public boolean mIsFinanceInfoFinish = false;
    public boolean mIsGoHome = false;//进入理财列表
    public boolean mIsGoFinance = false;//进入理财列表
    public boolean mIsGoFinanceHome = false;//进入理财列表
    public boolean mIsGoAccount = false;    //进入财富中心
    public boolean mIsGoFinanceInfo = false;//进入理财详细列表
    public String mAlarmPrjId = "";//待开标提醒项目ID
    public boolean mAlarmLoginFlag = false;//待开标提醒项目ID

    public boolean mIsGestureLoginAlive = false;    //是否运行手势锁页面

    public int mGoFinancetype = -1;//该变量为理财页面过滤条件 选择理财类型  不要乱标记

    public boolean mBackToAll = false;//理财-所有系列
    //public boolean mIsGoHome=false;
    public boolean mIsFirstOpen = true;    //程序是否是第一次启动手势锁
    public boolean mGestureAudoLoginFlag = false;    //解手势锁为true    自动登陆成功后改变为false
    public boolean mWebOpenAppFlag = false;    //url打开app
    //public boolean mRegIsGoHome=false;

    public double lastGestureTime = 0;

    public int type = 0;

    public boolean mIsGoFinanceRecord = false;

    public Bundle args;

    public UserInfoModel userInfo;//登录后获取用户谢谢

    public boolean isSetUname = false;
    public boolean isSetUnamego = false;

    public boolean mRegIsGoHomeThi = false;//返回到首页然后弹出注册第三步
    public String clockid = "";
    public String clockPrjid = "";
    public boolean isClock = false;
    public boolean goToFinanceAll = false;//理财列表回复默认状态
    public boolean goToFinanceRecommend = false;//推荐也刷新fragment数据用
    public boolean goLockActFromHome = false;//在当前程序界面按下了home 键

    public boolean goLockActFromPower = false;//在当前程序界面手机进入锁屏状态,区别于按下home键锁屏触发

    public long locktime = 0;//按下home键时间

    private static MemorySave mMS;

    public static MemorySave MS = MemorySave.getIntance();

    public boolean refreshTransferDoData = false;//转让成功刷新我要转让数据

    public boolean mgoHomeLoginOut;//程序开启是否登录成功过
    public DisplayMetrics dm = new DisplayMetrics();

    public boolean mFinanceToLoginLock = false;
    public boolean mLoginToRegLock = false;
    public boolean mRegLockSuccessBack = false;
    public boolean mRegLockSuccessBackMoney = false;

    public Bundle lockArgs;

    public boolean isrunResume = false;//理财时代是否在运行

    public boolean isGestureEnd = false;//手势划完状态 及手动登录后
    public boolean isGoLast = false;// 是否返回上级
//	public boolean isGestureClose=true;

    /**
     * 清理保存的内存
     */
    public static void clearMomery() {
        mMS = null;
    }

    private static MemorySave getIntance() {
        if (mMS == null) mMS = new MemorySave();
        return mMS;
    }
}
