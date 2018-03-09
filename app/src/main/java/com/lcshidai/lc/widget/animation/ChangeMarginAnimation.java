package com.lcshidai.lc.widget.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout.LayoutParams;


public class ChangeMarginAnimation extends Animation {

    private final int startMargin;

    private final int endMargin;

    private final View v;

    private final LayoutParams lp;

    public ChangeMarginAnimation(View v, int endHeight) {
        this.v = v;
        lp = (LayoutParams) v.getLayoutParams();
        this.startMargin = lp.topMargin;
        this.endMargin = endHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        LayoutParams lp = (LayoutParams) v.getLayoutParams();
        int curMargin = (int) (startMargin + ((endMargin - startMargin) * interpolatedTime));
//        LogUtil.d("curMargin:" + curMargin);
        lp.topMargin = curMargin;
        v.requestLayout();
        super.applyTransformation(interpolatedTime, t);
    }

}
