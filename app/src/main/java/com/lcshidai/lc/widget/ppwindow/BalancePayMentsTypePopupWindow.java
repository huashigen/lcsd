package com.lcshidai.lc.widget.ppwindow;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.lcshidai.lc.R;

/**
 * 收支记录查询类型选择popupwindow
 *
 * @author 000853
 */
public class BalancePayMentsTypePopupWindow extends PopupWindow {


    private View mMenuView;

    private ChooseListener mChooseListener;
    private MyChooseListener mListener;

    private ImageView lastIV;

    private RelativeLayout quanbu_rl, chongzhi_rl, touzi_rl, huikuan_rl, tixian_rl, jiangli_rl, zhaiquan_rl, diyongquan_rl;
    private ImageView quanbu_iv, chongzhi_iv, touzi_iv, huikuan_iv, tixian_iv, jiangli_iv, zhaiquan_iv, diyongquan_iv;

    public BalancePayMentsTypePopupWindow(Context context, ChooseListener chooseListener, int chooseMark) {
        this((Activity) context, chooseListener, chooseMark);
    }

    public BalancePayMentsTypePopupWindow(Activity context, ChooseListener chooseListener, int chooseMark) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mChooseListener = chooseListener;
        mMenuView = inflater.inflate(R.layout.layout_balance_payments_type_pp, null);

        quanbu_rl = (RelativeLayout) mMenuView.findViewById(R.id.bp_pp_quanbu_rl);
        chongzhi_rl = (RelativeLayout) mMenuView.findViewById(R.id.bp_pp_chongzhi_rl);
        touzi_rl = (RelativeLayout) mMenuView.findViewById(R.id.bp_pp_touzi_rl);
        huikuan_rl = (RelativeLayout) mMenuView.findViewById(R.id.bp_pp_huikuan_rl);
        tixian_rl = (RelativeLayout) mMenuView.findViewById(R.id.bp_pp_tixian_rl);
        jiangli_rl = (RelativeLayout) mMenuView.findViewById(R.id.bp_pp_jiangli_rl);
        zhaiquan_rl = (RelativeLayout) mMenuView.findViewById(R.id.bp_pp_zhaiquan_rl);
        diyongquan_rl = (RelativeLayout) mMenuView.findViewById(R.id.bp_pp_diyongquan_rl);

        quanbu_iv = (ImageView) mMenuView.findViewById(R.id.bp_pp_quanbu_iv);
        chongzhi_iv = (ImageView) mMenuView.findViewById(R.id.bp_pp_chongzhi_iv);
        touzi_iv = (ImageView) mMenuView.findViewById(R.id.bp_pp_touzi_iv);
        huikuan_iv = (ImageView) mMenuView.findViewById(R.id.bp_pp_huikuan_iv);
        tixian_iv = (ImageView) mMenuView.findViewById(R.id.bp_pp_tixian_iv);
        jiangli_iv = (ImageView) mMenuView.findViewById(R.id.bp_pp_jiangli_iv);
        zhaiquan_iv = (ImageView) mMenuView.findViewById(R.id.bp_pp_zhaiquan_iv);
        diyongquan_iv = (ImageView) mMenuView.findViewById(R.id.bp_pp_diyongquan_iv);

        lastIV = quanbu_iv;

        mListener = new MyChooseListener();

        quanbu_rl.setOnClickListener(mListener);
        chongzhi_rl.setOnClickListener(mListener);
        touzi_rl.setOnClickListener(mListener);
        huikuan_rl.setOnClickListener(mListener);
        tixian_rl.setOnClickListener(mListener);
        jiangli_rl.setOnClickListener(mListener);
        zhaiquan_rl.setOnClickListener(mListener);
        diyongquan_rl.setOnClickListener(mListener);

        //设置按钮监听
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.bp_pp_main_ll).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        mMenuView.findViewById(R.id.bp_pp_gray_iv).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                mChooseListener.onDismiss();
            }
        });
    }

    public void goAnim(View view, int x, int y, int currentT) {
        showAsDropDown(view, x, y);
    }

    class MyChooseListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            lastIV.setVisibility(View.GONE);
            if (arg0 == quanbu_rl) {
                mChooseListener.chooseItem(0);
                quanbu_iv.setVisibility(View.VISIBLE);
                lastIV = quanbu_iv;
            } else if (arg0 == chongzhi_rl) {
                mChooseListener.chooseItem(1);
                chongzhi_iv.setVisibility(View.VISIBLE);
                lastIV = chongzhi_iv;
            } else if (arg0 == touzi_rl) {
                mChooseListener.chooseItem(2);
                touzi_iv.setVisibility(View.VISIBLE);
                lastIV = touzi_iv;
            } else if (arg0 == huikuan_rl) {
                mChooseListener.chooseItem(3);
                huikuan_iv.setVisibility(View.VISIBLE);
                lastIV = huikuan_iv;
            } else if (arg0 == tixian_rl) {
                mChooseListener.chooseItem(4);
                tixian_iv.setVisibility(View.VISIBLE);
                lastIV = tixian_iv;
            } else if (arg0 == jiangli_rl) {
                mChooseListener.chooseItem(5);
                jiangli_iv.setVisibility(View.VISIBLE);
                lastIV = jiangli_iv;
            } else if (arg0 == zhaiquan_rl) {
                mChooseListener.chooseItem(6);
                zhaiquan_iv.setVisibility(View.VISIBLE);
                lastIV = zhaiquan_iv;
            }
            dismiss();
        }

    }


    public interface ChooseListener {
        void chooseItem(int markItem);

        void onDismiss();
    }


}
