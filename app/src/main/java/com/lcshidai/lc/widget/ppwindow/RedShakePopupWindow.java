package com.lcshidai.lc.widget.ppwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.lcshidai.lc.R;
import com.lcshidai.lc.widget.gifview.GifMovieView;

public class RedShakePopupWindow extends PopupWindow {

    private View mMenuView;
    GifMovieView mgifView;

    public RedShakePopupWindow(Activity context, final RedShakeBack redBack) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pp_shake, null);
        //设置按钮监听
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mgifView = (GifMovieView) mMenuView.findViewById(R.id.iv_gif);
        mgifView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                redBack.enter();
            }
        });
        mMenuView.findViewById(R.id.iv_close).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMenuView.findViewById(R.id.rl_showquestion).setVisibility(View.VISIBLE);
            }
        });

        mMenuView.findViewById(R.id.view_left).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuView.findViewById(R.id.rl_showquestion).setVisibility(View.GONE);
                RedShakePopupWindow.this.dismiss();
            }
        });

        mMenuView.findViewById(R.id.view_right).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMenuView.findViewById(R.id.rl_showquestion).setVisibility(View.GONE);
            }
        });
    }

    public interface RedShakeBack {
        void enter();
    }
}
