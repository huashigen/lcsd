package com.lcshidai.lc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by huashigen on 2018-02-28.
 */

public class ScrollWebview extends WebView {
    private static final String TAG = "ScrollWebview";

    public ScrollWebview(Context context) {
        super(context);
    }

    public ScrollWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollWebview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = super.onTouchEvent(event);
        return true;
    }
}
