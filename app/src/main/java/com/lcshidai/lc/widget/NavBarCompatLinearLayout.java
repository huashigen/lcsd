package com.lcshidai.lc.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.lcshidai.lc.utils.DensityUtil;

/**
 * Created by RandyZhang on 16/7/28.
 */
public class NavBarCompatLinearLayout extends LinearLayout {
    private static final String TAG ="CustomShareBoard";
    private Context context;
    public NavBarCompatLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;

    }

    public NavBarCompatLinearLayout(Context context) {
        super(context);
        this.context= context;
    }

    public NavBarCompatLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context= context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(!isOffset()) {
            if (this.getPaddingBottom()!=0) {
                this.setPadding(0, 0, 0, 0);
            }
        } else{
            this.setPadding(0, 0, 0, DensityUtil.getNavigationBarHeight(context));
            invalidate();
        }
        super.onLayout(true, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    public boolean isOffset() {
        return DensityUtil.getDecorViewHeight(context) > DensityUtil.getScreenHeight(context);
    }
}