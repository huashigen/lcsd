package com.lcshidai.lc.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {
	private int mDuration = 100;

	public FixedSpeedScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
		// TODO Auto-generated constructor stub
	}

	public FixedSpeedScroller(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		// Ignore received duration, use fixed one instead
		super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
		// Ignore received duration, use fixed one instead
		super.startScroll(startX, startY, dx, dy, mDuration);
    }

	public int getmDuration() {
		return mDuration;
	}

	public void setmDuration(int time) {
		this.mDuration = time;
	}

}
