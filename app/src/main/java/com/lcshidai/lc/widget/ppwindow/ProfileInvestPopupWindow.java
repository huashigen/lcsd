package com.lcshidai.lc.widget.ppwindow;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.impl.finance.FinanceInvestPBuyCheckImpl;
import com.lcshidai.lc.model.finance.FinanceMaxInvestMoneyJson;
import com.lcshidai.lc.model.finance.FinanceTabItemProductItem;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckJson;
import com.lcshidai.lc.service.finance.HttpFinanceInvestCheckService;
import com.lcshidai.lc.ui.account.UserPayPwdFirstSetActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

/**
 * 投资准备
 *
 * @author 000814
 */
public class ProfileInvestPopupWindow extends PopupWindow implements
        FinanceInvestPBuyCheckImpl {
    private View mMenuView;
    TRJActivity context;
    private Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, b00, bpoint, bbid;
    TextView bplus, bminus;
    View viewPlus, viewMinus, viewTop, bx, bhide;
    CustomEditTextLeftIcon cet;
    PIPWCallBack mCallBack;

    int plusValue = 1000;
    Dialog mLoadDialog;
    float minMoney, maxMoney;

    FinanceTabItemProductItem pi;
    boolean hidetop;

    HttpFinanceInvestCheckService checkService;
    private DialogPopupWindow dialogPopupWindow;
    private View mainView;

    public ProfileInvestPopupWindow(TRJActivity context, PIPWCallBack callBack) {
        super(context);
        this.context = context;
        checkService = new HttpFinanceInvestCheckService(context, this);
        mCallBack = callBack;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.profile_investpw, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb00000aa);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mainView = mMenuView.findViewById(R.id.absmain);
        dialogPopupWindow = new DialogPopupWindow(this.context, mainView, null);
        cet = (CustomEditTextLeftIcon) mMenuView.findViewById(R.id.money);
        cet.getET().clearFocus();
        cet.getET().setInputType(InputType.TYPE_NULL);
        bbid = (Button) mMenuView.findViewById(R.id.pp_btn_right);

        viewTop = mMenuView.findViewById(R.id.ll_top);

        b1 = (Button) mMenuView.findViewById(R.id.b1);
        b1.setOnClickListener(new BtnClick(1));
        b2 = (Button) mMenuView.findViewById(R.id.b2);
        b2.setOnClickListener(new BtnClick(2));
        b3 = (Button) mMenuView.findViewById(R.id.b3);
        b3.setOnClickListener(new BtnClick(3));
        b4 = (Button) mMenuView.findViewById(R.id.b4);
        b4.setOnClickListener(new BtnClick(4));
        b5 = (Button) mMenuView.findViewById(R.id.b5);
        b5.setOnClickListener(new BtnClick(5));
        b6 = (Button) mMenuView.findViewById(R.id.b6);
        b6.setOnClickListener(new BtnClick(6));
        b7 = (Button) mMenuView.findViewById(R.id.b7);
        b7.setOnClickListener(new BtnClick(7));
        b8 = (Button) mMenuView.findViewById(R.id.b8);
        b8.setOnClickListener(new BtnClick(8));
        b9 = (Button) mMenuView.findViewById(R.id.b9);
        b9.setOnClickListener(new BtnClick(9));
        b0 = (Button) mMenuView.findViewById(R.id.b0);
        b0.setOnClickListener(new BtnClick(0));
        b00 = (Button) mMenuView.findViewById(R.id.b00);
        b00.setOnClickListener(new BtnClick(10));
        bpoint = (Button) mMenuView.findViewById(R.id.bpoint);
        bpoint.setOnClickListener(new BtnClick(111));
        bplus = (TextView) mMenuView.findViewById(R.id.bplus);
        viewPlus = mMenuView.findViewById(R.id.view_plus);
        viewPlus.setOnClickListener(new BtnClick(11));
        bminus = (TextView) mMenuView.findViewById(R.id.bminus);
        viewMinus = mMenuView.findViewById(R.id.view_minus);
        viewMinus.setOnClickListener(new BtnClick(12));
        bx = mMenuView.findViewById(R.id.bx);
        bx.setOnClickListener(new BtnClick(13));

        bhide = mMenuView.findViewById(R.id.bhide);
        bhide.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ProfileInvestPopupWindow.this.dismiss();
            }
        });
        mLoadDialog = TRJActivity.createLoadingDialog(context, "加载中...", false);
        mLoadDialog.setCanceledOnTouchOutside(false);
    }

    public void showPre(final FinanceTabItemProductItem pi) {
        ((TextView) mMenuView.findViewById(R.id.tv_prj_type)).setText(pi.getPrj_type_name() + ":");
        ((TextView) mMenuView.findViewById(R.id.tv_prj_name)).setText("期限" + pi.getTime_limit() +
                pi.getTime_limit_unit_view());

        ((TextView) mMenuView.findViewById(R.id.content_tv_1)).setText(pi.getYear_rate() + "%");

        ((TextView) mMenuView.findViewById(R.id.content_tv_3)).setText(pi
                .getWanyuanProfit().replace("元", ""));

        if (pi.getHomeFlag().equals("1")) {
            if (pi.getMax_bid_amount_view() == null || pi.getMax_bid_amount_view().equals("")
                    || pi.getMax_bid_amount_view().equals("0.00")) {
                ((TextView) mMenuView.findViewById(R.id.content_tv_2))
                        .setText(pi.getMin_bid_amount_name() + "+");
            } else {
                ((TextView) mMenuView.findViewById(R.id.content_tv_2))
                        .setText(pi.getMin_bid_amount_name() + "-"
                                + pi.getMax_bid_amount_view());
            }
        } else {
            if (pi.getMax_bid_amount_name().equals("0.00")) {
                ((TextView) mMenuView.findViewById(R.id.content_tv_2))
                        .setText(pi.getMin_bid_amount_raw() + "+");
            } else {
                ((TextView) mMenuView.findViewById(R.id.content_tv_2))
                        .setText(pi.getMin_bid_amount_raw() + "-"
                                + pi.getMax_bid_amount_raw());
            }
        }

        if (!StringUtils.isInteger(pi.getSchedule()))
            pi.setSchedule("0");
        ((ProgressBar) mMenuView.findViewById(R.id.pb)).setProgress(Integer
                .parseInt(pi.getSchedule()));

        bbid.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                float va = Float.parseFloat(cet.getEdtText().equals("") ? "0"
                        : cet.getEdtText());
                check(va, pi.getId(), "0", bbid, pi.getIs_pre_sale().equals("1")
                        && pi.getBid_status().equals("1"), pi.getYear_rate()
                        + "%");
            }
        });
    }

    class BtnClick implements OnClickListener {

        int value;

        public BtnClick(int value) {
            this.value = value;
        }

        @Override
        public void onClick(View v) {
            String str;
            str = cet.getEdtText();
            try {
                float va = Float.parseFloat(str.equals("") ? "0" : str);
                switch (value) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        if (str.length() < 9) {
                            if (va == 0) {
                                str = value + "";
                            } else {
                                str = str + value;
                            }
                        }
                        break;
                    case 10:
                        if (va < 1) {
                            return;
                        }
                        if (str.length() < 8)
                            str = str + "00";
                        break;
                    case 11:
                        va += plusValue;
                        str = va + "";
                        break;
                    case 12:
                        if (va < plusValue) {
                            str = "0";
                        } else {
                            str = va - plusValue + "";
                        }
                        break;
                    case 13:
                        if (str.length() > 1) {
                            str = str.substring(0, str.length() - 1);
                        } else {
                            str = "0";
                        }
                        break;
                    case 111:
                        if (str.indexOf(".") == -1) {
                            str += ".";
                        }
                        break;
                    default:
                        str = "0";
                        break;
                }
                float ve = 0;
                if (StringUtils.isFloat(str)) {
                    Float.parseFloat(str);
                } else {
                    str = "0";
                }
                // if(ve<minMoney){
                // str=minMoney+"";
                // }
                if (maxMoney != 0)
                    if (ve > maxMoney) {
                        str = maxMoney + "";
                        ToastUtil.showToast(context, "余额不足或超过投资上限");
                    }
            } catch (Exception e) {
                str = "0";
            }
            cet.setText(str);
        }

    }

    public void goAnim(View mContainer, FinanceTabItemProductItem pi,
                       boolean hidetop) {
        this.mContainer = mContainer;
        this.pi = pi;
        this.hidetop = hidetop;
        gainMaxInvestMoney();

    }

    private void goAn() {
        // cet.setHint(pi.min_bid_amount+"元以上");
        maxMoney = 0f;
        if (StringUtils.isFloat(pi.getMin_bid_amount_raw())) {
            minMoney = Float.parseFloat(pi.getMin_bid_amount_raw());
        } else {
            minMoney = 0f;
        }
        if (pi.getIs_pre_sale().equals("1") && pi.getBid_status().equals("1")) {
            bbid.setText("预售投资");
            bbid.setBackgroundDrawable(context.getResources().getDrawable(
                    R.drawable.feedback_submit_blue));
        } else {
            bbid.setText("立即投资");
            bbid.setBackgroundDrawable(context.getResources().getDrawable(
                    R.drawable.feedback_submit_bg_xml));
        }
        cet.setText(minMoney + "");
        String step = StringUtils.isInteger(pi.getStep_bid_amount()) ? pi
                .getStep_bid_amount() : "100";
        plusValue = Integer.parseInt(step);
        bminus.setText(step);
        bplus.setText(step);
        showAtLocation(mContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0, 0); // 设置layout在PopupWindow中显示的位置
        if (hidetop) {
            viewTop.setVisibility(View.GONE);
            bbid.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    long va = Integer
                            .parseInt(cet.getEdtText().equals("") ? "0" : cet
                                    .getEdtText());
                    check(va, pi.getId(), "0", bbid, pi.getIs_pre_sale().equals("1")
                                    && pi.getBid_status().equals("1"),
                            pi.getYear_rate() + "%");
                }
            });
            return;
        } else {
            viewTop.setVisibility(View.VISIBLE);
            showPre(pi);
        }

        if (android.os.Build.VERSION.SDK_INT < 12)
            return;
        final ViewPropertyAnimator animate = mContainer.animate();
        animate.setDuration(mDuration);
        View movingFragmentView = mContainer;

        movingFragmentView.setPivotY(movingFragmentView.getHeight() / 2);
        movingFragmentView.setPivotX(movingFragmentView.getWidth() / 2);

        PropertyValuesHolder rotateX = PropertyValuesHolder.ofFloat("rotationX", 15f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.9f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.9f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.4f);
        ObjectAnimator movingFragmentAnimator = ObjectAnimator.
                ofPropertyValuesHolder(movingFragmentView, rotateX, scaleX, scaleY, alpha);

        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(
                movingFragmentView, "rotationX", 0);
        movingFragmentAnimator.setStartDelay(150);
        movingFragmentRotator.setStartDelay(350);
        movingFragmentRotator.setDuration(300);
        movingFragmentAnimator.setDuration(300);
        movingFragmentAnimator.start();
        movingFragmentRotator.start();

        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                View movingFragmentView = ProfileInvestPopupWindow.this.mContainer;
                PropertyValuesHolder rotateXr = PropertyValuesHolder.ofFloat("rotationX", 15f);
                PropertyValuesHolder scaleXr = PropertyValuesHolder.ofFloat("scaleX", 1.0f);
                PropertyValuesHolder scaleYr = PropertyValuesHolder.ofFloat("scaleY", 1.0f);
                PropertyValuesHolder alphar = PropertyValuesHolder.ofFloat("alpha", 1f);
                ObjectAnimator movingFragmentAnimatorr = ObjectAnimator.
                        ofPropertyValuesHolder(movingFragmentView, rotateXr, scaleXr, scaleYr, alphar);
                ObjectAnimator movingFragmentRotatorr = ObjectAnimator.ofFloat(
                        movingFragmentView, "rotationX", 0);
                movingFragmentAnimatorr.setStartDelay(150);
                movingFragmentRotatorr.setStartDelay(350);
                movingFragmentAnimatorr.setDuration(300);
                movingFragmentRotatorr.setDuration(300);
                movingFragmentRotatorr.start();
                movingFragmentAnimatorr.start();
            }
        });
    }

    float ct = 1 / 2;
    public View mContainer = null;
    int mDuration = 1000;
    float depz = 0.1f;
    int degress = 10;

    private void gainMaxInvestMoney() {
        if (mLoadDialog != null && !mLoadDialog.isShowing()) {
            mLoadDialog.show();
        }
        checkService.getMaxAvaInvestAmount(pi.getId(), pi.getIs_collection());
    }

    View btnView;

    /**
     * 立即投资检查
     *
     * @param va
     * @param id
     */
    public void check(final float va, final String id, final String jxq_id, final View btnView,
                      final boolean isRepay, final String yearrate) {
        this.btnView = btnView;
        btnView.setEnabled(false);
        if (mLoadDialog != null && !mLoadDialog.isShowing()) {
            mLoadDialog.show();
        }
        checkService.doInvestCheck(va, id, jxq_id, isRepay, yearrate);
    }

    @Override
    public void getMaxAvaInvestAmountSuccess(FinanceMaxInvestMoneyJson result) {
        if (result != null) {
            if (result.getBoolen() != null && result.getBoolen().equals("1")) {
                String income = result.getData();
                goAn();
                if (StringUtils.isFloat(income)) {
                    maxMoney = Float.parseFloat(income);
                    cet.setText(maxMoney + "");
                }
            } else {
                if (result.getMessage() != null
                        && result.getMessage().indexOf("余额不足") != -1) {
                    dialogPopupWindow.setrechargeactivity("recharge");
                    dialogPopupWindow.showWithMessage(
                            result.getMessage(), "0");
                } else if (result.getMessage() != null && result.getMessage().indexOf("请登入") == -1) {
                    ToastUtil.showToast(context, result.getMessage());
                }
                if (result.getLogined() != null
                        && result.getLogined().trim().equals("0")) {
                    MemorySave.MS.mFinanceToLoginLock = true;
                }
            }
        }
        if (mLoadDialog != null)
            mLoadDialog.dismiss();
    }

    @Override
    public void doInvestCheckSuccess(FinanceInvestPBuyCheckJson result, float va, String id,
                                     boolean isRepay, String yearrate) {

        if (result != null) {
            if (result.getBoolen() != null && result.getBoolen().equals("1") && result.getData() != null) {
                result.getData().setMoney(va + "");
                result.getData().setRepay(isRepay);
                result.getData().setPrjid(id);
                result.getData().setYearrate(yearrate);
                mCallBack.doInvestCheckSuccess(true, result.getData());
            } else {
                if (result.getMessage() != null
                        && result.getMessage().indexOf("支付密码") != -1) {
                    Intent intent = new Intent(context,
                            UserPayPwdFirstSetActivity.class);
                    context.startActivity(intent);
                } else {
                    if (result.getMessage() != null
                            && result.getMessage().indexOf("余额不足") != -1)
                        dialogPopupWindow.showWithMessage(
                                result.getMessage(), 0 + "");
                }
            }
        }
        btnView.setEnabled(true);
        if (mLoadDialog != null)
            mLoadDialog.dismiss();
    }

    @Override
    public void getMaxAvaInvestAmountFail() {
        if (mLoadDialog != null)
            mLoadDialog.dismiss();
    }

    @Override
    public void doInvestCheckFail(String response) {
        if (!StringUtils.isEmpty(response)) {
            ToastUtil.showToast(context, response);
        }
        btnView.setEnabled(true);
        if (mLoadDialog != null)
            mLoadDialog.dismiss();
    }
}
