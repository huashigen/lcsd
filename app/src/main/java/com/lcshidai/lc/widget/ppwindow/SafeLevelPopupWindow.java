package com.lcshidai.lc.widget.ppwindow;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.account.SafeQuestionActivity;
import com.lcshidai.lc.ui.account.UserMailAuthActivity;
import com.lcshidai.lc.ui.account.UserMailUpdaterActivity;
import com.lcshidai.lc.ui.account.UserMobileAuthActivity;
import com.lcshidai.lc.ui.account.UserMobileUpdaterActivity;
import com.lcshidai.lc.ui.account.UserPayPwdFirstSetActivity;
import com.lcshidai.lc.ui.account.pwdmanage.GesturePwdActivity;
import com.lcshidai.lc.ui.account.pwdmanage.UserMobilePayPwdActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.Thumbnail;


public class SafeLevelPopupWindow extends PopupWindow implements OnClickListener {
    private TRJActivity mContext;
    private TextView titleText;
    private ImageView star_1, star_2, star_3, star_4, star_5;
    private LinearLayout ll_mobile, ll_login_psw, ll_email, ll_real_name, ll_lock_psw, ll_pay_psw, ll_question;
    private TextView tv_mobile, tv_login_psw, tv_email, tv_real_name, tv_lock_psw, tv_pay_psw, tv_question;
    private ImageView img_mobile, img_login_psw, img_email, img_real_name, img_lock_psw, img_pay_psw, img_question;
    private TextView cancel;
    private boolean mobile, loginPsw, email, realName, gesture, payPsw, question;
    private final String openEscrowUrl;
    private Dialog mRzDialog;

    private Dialog openEcwDialog = null;

    public SafeLevelPopupWindow(TRJActivity context, Bundle bundle) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        填充布局
        View contentView = inflater.inflate(R.layout.pp_safe_level, null);

        titleText = (TextView) contentView.findViewById(R.id.title);
        star_1 = (ImageView) contentView.findViewById(R.id.star_1);
        star_2 = (ImageView) contentView.findViewById(R.id.star_2);
        star_3 = (ImageView) contentView.findViewById(R.id.star_3);
        star_4 = (ImageView) contentView.findViewById(R.id.star_4);
        star_5 = (ImageView) contentView.findViewById(R.id.star_5);

        ll_mobile = (LinearLayout) contentView.findViewById(R.id.ll_mobile);
        tv_mobile = (TextView) contentView.findViewById(R.id.tv_mobile);
        img_mobile = (ImageView) contentView.findViewById(R.id.img_mobile);

        ll_login_psw = (LinearLayout) contentView.findViewById(R.id.ll_login_psw);
        tv_login_psw = (TextView) contentView.findViewById(R.id.tv_login_psw);
        img_login_psw = (ImageView) contentView.findViewById(R.id.img_login_psw);

        ll_email = (LinearLayout) contentView.findViewById(R.id.ll_email);
        tv_email = (TextView) contentView.findViewById(R.id.tv_email);
        img_email = (ImageView) contentView.findViewById(R.id.img_email);

        ll_real_name = (LinearLayout) contentView.findViewById(R.id.ll_real_name);
        tv_real_name = (TextView) contentView.findViewById(R.id.tv_real_name);
        img_real_name = (ImageView) contentView.findViewById(R.id.img_real_name);

        ll_lock_psw = (LinearLayout) contentView.findViewById(R.id.ll_lock);
        tv_lock_psw = (TextView) contentView.findViewById(R.id.tv_lock);
        img_lock_psw = (ImageView) contentView.findViewById(R.id.img_lock);

        ll_pay_psw = (LinearLayout) contentView.findViewById(R.id.ll_pay);
        tv_pay_psw = (TextView) contentView.findViewById(R.id.tv_pay);
        img_pay_psw = (ImageView) contentView.findViewById(R.id.img_pay);

        ll_question = (LinearLayout) contentView.findViewById(R.id.ll_question);
        tv_question = (TextView) contentView.findViewById(R.id.tv_question);
        img_question = (ImageView) contentView.findViewById(R.id.img_question);

        cancel = (TextView) contentView.findViewById(R.id.cancel);

        ll_mobile.setOnClickListener(this);
        ll_login_psw.setOnClickListener(this);
        ll_email.setOnClickListener(this);
        ll_real_name.setOnClickListener(this);
        ll_lock_psw.setOnClickListener(this);
        ll_pay_psw.setOnClickListener(this);
        ll_question.setOnClickListener(this);
        cancel.setOnClickListener(this);

        String safeLevel = bundle.getString("safeLevel");
        int starNum = bundle.getInt("starNum");
        mobile = bundle.getBoolean("is_mobile_auth");
        loginPsw = bundle.getBoolean("is_login_psw");
        email = bundle.getBoolean("is_email_auth");
        realName = bundle.getBoolean("is_real_name");
        gesture = bundle.getBoolean("is_gesture_lock");
        payPsw = bundle.getBoolean("is_paypwd");
        question = bundle.getBoolean("is_sqa");
        openEscrowUrl = bundle.getString("openEscrowUrl");

        setTitle(safeLevel);
        setStars(starNum);

        setAttest();

        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.PopAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_mobile:
                if (mobile) {
                    intent = new Intent(mContext, UserMobileUpdaterActivity.class);
                } else {
                    intent = new Intent(mContext, UserMobileAuthActivity.class);
                    intent.putExtra("mode", 1);
                }
                mContext.startActivity(intent);
                break;
            case R.id.ll_login_psw:
                break;
            case R.id.ll_email:
//			intent = new Intent(mContext, UserMailUpdaterActivity.class);
//			mContext.startActivity(intent);
                if (email) {
                    intent = new Intent(mContext, UserMailUpdaterActivity.class);
                } else {
                    intent = new Intent(mContext, UserMailAuthActivity.class);
                    intent.putExtra("mode", 1);
                }
                mContext.startActivity(intent);
                break;
            case R.id.ll_real_name:
//			intent = new Intent();
//			intent.putExtra("web_url", MainWebActivity.RELEASEE_URL_WAP);
//			intent.setClass(mContext, MainWebActivity.class);
//			mContext.startActivity(intent);
                if (!realName) {
//				String title = "温馨提示";
//				String msg = "充值成功后系统自动进行实名认证！";
//				String absBtn = "现在去充值";
//				String negBtn = "取消";
//				mRzDialog = mContext.createDialog1(title , msg , absBtn , negBtn , new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						Intent new_recharge_intent = new Intent();
//						new_recharge_intent.putExtra("web_url", MainWebActivity.RELEASEE_URL_WAP);
//						new_recharge_intent.setClass(mContext, MainWebActivity.class);
//						mContext.startActivity(new_recharge_intent);
//						mRzDialog.dismiss();
//					}
//				} , new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						mRzDialog.dismiss();
//					}
//				});
//				mRzDialog.show();
//                    showOpenEcwDialog();
                }
                break;
            case R.id.ll_lock:
                intent = new Intent();
                intent.setClass(mContext, GesturePwdActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.ll_pay:
                intent = new Intent();
//			intent.setClass(mContext, UserMobilePayPwdActivity.class);
//			mContext.startActivity(intent);
                if (payPsw) {
                    intent.setClass(mContext, UserMobilePayPwdActivity.class);
                } else {
                    intent.setClass(mContext, UserPayPwdFirstSetActivity.class);
                    intent.putExtra("from_activity", 1);
                }
                mContext.startActivity(intent);
                break;
            case R.id.ll_question:
                intent = new Intent();
                intent.setClass(mContext, SafeQuestionActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.cancel:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    private void setTitle(String safe) {
        titleText.setText("安全等级:" + safe);
    }

    private void setStars(int starNum) {
        float safeLevel = (starNum / 100f) * 5;
        int num = (int) safeLevel;
        boolean isHalf = false;
        if (safeLevel > num) {
            isHalf = true;
        }
        Thumbnail thumbnail = Thumbnail.init(mContext);
        ImageView[] views = {star_1, star_2, star_3, star_4, star_5};
        for (int i = 0; i < num; i++) {
            views[i].setImageBitmap(thumbnail.readBitMap(mContext, R.drawable.img_star_theme));
        }
        if (isHalf) {
            if (num < views.length) {
                views[num].setImageBitmap(thumbnail.readBitMap(mContext, R.drawable.img_star_half));
            }
        }
    }

    private void setAttest() {
        if (!mobile) {
            ll_mobile.setClickable(true);
            tv_mobile.setVisibility(View.VISIBLE);
            img_mobile.setVisibility(View.GONE);
        } else {
            ll_mobile.setClickable(false);
            tv_mobile.setVisibility(View.GONE);
            img_mobile.setVisibility(View.VISIBLE);
        }

        if (!loginPsw) {
            ll_login_psw.setClickable(true);
            tv_login_psw.setVisibility(View.VISIBLE);
            img_login_psw.setVisibility(View.GONE);
        } else {
            ll_login_psw.setClickable(false);
            tv_login_psw.setVisibility(View.GONE);
            img_login_psw.setVisibility(View.VISIBLE);
        }

        if (!email) {
            ll_email.setClickable(true);
            tv_email.setVisibility(View.VISIBLE);
            img_email.setVisibility(View.GONE);
        } else {
            ll_email.setClickable(false);
            tv_email.setVisibility(View.GONE);
            img_email.setVisibility(View.VISIBLE);
        }

        if (!realName) {
            ll_real_name.setClickable(true);
            tv_real_name.setVisibility(View.VISIBLE);
            img_real_name.setVisibility(View.GONE);
        } else {
            ll_real_name.setClickable(false);
            tv_real_name.setVisibility(View.GONE);
            img_real_name.setVisibility(View.VISIBLE);
        }

        if (!gesture) {
            ll_lock_psw.setClickable(true);
            tv_lock_psw.setVisibility(View.VISIBLE);
            img_lock_psw.setVisibility(View.GONE);
        } else {
            ll_lock_psw.setClickable(false);
            tv_lock_psw.setVisibility(View.GONE);
            img_lock_psw.setVisibility(View.VISIBLE);
        }

        if (!payPsw) {
            ll_pay_psw.setClickable(true);
            tv_pay_psw.setVisibility(View.VISIBLE);
            img_pay_psw.setVisibility(View.GONE);
        } else {
            ll_pay_psw.setClickable(false);
            tv_pay_psw.setVisibility(View.GONE);
            img_pay_psw.setVisibility(View.VISIBLE);
        }

        if (!question) {
            ll_question.setClickable(true);
            tv_question.setVisibility(View.VISIBLE);
            img_question.setVisibility(View.GONE);
        } else {
            ll_question.setClickable(false);
            tv_question.setVisibility(View.GONE);
            img_question.setVisibility(View.VISIBLE);
        }
    }

    private void showOpenEcwDialog() {
        if (openEcwDialog == null) {
            openEcwDialog = mContext.createDialog("温馨提示", mContext.getString(R.string.ecw_dialog_open_ecw_account_instruction_auth),
                    mContext.getString(R.string.ecw_dialog_not_open_ecw_account_now), mContext.getString(R.string.ecw_dialog_open_ecw_account_now),
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (openEcwDialog.isShowing()) {
                                openEcwDialog.dismiss();
                                openEcwDialog = null;
                            }
                        }
                    },
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (openEcwDialog.isShowing()) {
                                openEcwDialog.dismiss();
                                openEcwDialog = null;
                            }
                            if (CommonUtil.isNullOrEmpty(openEscrowUrl)) {
                                Intent openEcwIntent = new Intent();
                                openEcwIntent.setAction(BroadCastImpl.ACTION_OPEN_ECW_ACCOUNT);
                                mContext.sendBroadcast(openEcwIntent);
                            } else {
                                Intent intent = new Intent(mContext, MainWebActivity.class);
                                intent.putExtra("web_url", openEscrowUrl);
                                intent.putExtra("need_header", 0);
                                intent.putExtra("title", "开通存管");
                                mContext.startActivity(intent);
                            }
                            dismiss();
                        }
                    }
            );
            Button btn = (Button) openEcwDialog.findViewById(R.id.dialog_message_bt_absolute);
            Button t = (Button) openEcwDialog.findViewById(R.id.dialog_message_bt_negative);
            btn.setTextColor(mContext.getResources().getColor(R.color.text_default));
            t.setTextColor(mContext.getResources().getColor(R.color.color_finance_child_yellow));
        }
        if (!openEcwDialog.isShowing()) {
            openEcwDialog.show();
        }
    }

}
