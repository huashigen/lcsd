package com.lcshidai.lc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HomeViewPager extends ParentViewPager{
    
    private boolean scrollble = true;
  
    public HomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
  
    public HomeViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    
    
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
    	if(!scrollble){
    		return false;
    	}
		return super.onTouchEvent(arg0);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
    	if(!scrollble){
    		return false;
    	}
    	return super.onInterceptTouchEvent(arg0);
    }
  
    public void setScrollble(boolean scrollble) {  
        this.scrollble = scrollble;  
    }
  
}