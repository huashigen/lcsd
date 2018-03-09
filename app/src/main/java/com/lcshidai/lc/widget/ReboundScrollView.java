package com.lcshidai.lc.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 弹性ScrollView 实现下拉弹回和上拉弹回
 * 下拉放大顶部视图
 * @author Allin
 * 2016年5月10日
 */

public class ReboundScrollView extends ScrollView {
	// 保存ScrollView中子控件
	private View contentView = null;
	// 用来保存唯一子控件的布局信息
	private Rect contentViewRect = new Rect();
	// 移动开始时候的Y坐标
	private float startY;
	// 线性阻尼 缓冲过量移动的移动速度
	private static float MOVE_FACTOR = 0.5f;
	//过度位移恢复的动画持续时间
	private static long DURATION_MILLIS = 300;
	
	private boolean mFirstRender = true;
	//headView
	private View mHeadView;
	private Rect mHeadRect = new Rect();
	//是否缩放状态
	private boolean mZoom;

	public ReboundScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ReboundScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ReboundScrollView(Context context) {
		super(context);
	}

	/**
	 * 在布局完成后得到ScrollView的唯一子View，并存在contentView中
	 */
	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			contentView = getChildAt(0);
		}
	}

	/**
	 * 在事件分发其中处理触摸事件
	 * 根据android中事件分发的机制判断，个人觉得把事件处理逻辑写在分发器中比写在onTouchEvent中好些，
	 * 因为在其子View没有接收到该触摸事件之前自己就处理了触摸事件。
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (contentView != null)
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = ev.getY();
				break;
			case MotionEvent.ACTION_UP:
				if (isNeedAnimation()) {
					playAnimation();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float nowY = ev.getY();
				int detailY = (int) (nowY - startY);
				if (isNeedMove(detailY)) {
					// 超出屏幕后滚动的View移动的距离为滑动位移的MOVE_FACTOR倍
					detailY = (int) (detailY * MOVE_FACTOR);
					if(detailY > 0){
						mZoom = true;
						mHeadView.getLayoutParams().height = mHeadRect.height() + detailY / 2;
						mHeadView.requestLayout();
					}else{
						mZoom = false;
						//重新布局子View，并且只修改顶部与底部的位置
						contentView.layout(contentViewRect.left, contentViewRect.top + detailY, contentViewRect.right,
								contentViewRect.bottom + detailY);
					}
				}
				break;
			default:
				break;
			}

		return super.dispatchTouchEvent(ev);
	}

	
	/**
	 * 在布局都完成后contentView的布局也就确定了
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		//在未超出移动前contentView的布局没有发生变化 即全局中contentView的布局不变
		if(contentView != null){
			if(mFirstRender){
				mFirstRender = false;
				contentViewRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
				mHeadView = ((ViewGroup)contentView).getChildAt(0);
				mHeadRect.set(mHeadView.getLeft(), mHeadView.getTop(), mHeadView.getRight(), mHeadView.getBottom());
			}
		}
	}

	/**
	 * 判断是否需要超出屏幕移动
	 * 
	 * 通过三个量来判断是否需要移动及如何移动，这三个量分别为scrollY、
	 * contentViewHeight和scrollViewHeight外加辅助detailY手指移动的位移。分三种情况：
	 * 
	 * 其中两种均为contentViewHeight>scrollViewHeight：
	 * 1、当contentView的顶部处于ScrollView顶部且向下滑动手指时候需要超出屏幕移动条件为：
	 * scrollY == 0 && detailY > 0, 如图：
	 * |-----scrollViewHeight-----|
	 * |----------contentViewHeight--------|
	 *  -----detailY---->
	 *  
	 * 2、当contentView的底部处于ScrollView底部且向上滑动手指时候需要超出屏幕移动条件为：
	 * scrollY + scrollViewHeight >= contentViewHeight && detailY < 0, 如图：
	 * |--scrollY--|
	 *             |-----scrollViewHeight-----|
	 * |-----------contentViewHeight----------|
	 *                       <-----detailY----
	 *                       
	 * 另外一种情况是contentViewHeight<=scrollViewHeight上下滑动都需要做超出屏幕移动
	 * 3、当contentView的本身处于ScrollView内部时候无论向上或向下滑动手指时候都需要超出屏幕移动条件为：
	 * contentViewHeight <= scrollViewHeight，如图：
	 * |-----scrollViewHeight-----|
	 * |---contentViewHeight---|
	 *  <-----detailY---->
	 * 
	 * @param detailY
	 *            手指移动的位移（向下或向右滑动为正方向）
	 * @return 是否需要移动
	 */
	private boolean isNeedMove(int detailY) {
		int scrollY = getScrollY();
		int contentViewHeight = contentView.getHeight();
		int scrollViewHeight = getHeight();

		return (scrollY == 0 && detailY > 0)|| (scrollY + scrollViewHeight >= contentViewHeight && detailY < 0)
				|| (contentViewHeight <= scrollViewHeight);
	}

	/**
	 * 播放contentView复位的动画并将contentView复位
	 * 动画可以自定义
	 * 动画执行时间随拉伸的距离增加而减少
	 */
	private void playAnimation() {
		if(mZoom){
			final float height = mHeadView.getHeight();
			float scale = (mHeadRect.height() * 1.0f) / (mHeadView.getHeight() * 1.0f);
			float factor = 1 - scale * 0.5f;
			ObjectAnimator ani = ObjectAnimator.ofFloat(mHeadView, "translationY", 1.0f, scale);
			ani.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float value = (Float) animation.getAnimatedValue();
					mHeadView.getLayoutParams().height = (int) (height * value);
					mHeadView.requestLayout();
				}
			});
			ani.setDuration((long) (DURATION_MILLIS * factor));
			ani.start();
			return;
		}
		int contentViewTop = contentView.getTop();
		int scrollViewHeight = this.getHeight();
		float factor = 1-Math.abs(contentViewTop - contentViewRect.top)/(scrollViewHeight*1.0f);
		TranslateAnimation ta = new TranslateAnimation(0,0,contentViewTop,contentViewRect.top);
		ta.setDuration((long) (DURATION_MILLIS*factor));
		contentView.startAnimation(ta);
		contentView.layout(contentViewRect.left, contentViewRect.top, contentViewRect.right,contentViewRect.bottom);
	}

	/**
	 * 判断是否需要动画效果
	 * @return
	 */
	private boolean isNeedAnimation() {
		return contentView.getBottom() != contentViewRect.bottom || mHeadView.getBottom() != mHeadRect.bottom;
	}

}
