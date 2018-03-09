package com.lcshidai.lc.ui.base;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.DiscoveryActivity;
import com.lcshidai.lc.ui.account.AccountActivity;
import com.lcshidai.lc.ui.finance.ManageFinanceActivity;
import com.lcshidai.lc.ui.newfinan.NewFinanceActivity;
import com.lcshidai.lc.ui.project.ProjectActivity;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;

import java.util.HashMap;
import java.util.Map;

/**
 * 继承该类并实现其五个抽象方法即可
 */
public abstract class AbsActivityGroup extends ActivityGroup {

    public boolean backFlag = false;
    public int tempId;

    // 功能模块跳转的目标Intent
    protected Intent targetIntent = new Intent();
    // 用来加载子Activity的布局
    private FrameLayout container = null;
    // 选项卡
    private RadioGroup radioGroup = null;
    // 选中标签的ID
    private int radioGroupCheckId;
    // 切换标签动作的标志位
    private boolean changedFlag;
    // 选项卡的所有标签
    public RadioButton[] radioButtons = null;
    // 选项卡所有标签的ID
    private int[] radioButtonIds = null;
    // 选项卡所有标签的图标ID
    private int[] radioButtonImageIds;
    // 选项卡所有标签的文字
    private String[] radioButtonTexts;
    // 标签ID对应的初始Activity集合
    private Map<Integer, Class<? extends Activity>> classes = new HashMap<>();
    // 标签ID对应的当前Activity集合
    public Map<Integer, Class<? extends Activity>> currentClasses = new HashMap<>();

    // 在子类中实现的设定布局的方法，Activity的布局的id必须为activity_group_container；
    // 选项卡的id必须为activity_group_radioGroup
    protected abstract int getLayoutResourceId();

    // 在子类中需要实现的获取选项卡所有标签的ID的方法
    protected abstract int[] getRadioButtonIds();

    // 在子类中需要实现的获取选项卡所有标签的图标的方法
    protected abstract int[] getRadioButtonImageIds();

    // 在子类中需要实现的获取选项卡所有标签的文字的方法*/
    protected abstract String[] getRadioButtonTexts();

    // 在子类中需要实现的获取选项卡所有标签ID对应的初始Activity的方法
    public abstract Class<? extends Activity>[] getClasses();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTheme(android.R.style.Theme_Holo_NoActionBar_TranslucentDecor);
        } else {
            setTheme(android.R.style.Theme_NoTitleBar);
        }
        setContentView(getLayoutResourceId());
        // 设定原始数据
        setData();
        // 初始化控件
        initWidgetStatic();
    }

    /**
     * 设定数据源的方法
     */
    protected void setData() {
        radioButtonIds = getRadioButtonIds();
        radioButtonImageIds = getRadioButtonImageIds();
        radioButtonTexts = getRadioButtonTexts();
        Class<? extends Activity>[] classNames = getClasses();
        for (int i = 0; i < radioButtonIds.length; i++) {
            classes.put(radioButtonIds[i], classNames[i]);
            currentClasses.put(radioButtonIds[i], classNames[i]);
        }
    }

    /**
     * 初始化控件
     */
    protected void initWidgetStatic() {
        container = (FrameLayout) findViewById(R.id.activity_group_container);
        radioGroup = (RadioGroup) findViewById(R.id.activity_group_radioGroup);
        radioButtons = new RadioButton[radioButtonIds.length];
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i] = (RadioButton) findViewById(radioButtonIds[i]);
            if (radioButtonImageIds != null) {
                radioButtons[i].setText(radioButtonTexts[i]);
                Drawable drawable = getResources().getDrawable(radioButtonImageIds[i]);
                radioButtons[i].setCompoundDrawablesWithIntrinsicBounds(null,
                        drawable, null, null);
            }
            radioButtons[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!changedFlag) {//点击当前状态下tab
                        targetIntent.setClass(AbsActivityGroup.this,
                                classes.get(radioGroupCheckId));
                        launchActivity(targetIntent);
                        String name = classes.get(radioGroupCheckId).getName();
                        if (name.equals(ManageFinanceActivity.class.getName())) {
                            MemorySave.MS.goToFinanceAll = true;
                        } else if (name.equals(ManageFinanceActivity.class.getName())) {
                            MemorySave.MS.goToFinanceRecommend = true;
                        }
                    }
                    changedFlag = false;
                }
            });
        }

        // 给选项卡设定监听
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changedFlag = true;
                tempId = 0;//重置gotemp()跳转，如果不是指向账户的登录不做记录
                if (!backFlag) {
                    String name = classes.get(checkedId).getName();
//                    账户页
                    if (name.equals(AccountActivity.class.getName())) {
                        if (!MemorySave.MS.mIsLogin) {
                            boolean isTrue = GoLoginUtil.ToLoginActivityForResultBase((TRJActivity) AbsActivityGroup.this.getCurrentActivity(), Constants.REQUEST_CODE, "");
                            tempId = radioGroupCheckId;
                            switch (radioGroupCheckId) {
                                case R.id.activity_group_radioButton0:
                                    switchTab(1);
                                    break;
                                case R.id.activity_group_radioButton1:
                                    switchTab(0);
                                    break;
                                case R.id.activity_group_radioButton2:
                                    switchTab(2);
                                    break;
                                case R.id.activity_group_radioButton3:
                                    switchTab(3);
                                    break;
                            }
                            if (!isTrue) return;
                        }
//                        首页
                    } else if (name.equals(ManageFinanceActivity.class.getName())) {
                        MemorySave.MS.goToFinanceAll = true;
                        //mfActivity.chooseItemNoreloadReALL();
//					}else if(name.equals(ManageTransferActivity.class.getName())){
                    } else if (name.equals(ManageFinanceActivity.class.getName())) {
                        MemorySave.MS.goToFinanceRecommend = true;
                    }

                } else {
                    backFlag = false;
                }
                currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
                        .getCurrentActivity().getClass());
                setTargetIntent(checkedId);
                launchActivity(targetIntent);
                radioGroupCheckId = checkedId;
            }
        });

        // 初始化加载
        radioGroupCheckId = getCheckedRadioButtonId();
        setTargetIntent(radioGroupCheckId);
        launchNewActivity(targetIntent);
    }

    /**
     * 设定目标Intent的方法
     */
    protected void setTargetIntent(int checkedId) {
        targetIntent.setClass(AbsActivityGroup.this, currentClasses.get(checkedId));
    }

    //	/** 设定目标Intent的方法 */
    public void goIt(int checkedId) {
        currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
                .getCurrentActivity().getClass());
        setTargetIntent(checkedId);
        launchActivity(targetIntent);
        radioGroupCheckId = checkedId;
        radioGroup.check(checkedId);
        changedFlag = true;
    }

    /**
     * 设定目标Intent的方法
     */
    public void goBackIt(int checkedId) {
        currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
                .getCurrentActivity().getClass());
        targetIntent.setClass(AbsActivityGroup.this, classes.get(checkedId));
        launchActivity(targetIntent);
        radioGroupCheckId = checkedId;
        radioGroup.check(checkedId);
        changedFlag = true;
    }

    protected void gotoAccount() {
        for (Integer it : classes.keySet()) {
            String name = classes.get(it).getName();
            if (name.equals(AccountActivity.class.getName())) {
                //radioGroup.check(it);
                ((RadioButton) findViewById(it)).setChecked(true);
                currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
                        .getCurrentActivity().getClass());
                return;
            }
        }
    }

    protected void goProject() {
        for (Integer it : classes.keySet()) {
            String name = classes.get(it).getName();
//            投资页
            if (name.equals(ProjectActivity.class.getName())) {
                //radioGroup.check(it);
                ((RadioButton) findViewById(it)).setChecked(true);
                currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
                        .getCurrentActivity().getClass());
                return;
            }
        }
    }

    /**
     * 设定目标Intent的方法
     */
    public void goFinanceTemp() {
        for (Integer it : classes.keySet()) {
            String name = classes.get(it).getName();
            if (name.equals(ManageFinanceActivity.class.getName())) {
                //radioGroup.check(it);
                ((RadioButton) findViewById(it)).setChecked(true);
                currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
                        .getCurrentActivity().getClass());
                return;
            }
        }
    }

    /**
     * 设定目标Intent的方法
     */
    public void goTransferTemp() {
        for (Integer it : classes.keySet()) {
            String name = classes.get(it).getName();
            if (name.equals(NewFinanceActivity.class.getName())) {
                ((RadioButton) findViewById(it)).setChecked(true);
                currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
                        .getCurrentActivity().getClass());
                return;
            }
        }
    }

    /**
     * 跳转至账户子页面
     *
     * @param intent 设置账户子页面跳转的activity
     */
    public void goAccount(Intent intent) {
        for (Integer it : classes.keySet()) {
            String name = classes.get(it).getName();
            if (name.equals(AccountActivity.class.getName())) {
                goIt(it);
                launchNewActivity(intent.putExtra("fromSubActivity", AccountActivity.class.getName()));
                return;
            }
        }
    }

    /**
     * 跳转至财富中心
     */
    public void goAccountTemp() {
        for (Integer it : classes.keySet()) {
            String name = classes.get(it).getName();
            if (name.equals(AccountActivity.class.getName())) {
                ((RadioButton) findViewById(it)).setChecked(true);
                currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
                        .getCurrentActivity().getClass());
                return;
            }
        }
    }


    /**
     * 跳转至首页
     */
    public void goMoreTemp() {
        for (Integer it : classes.keySet()) {
            String name = classes.get(it).getName();
//            发现页
            if (name.equals(DiscoveryActivity.class.getName())) {
                ((RadioButton) findViewById(it)).setChecked(true);
                currentClasses.put(radioGroupCheckId, AbsActivityGroup.this
                        .getCurrentActivity().getClass());
                return;
            }
        }
    }

    /**
     * ActivityGroup加载新的子Activity的方法(创建新的)
     */
    public void launchNewActivity(Intent intent) {
        container.removeAllViews();
        container.addView(getLocalActivityManager().startActivity(
                intent.getComponent().getShortClassName() + getCheckedRadioButtonId(),
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
    }

    /**
     * ActivityGroup加载新的子Activity的方法(创建新的)
     */
    public void launchNewActivityForResult(AbsSubActivity requestSubActivity,
                                           Intent intent, int requestCode) {
        container.removeAllViews();
        container.addView(getLocalActivityManager().startActivity(
                intent.getComponent().getShortClassName() + getCheckedRadioButtonId(),
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());
        ((AbsSubActivity) getCurrentActivity()).setRequestSubActivity(requestSubActivity);
    }

    /**
     * ActivityGroup加载子Activity的方法(先看有没有，有则加载原来的，否则创建新的)
     */
    public void launchActivity(Intent intent) {
        try {
            container.removeAllViews();
            container.addView(getLocalActivityManager().startActivity(
                    intent.getComponent().getShortClassName() + getCheckedRadioButtonId(),
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)).getDecorView());
        } catch (Exception e) {

        }
    }


    /**
     * 这个方法用于获取当前ActivityGroup的选项卡按下的单选按钮的ID
     */
    public int getCheckedRadioButtonId() {
        return radioGroup.getCheckedRadioButtonId();
    }

    public void goTemp() {
        if (tempId != 0) goIt(tempId);
    }

    protected void switchTab(int position) {
        radioGroup.getChildAt(position).performClick();
    }
}
