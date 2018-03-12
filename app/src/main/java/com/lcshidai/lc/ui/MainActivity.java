package com.lcshidai.lc.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.lcshidai.lc.R;
import com.lcshidai.lc.getui.GtIntentService;
import com.lcshidai.lc.getui.GtPushService;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.model.MessageLocalData;
import com.lcshidai.lc.model.MessageTypeNew;
import com.lcshidai.lc.model.MsgNew;
import com.lcshidai.lc.ui.account.AccountActivity;
import com.lcshidai.lc.ui.base.AbsActivityGroup;
import com.lcshidai.lc.ui.finance.ManageFinanceActivity;
import com.lcshidai.lc.ui.project.ProjectActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.MsgUtil;
import com.lcshidai.lc.utils.SpUtils;
import com.lcshidai.lc.utils.StringUtils;

import java.util.List;

/**
 * 主页面
 */
public class MainActivity extends AbsActivityGroup {
    private String webUrl;
    private String webTitle;

    private TextView tv_finance, tv_account, tv_discovery;
    private LinearLayout ll_finance, ll_account, ll_discovery;
    protected MessageReceiver mMsgReceiver;

    private SwitchTabReceiver mSwitchTabReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MemorySave.MS.mIsFirstOpen = false;

        tv_finance = (TextView) findViewById(R.id.tv_msg_finance);
        tv_account = (TextView) findViewById(R.id.tv_msg_account);
        tv_discovery = (TextView) findViewById(R.id.tv_msg_more);
        ll_finance = (LinearLayout) findViewById(R.id.ll_msg_finance);
        ll_account = (LinearLayout) findViewById(R.id.ll_msg_account);
        ll_discovery = (LinearLayout) findViewById(R.id.ll_msg_discovery);
        getWindowManager().getDefaultDisplay().getMetrics(MemorySave.MS.dm);
        mMsgReceiver = new MessageReceiver();
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(MsgUtil.MSG_ACTION_ADD);
        intentfilter.addAction(MsgUtil.MSG_ACTION_REMOVE);
        intentfilter.addAction(MsgUtil.MSG_ACTION_REFRESH);
        registerReceiver(mMsgReceiver, intentfilter);

        mSwitchTabReceiver = new SwitchTabReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastImpl.ACTION_MAIN_SWITCH_TAB);
        registerReceiver(mSwitchTabReceiver, filter);

    }

    @Override
    public void onResume() {
        super.onResume();
        MemorySave.MS.mIsGoHome = false;
        webUrl = (String) SpUtils.getParam(SpUtils.Table.DEFAULT, "webUrl", "");
        webTitle = (String) SpUtils.getParam(SpUtils.Table.DEFAULT, "webTitle", "");

        if (!StringUtils.isEmpty(webUrl) && !MemorySave.MS.mWebOpenAppFlag) {
            Intent intentWeb = new Intent();
            intentWeb.putExtra("title", webTitle);
            if (!webUrl.contains("http")) {
                intentWeb.putExtra("web_url", "http://" + webUrl);
            } else {
                intentWeb.putExtra("web_url", webUrl);
            }
            intentWeb.setClass(MainActivity.this, MainWebActivity.class);
            startActivity(intentWeb);
        }
        if (MemorySave.MS.mIsGoFinance) {
            MemorySave.MS.mIsGoFinance = false;
            goProject();
            return;
        }
        if (MemorySave.MS.mIsGoAccount) {
            MemorySave.MS.mIsGoAccount = false;
            gotoAccount();
            return;
        }

        MessageLocalData invest_data = (MessageLocalData) MsgUtil.getObj(this, MsgUtil.INVEST);
        MessageLocalData account_data = (MessageLocalData) MsgUtil.getObj(this, MsgUtil.ACCOUNT);
        MessageLocalData dis_data = (MessageLocalData) MsgUtil.getObj(this, MsgUtil.DISCOVERY);
        if (invest_data != null) {
            setCount(0, invest_data.getCount());
        }
        if (account_data != null) {
            if (MemorySave.MS.oldAccountBadgeNum > 0) {
                setCount(1, account_data.getCount() + MemorySave.MS.oldAccountBadgeNum);
            } else {
                setCount(1, account_data.getCount());
            }
        } else {
            setCount(1, 0);
        }

        if (dis_data != null) {
            MessageTypeNew sysType = dis_data.getMap().get(MsgUtil.TYPE_DISCOVERY_SYSYNOTIFY);
            if (null != sysType) {// 减去系统通知的消息数目
                setCount(2, dis_data.getCount() - sysType.getMessages().size());
            } else {
                setCount(2, dis_data.getCount());
            }
        }
        int index = getIntent().getIntExtra("tempId", -1);
        if (index != -1) {
            switchTab(index);
        }

        //个推权限获取
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                getPackageManager().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        } else {
            //个推服务初始化
            PushManager.getInstance().initialize(this.getApplicationContext(), GtPushService.class);
        }
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GtIntentService.class);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext(), GtPushService.class);
            } else {
                PushManager.getInstance().initialize(this.getApplicationContext(), GtPushService.class);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 根据下标设置对应的消息数量
     *
     * @param position
     * @param count
     */
    public void setCount(int position, int count) {
        switch (position) {
            case 0:
                if (count == 0) {
                    ll_finance.setVisibility(View.INVISIBLE);
                } else {
                    ll_finance.setVisibility(View.VISIBLE);
                }
                tv_finance.setText(count + "");
                break;
            case 1:
                if (count == 0) {
                    ll_account.setVisibility(View.INVISIBLE);
                } else {
                    ll_account.setVisibility(View.VISIBLE);
                }
                tv_account.setText(count + "");
                break;
            case 2:
                if (count == 0) {
                    ll_discovery.setVisibility(View.INVISIBLE);
                } else {
                    ll_discovery.setVisibility(View.VISIBLE);
                }
                tv_discovery.setText(count + "");
                break;
            default:
                break;
        }
    }

    // 第一个需要实现的方法，直接返回ActivityGroup实现类的layou布局即可
    // 注意该布局一定要有个id为activity_group_container的布局用来放子Activity的布局
    @Override
    protected int getLayoutResourceId() {
        // 横向排列选项卡
        return R.layout.activity_group_bottom_layout;
        // 如果是纵向排列选项卡，可以返回下面这个布局
        // return R.layout.activity_group_left5_layout;
    }

    // 第二个需要实现的方法，返回layout布局下选项卡对应的radioButton的id
    @Override
    protected int[] getRadioButtonIds() {
        return new int[]{
                R.id.activity_group_radioButton1,
                R.id.activity_group_radioButton0,
                R.id.activity_group_radioButton2,
                R.id.activity_group_radioButton3
        };
    }

    // 第三个需要实现的方法，上面一个方法中的radioButton对应的图标，注意图标的尺寸要自己调整到合适大小
    @Override
    protected int[] getRadioButtonImageIds() {
        return new int[]{
                R.drawable.tab_icon_finance,
                R.drawable.tab_icon_project,
                R.drawable.tab_icon_account,
                R.drawable.tab_icon_more,
        };
    }

    // 第四个需要实现的方法，radioButton对应的文字，也就是选项卡标签的文字，
    // 最好不要太长，否则要到布局文件里调整文字大小到适应界面
    @Override
    protected String[] getRadioButtonTexts() {
        // return new String[]{"推荐","投资","理财","财富","更多"};
        return new String[]{"首页", "投资", "账户", "发现"};
    }

    // 第五个需要实现的方法，返回每个选项卡对应的第一个子Activity（注意要继承自AbsSubActivity）
    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Activity>[] getClasses() {
        Class<? extends Activity>[] classes = new Class[]{

                ManageFinanceActivity.class,
                ProjectActivity.class,
                AccountActivity.class,
                DiscoveryActivity.class
        };
        return classes;
    }

    // @SuppressWarnings("deprecation")
    // @Override
    // public void onBackPressed() {
    // // super.onBackPressed();
    // //把后退事件交给子Activity处理
    // this.getLocalActivityManager().getCurrentActivity().onBackPressed();
    // }

    //    点击back键退出应用
    Dialog mExitDialog;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mExitDialog = createDialog("您确定要退出吗？", "确定", "取消",
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (mExitDialog.isShowing()) {
                                mExitDialog.dismiss();
                            }
                            System.exit(0);
                        }
                    },
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (mExitDialog.isShowing()) {
                                mExitDialog.dismiss();
                            }
                        }
                    });
            mExitDialog.show();
//            return getCurrentActivity().onKeyDown(keyCode, event);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //    创建退出应用Dialog
    public Dialog createDialog(CharSequence message, String absoluteStr,
                               String negativeStr, View.OnClickListener absoluteListener,
                               View.OnClickListener negativeListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_dialog_no_message,
                null);
        TextView titleTV = (TextView) view
                .findViewById(R.id.dialog_nomessage_tv_title);
        TextView absoluteBT = (TextView) view
                .findViewById(R.id.dialog_nomessage_bt_absolute);
        TextView negativeBT = (TextView) view
                .findViewById(R.id.dialog_nomessage_bt_negative);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.88);
        titleTV.setLayoutParams(lp);

        if (android.os.Build.VERSION.SDK_INT < 12) {
            absoluteBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_absolute_12);
            negativeBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_negative_12);
        } else {
            absoluteBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_absolute);
            negativeBT
                    .setBackgroundResource(R.drawable.bg_dialog_button_negative);
        }

        titleTV.setText(message);
        absoluteBT.setText(absoluteStr);
        negativeBT.setText(negativeStr);

        absoluteBT.setOnClickListener(absoluteListener);
        negativeBT.setOnClickListener(negativeListener);

        Dialog dialog = new Dialog(this, R.style.style_loading_dialog);
        dialog.setContentView(view);
        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE) {
            setResult(0);
            if (resultCode == LoginActivity.LOGIN_SUCCESS) {
                goTemp();
            } else {
                // reback();
            }
        }
    }

    @Override
    protected void onDestroy() {
        MemorySave.MS.mIsFirstOpen = true;
        unregisterReceiver(mMsgReceiver);
        unregisterReceiver(mSwitchTabReceiver);
        super.onDestroy();
    }

    /**
     * 消息数量处理广播
     *
     * @author Administrator
     */
    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int position = intent.getIntExtra("flag", -1);
            String id = intent.getStringExtra("id");
            String type = intent.getStringExtra("type");
            MessageLocalData md;
            String key = "";
            switch (position) {
                case 0:
                    key = MsgUtil.INVEST;
                    break;
                case 1:
                    key = MsgUtil.ACCOUNT;
                    break;
                case 2:
                    key = MsgUtil.DISCOVERY;
                    break;
                default:
                    break;
            }
            if (key.equals("")) {
                return;
            }
            md = (MessageLocalData) MsgUtil.getObj(getApplicationContext(), key);
            if (intent.getAction().equals(MsgUtil.MSG_ACTION_ADD)) {
                // 新增消息

            } else if (intent.getAction().equals(MsgUtil.MSG_ACTION_REMOVE)) {
                // 减少消息
                int count = 0;
                List<MsgNew> msgNews = md.getMap().get(type).getMessages();
                for (MsgNew msgNew : msgNews) {
                    ++count;
                    if (msgNew.getMsg().equals(id)) {
                        msgNew.setDirty(true);
                        --count;
                    }
                }
                MsgUtil.setObj(getApplicationContext(), key, md);
                setCount(position, count);
            } else if (intent.getAction().equals(MsgUtil.MSG_ACTION_REFRESH)) {
                // 更新账户消息显示
                int count = 0;
                if (md != null) {
                    count = md.getCount();
                }
                if (position == 2) {
                    if (md.getMap() != null && md.getMap().size() > 0) { //减去系统通知消息数
                        MessageTypeNew sysNotifyMsg = md.getMap().get(MsgUtil.TYPE_DISCOVERY_SYSYNOTIFY);
                        if (null != sysNotifyMsg && sysNotifyMsg.getMessages() != null) {
                            setCount(position, count - sysNotifyMsg.getMessages().size());
                        }
                    } else {
                        setCount(position, count);
                    }
                } else if (position == 1) {
                    if (MemorySave.MS.oldAccountBadgeNum > 0) {
                        setCount(1, count + MemorySave.MS.oldAccountBadgeNum);
                    } else {
                        setCount(1, count);
                    }
                } else {
                    setCount(position, count);
                }
            }
        }
    }

    private class SwitchTabReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position", -1);
            switchTab(position);
        }
    }

}