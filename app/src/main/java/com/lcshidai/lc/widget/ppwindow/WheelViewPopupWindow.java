package com.lcshidai.lc.widget.ppwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.lcshidai.lc.R;
import com.lcshidai.lc.widget.wheelview.WheelView;
import com.lcshidai.lc.widget.wheelview.adapters.ArrayWheelAdapter;

public class WheelViewPopupWindow extends PopupWindow implements OnClickListener {

    public interface OnWVPWClickListener {
        void onSubmitClick(int position);
    }

    private Context mContext;
    private WheelView wheelView;
    private View shadow;
    private View bottomView;
    private Button cancelBT0, submitBT0;
    private OnWVPWClickListener wVPWClickListener;
    private String[] itemArray;
    private LinearLayout ll_button0, ll_button1;
    private TextView cancelBT1, submitBT1;

    //flag:	0-预约	1-选择银行卡
    public WheelViewPopupWindow(Context context, String[] items, int flag) {
        super(context);
        this.itemArray = items;
        this.mContext = context;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View contentView = layoutInflater.inflate(R.layout.layout_pop_wheelview, null);
        this.setContentView(contentView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);

        ll_button0 = (LinearLayout) contentView.findViewById(R.id.pop_wheelview_ll_button0);
        ll_button1 = (LinearLayout) contentView.findViewById(R.id.pop_wheelview_ll_button1);
        bottomView = contentView.findViewById(R.id.pop_wheelview_bottom);
        cancelBT0 = (Button) contentView.findViewById(R.id.pop_wheelview_bt_cancel0);
        submitBT0 = (Button) contentView.findViewById(R.id.pop_wheelview_bt_submit0);
        cancelBT1 = (TextView) contentView.findViewById(R.id.pop_wheelview_bt_cancel1);
        submitBT1 = (TextView) contentView.findViewById(R.id.pop_wheelview_bt_submit1);
        shadow = contentView.findViewById(R.id.pop_wheelview_shadow);
        shadow.setOnClickListener(this);
        cancelBT0.setOnClickListener(this);
        submitBT0.setOnClickListener(this);
        cancelBT1.setOnClickListener(this);
        submitBT1.setOnClickListener(this);

        switch (flag) {
            //预约
            case 0:
                ll_button0.setVisibility(View.VISIBLE);
                break;
            //选择银行卡
            case 1:
                ll_button1.setVisibility(View.VISIBLE);
                break;
        }

        wheelView = (WheelView) contentView.findViewById(R.id.pop_wheelview_wv);
//		wheelView.TEXT_SIZE = DensityUtil.dip2px(mContext, 14);
//		wheelView.TEXT_SIZE_VALUE = DensityUtil.dip2px(mContext, 16);
        wheelView.setCyclic(false);
    }

    public void setOnWVPWClickListener(OnWVPWClickListener listener) {
        this.wVPWClickListener = listener;
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                wheelView.setViewAdapter(new ArrayWheelAdapter<String>(mContext, itemArray));
                wheelView.setCurrentItem(msg.getData().getInt("position"));
//                wheelView.justify();
            }
        }

    };

    /**
     * flag:	1-左边		2-右边
     */
    public void showWheelViewPop(View view, final int position) {
        wheelView.setViewAdapter(null);
        this.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        if (android.os.Build.VERSION.SDK_INT < 12) {
            Message msg = new Message();
            msg.what = 1;
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            msg.setData(bundle);
            mHandler.sendMessageDelayed(msg, 100);
            return;
        }

        wheelView.setViewAdapter(new ArrayWheelAdapter<String>(mContext, itemArray));
        wheelView.setCurrentItem(position);
//        ObjectAnimator showAnim = ObjectAnimator.ofFloat(
//                bottomView, "YFraction", 1, 0);
//        showAnim.setDuration(400);
//        showAnim.addListener(new AnimatorListener() {
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                wheelView.setViewAdapter(new ArrayWheelAdapter<String>(mContext, itemArray));
//                wheelView.setCurrentItem(position);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//            }
//        });
//        showAnim.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_wheelview_shadow:
                if (this.isShowing()) {
                    this.dismiss();
                }
                break;
            case R.id.pop_wheelview_bt_cancel0:
                if (this.isShowing()) {
                    this.dismiss();
                }
                break;
            case R.id.pop_wheelview_bt_submit0:
                if (this.isShowing()) {
                    this.dismiss();
                }
                if (null != wVPWClickListener) {
                    wVPWClickListener.onSubmitClick(wheelView.getCurrentItem());
                }
                break;
            case R.id.pop_wheelview_bt_cancel1:
                if (this.isShowing()) {
                    this.dismiss();
                }
                break;
            case R.id.pop_wheelview_bt_submit1:
                if (this.isShowing()) {
                    this.dismiss();
                }
                if (null != wVPWClickListener) {
                    wVPWClickListener.onSubmitClick(wheelView.getCurrentItem());
                }
                break;
        }

    }
}
