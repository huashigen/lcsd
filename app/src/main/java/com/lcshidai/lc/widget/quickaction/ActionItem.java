package com.lcshidai.lc.widget.quickaction;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Action item, displayed as menu with icon and text.
 * 
 * @author Lorensius. W. L. T
 *
 */
public class ActionItem {
	private Drawable icon;
	private Bitmap thumb;
	private String title;
	private boolean selected;
	private View.OnClickListener listener;
	private Drawable mRightDraw;
	public View.OnClickListener getListener() {
		return listener;
	}

	public ActionItem setListener(View.OnClickListener listener) {
		this.listener = listener;
		return this;
	}

	/**
	 * Constructor
	 */
	public ActionItem() {}
	
	/**
	 * Constructor
	 * 
	 * @param icon {@link Drawable} action icon
	 */
	public ActionItem(Drawable icon) {
		this.icon = icon;
	}
	
	/**
	 * Set action title
	 * 
	 * @param title action title
	 */
	public ActionItem setTitle(String title) {
		this.title = title;
		return this;
	}
	
	/**
	 * Get action title
	 * 
	 * @return action title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Set action icon
	 * 
	 * @param icon {@link Drawable} action icon
	 */
	public ActionItem setIcon(Drawable icon) {
		this.icon = icon;
		return this;
	}
	
	/**
	 * Get action icon
	 * @return  {@link Drawable} action icon
	 */
	public Drawable getIcon() {
		return this.icon;
	}
	
	/**
	 * Set selected flag;
	 * 
	 * @param selected Flag to indicate the item is selected
	 */
	public ActionItem setSelected(boolean selected) {
		this.selected = selected;
		return this;
	}
	
	/**
	 * Check if item is selected
	 * 
	 * @return true or false
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * Set thumb
	 * 
	 * @param thumb Thumb image
	 */
	public ActionItem setThumb(Bitmap thumb) {
		this.thumb = thumb;
		return this;
	}
	
	/**
	 * Get thumb image
	 * 
	 * @return Thumb image
	 */
	public Bitmap getThumb() {
		return this.thumb;
	}
	
	public ActionItem setRight(Drawable icon){
		mRightDraw=icon;
		return this;
	}
	
	public Drawable getRightIcon(){
		return mRightDraw;
	}
}