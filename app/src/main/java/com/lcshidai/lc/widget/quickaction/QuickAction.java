package com.lcshidai.lc.widget.quickaction;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcshidai.lc.R;

/**
 * Popup window, shows action list as icon and text like the one in Gallery3D app. 
 * 
 * @author Lorensius. W. T
 */
public class QuickAction extends PopupWindows {
	private View mRootView;
	private ImageView mArrowUp;
	private ImageView mArrowDown;
	private LayoutInflater inflater;
	private ViewGroup mTrack;
//	private ScrollView mScroller;
	private OnActionItemClickListener mListener;
	
	protected static final int ANIM_GROW_FROM_LEFT = 1;
	protected static final int ANIM_GROW_FROM_RIGHT = 2;
	protected static final int ANIM_GROW_FROM_CENTER = 3;
	protected static final int ANIM_REFLECT = 4;
	protected static final int ANIM_AUTO = 5;
	
	private int mChildPos;
	private int animStyle;
	/**
	 * Constructor.
	 * 
	 * @param context Context
	 */
	public QuickAction(Context context) {
		super(context);
		
		inflater 		= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		setRootViewId(R.layout.popup);
		animStyle		= ANIM_AUTO;
		mChildPos		= 0;
	}

	/**
	 * Set root view.
	 * 
	 * @param id Layout resource id
	 */
	public void setRootViewId(int id) {
		mRootView	= inflater.inflate(id, null);
		mTrack 		= (ViewGroup) mRootView.findViewById(R.id.tracks);

		mArrowDown 	= (ImageView) mRootView.findViewById(R.id.arrow_down);
		mArrowUp 	= (ImageView) mRootView.findViewById(R.id.arrow_up);

//		mScroller	= (ScrollView) mRootView.findViewById(R.id.scroller);
		
		setContentView(mRootView);
	}
	
	public void dismissView(){
		mRootView.findViewById(R.id.tv_ex_info_pp2).setVisibility(View.GONE);
	}
	
	
	public TextView getContentView(){
		return (TextView) mRootView.findViewById(R.id.tv_ex_info_pp);
	}
	
	public TextView getContentViewBottom(){
		return (TextView) mRootView.findViewById(R.id.tv_ex_info_pp2);
	}
	
	/**
	 * Set animation style
	 * 
	 * @param animStyle animation style, default is set to ANIM_AUTO
	 */
	public void setAnimStyle(int animStyle) {
		this.animStyle = animStyle;
	}
	
	/**
	 * Set listener for action item clicked.
	 * 
	 * @param listener Listener
	 */
	public void setOnActionItemClickListener(OnActionItemClickListener listener) {
		mListener = listener;
	}
	
	/**
	 * Add action item
	 * 
	 * @param action  {@link ActionItem}
	 */
	public QuickAction addActionItem(ActionItem action) {
		
		String title 	= action.getTitle();
		Drawable icon 	= action.getIcon();
		
		View container	= inflater.inflate(R.layout.action_item, null);
		
		if(android.os.Build.VERSION.SDK_INT <= 10){
			container.setBackgroundColor(Color.TRANSPARENT);
			container.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						v.setBackgroundResource(R.drawable.action_item_selected);
						break;
					case MotionEvent.ACTION_UP:
						v.setBackgroundColor(Color.TRANSPARENT);
						break;
					case MotionEvent.ACTION_CANCEL:
						v.setBackgroundColor(Color.TRANSPARENT);
						break;
					}
					return false;
				}
			});
		}else{
			container.findViewById(R.id.view).setAlpha(0.2f);
		}
		
		
		ImageView img 	= (ImageView) container.findViewById(R.id.iv_icon);
		TextView text 	= (TextView) container.findViewById(R.id.tv_top_bar_title);
		ImageView imgRight 	= (ImageView) container.findViewById(R.id.iv_right);
		
		if (icon != null) 
			img.setImageDrawable(icon);
		else
			img.setVisibility(View.GONE);
		
		if (title != null)
			text.setText(title);
		else
			text.setVisibility(View.GONE);
//		if(action.getRightIcon()!=null){
//			imgRight.setBackgroundDrawable(action.getRightIcon());
//			imgRight.setVisibility(View.VISIBLE);
//		}else{
//		}
		imgRight.setVisibility(View.GONE);
		final int pos =  mChildPos;
		
		container.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) mListener.onItemClick(pos);
				dismiss();
			}
		});
		OnClickListener listener = action.getListener();
		if(listener != null) {
			container.setOnClickListener(listener);
		}
		container.setFocusable(true);
		container.setClickable(true);
			 
		mTrack.addView(container, mChildPos);
		
		mChildPos++;
		return this;
	}
	
	/**
	 * Show popup window. Popup is automatically positioned, on top or bottom of anchor view.
	 * 
	 */
	public void show (View anchor,boolean isRight) {
		preShow();
		
		int xPos, yPos;
		
		int[] location 		= new int[2];
	
		anchor.getLocationOnScreen(location);

		Rect anchorRect 	= new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] 
		                	+ anchor.getHeight());

		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
		int rootHeight 		= mRootView.getMeasuredHeight();
		int rootWidth		= mRootView.getMeasuredWidth();
		
		int screenWidth 	= mWindowManager.getDefaultDisplay().getWidth();
		int screenHeight	= mWindowManager.getDefaultDisplay().getHeight();
		
		//automatically get X coord of popup (top left)
		if ((anchorRect.left + rootWidth) > screenWidth) {
			xPos = anchorRect.left - (rootWidth-anchor.getWidth());
		} else {
			if (anchor.getWidth() > rootWidth) {
				xPos = anchorRect.centerX() - (rootWidth/2);
			} else {
				xPos = anchorRect.left;
			}
		}
		
		int dyTop			= anchorRect.top;
		int dyBottom		= screenHeight - anchorRect.bottom;

		boolean onTop=true;//		= (dyTop > dyBottom) ? true : false;

		if (onTop) {
			if (rootHeight > dyTop) {
				yPos 			= 15;
				LayoutParams l 	= mTrack.getLayoutParams();
				l.height		= dyTop - anchor.getHeight();
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;
			
			if (rootHeight > dyBottom) { 
				LayoutParams l 	= mTrack.getLayoutParams();
				l.height		= dyBottom;
			}
		}
		
		if(isRight){
			showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), anchorRect.left);
		}else{
			showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), screenWidth*21/64);
		}
		
		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
		
		if(isRight){
			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, anchorRect.left, yPos-120);
		}else{
			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY,anchorRect.left, yPos-20);
		}
	}
	
	
	/**
	 * Show popup window. Popup is automatically positioned, on top or bottom of anchor view.
	 * 
	 */
	public void showTOP(View anchor) {
		preShow();
		
		int xPos, yPos;
		
		int[] location 		= new int[2];
	
		anchor.getLocationOnScreen(location);

		Rect anchorRect 	= new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] 
		                	+ anchor.getHeight());

		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
		int rootHeight 		= mRootView.getMeasuredHeight();
		int rootWidth		= mRootView.getMeasuredWidth();
		
		int screenWidth 	= mWindowManager.getDefaultDisplay().getWidth();
		
		//automatically get X coord of popup (top left)
		if ((anchorRect.left + rootWidth) > screenWidth) {
			xPos = screenWidth-rootWidth;
		} else {
			if (anchor.getWidth() > rootWidth) {
				xPos = anchorRect.centerX() - (rootWidth/2);
			} else {
				xPos = anchorRect.left;
			}
		}
		
		int dyTop			= anchorRect.top;
		if (rootHeight > dyTop) {
			yPos 			= 15;
			LayoutParams l 	= mTrack.getLayoutParams();
			l.height		= dyTop - anchor.getHeight();
		} else {
			yPos = anchorRect.top - rootHeight;
		}
		
		showArrow( R.id.arrow_down, anchorRect.left+anchor.getWidth()/2-xPos);
		setAnimationStyle(screenWidth, anchorRect.centerX(), true);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos-10);
	}
	
	/**
	 * Set animation style
	 * 
	 * @param screenWidth screen width
	 * @param requestedX distance from left edge
	 * @param onTop flag to indicate where the popup should be displayed. Set TRUE if displayed on top of anchor view
	 * 		  and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
		int arrowPos = requestedX - mArrowUp.getMeasuredWidth()/2;

		switch (animStyle) {
		case ANIM_GROW_FROM_LEFT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			break;
					
		case ANIM_GROW_FROM_RIGHT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			break;
					
		case ANIM_GROW_FROM_CENTER:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
		break;
			
		case ANIM_REFLECT:
			mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
		break;
		
		case ANIM_AUTO:
			if (arrowPos <= screenWidth/4) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			} else if (arrowPos > screenWidth/4 && arrowPos < 3 * (screenWidth/4)) {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
			} else {
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			}
					
			break;
		}
	}
	
	/**
	 * Show arrow
	 * 
	 * @param whichArrow arrow type resource id
	 * @param requestedX distance from left screen
	 */
	private void showArrow(int whichArrow, int requestedX) {
        final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;
        final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;

        final int arrowWidth = mArrowDown.getMeasuredWidth();

        showArrow.setVisibility(View.VISIBLE);
        
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams)showArrow.getLayoutParams();
       
        param.leftMargin = requestedX - arrowWidth / 2;
        
        hideArrow.setVisibility(View.INVISIBLE);
    }
	
	/**
	 * Listener for item click
	 *
	 */
	public interface OnActionItemClickListener {
		void onItemClick(int pos);
	}

	public void showBespeakTOP(View anchor) {
		// TODO Auto-generated method stub
		preShow();
		
		int xPos, yPos;
		
		int[] location 		= new int[2];
	
		anchor.getLocationOnScreen(location);

		Rect anchorRect 	= new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] 
		                	+ anchor.getHeight());

		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
		int rootHeight 		= mRootView.getMeasuredHeight();
		int rootWidth		= mRootView.getMeasuredWidth();
		
		int screenWidth 	= mWindowManager.getDefaultDisplay().getWidth();
		
		//automatically get X coord of popup (top left)
		if ((anchorRect.left + rootWidth) > screenWidth) {
			xPos = screenWidth-rootWidth;
		} else {
			if (anchor.getWidth() > rootWidth) {
				xPos = anchorRect.centerX() - (rootWidth/2);
			} else {
				xPos = anchorRect.left;
			}
		}
		
		int dyTop			= anchorRect.top;
		if (rootHeight > dyTop) {
			yPos 			= 15;
			LayoutParams l 	= mTrack.getLayoutParams();
			l.height		= dyTop - anchor.getHeight();
		} else {
			yPos = anchorRect.top - rootHeight;
		}
		
		showArrow( R.id.arrow_down, anchorRect.left+anchor.getWidth()/2-xPos+50);
		setAnimationStyle(screenWidth, anchorRect.centerX(), true);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos-50, yPos-10);
	}
	
}