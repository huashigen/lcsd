package com.lcshidai.lc.widget.gifview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.lcshidai.lc.R;

/**
 * This is a View class that wraps Android {@link Movie} object and displays it.
 * You can set GIF as a Movie object or as a resource id from XML or by calling
 * {@link #setMovie(Movie)} or {@link #setMovieResource(int)}.
 * <p>
 * You can pause and resume GIF animation by calling {@link #setPaused(boolean)}.
 * <p>
 * The animation is drawn in the center inside of the measured view bounds.
 * 
 * @author Sergey Bakhtiarov
 */

public class GifMovieView extends View {

	private static final int DEFAULT_MOVIEW_DURATION = 1000;

	private int mMovieResourceId;
	private Movie mMovie;

	private long mMovieStart;
	private int mCurrentAnimationTime = 0;

	/**
	 * Position for drawing animation frames in the center of the view.
	 */
	private float mLeft;
	private float mTop;

	/**
	 * Scaling factor to fit the animation within view bounds.
	 */
	private float mScale;

	/**
	 * Scaled movie frames width and height.
	 */
	private int mMeasuredMovieWidth;
	private int mMeasuredMovieHeight;

	private volatile boolean mPaused = false;
	private boolean mVisible = true;

	public GifMovieView(Context context) {
		this(context, null);
	}

	public GifMovieView(Context context, AttributeSet attrs) {
		this(context, attrs, R.styleable.CustomThemeS_gifMoviewViewStyle);
	}

	public GifMovieView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		setViewAttributes(context, attrs, defStyle);
	}

	int width;
	int height;

	@SuppressLint("NewApi")
	private void setViewAttributes(Context context, AttributeSet attrs,
			int defStyle) {

		/**
		 * Starting from HONEYCOMB have to turn off HW acceleration to draw
		 * Movie on Canvas.
		 */
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		final TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.GifView, defStyle, R.style.Widget_GifMoviewView);

		mMovieResourceId = array.getResourceId(R.styleable.GifView_gif, -1);
		mPaused = array.getBoolean(R.styleable.GifView_paused, false);

		array.recycle();

		if (mMovieResourceId != -1) {
			mMovie = Movie.decodeStream(getResources().openRawResource(
					mMovieResourceId));
		}

		// gif图片宽度，高度
		mViewHeight = mMovie.height();
		mViewWidht = mMovie.width();
	}

	int mViewHeight, mViewWidht;

	public void setMovieResource(int movieResId) {
		this.mMovieResourceId = movieResId;
		mMovie = Movie.decodeStream(getResources().openRawResource(
				mMovieResourceId));
		requestLayout();
	}

	public void setMovie(Movie movie) {
		this.mMovie = movie;
		requestLayout();
	}

	public Movie getMovie() {
		return mMovie;
	}

	public void setMovieTime(int time) {
		mCurrentAnimationTime = time;
		invalidate();
	}

	public void setPaused(boolean paused) {
		this.mPaused = paused;

		/**
		 * Calculate new movie start time, so that it resumes from the same
		 * frame.
		 */
		if (!paused) {
			mMovieStart = android.os.SystemClock.uptimeMillis()
					- mCurrentAnimationTime;
		}

		invalidate();
	}

	public boolean isPaused() {
		return this.mPaused;
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//		if (mMovie != null) {
//			int movieWidth = mMovie.width();
//			int movieHeight = mMovie.height();
//
//			/*
//			 * Calculate horizontal scaling
//			 */
//			float scaleH = 1f;
//			int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);
//
//			if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
//				int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
//				if (movieWidth > maximumWidth) {
//					scaleH = (float) movieWidth / (float) maximumWidth;
//				} else {
//					scaleH = (float) maximumWidth / (float) movieWidth;
//				}
//			}
//
//			/*
//			 * calculate vertical scaling
//			 */
//			float scaleW = 1f;
//			int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);
//
//			if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
//				int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
//				if (movieHeight > maximumHeight) {
//					scaleW = (float) movieHeight / (float) maximumHeight;
//				} else {
//					scaleW = (float) maximumHeight / (float) movieHeight;
//				}
//			}
//
//			/*
//			 * calculate overall scale
//			 */
//			mScale = Math.max(scaleH, scaleW);
//
//			mMeasuredMovieWidth = (int) (movieWidth * scaleW);
//			mMeasuredMovieHeight = (int) (movieHeight * scaleH);
//
//			setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);
//
//		} else {
//			/*
//			 * No movie set, just set minimum available size.
//			 */
//			setMeasuredDimension(getSuggestedMinimumWidth(),
//					getSuggestedMinimumHeight());
//		}
//	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		/*
		 * Calculate left / top for drawing in center
		 */
		mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
		mTop = (getHeight() - mMeasuredMovieHeight) / 2f;

		mVisible = getVisibility() == View.VISIBLE;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mMovie != null) {
			// if (!mPaused) {
			// updateAnimationTime();
			// drawMovieFrame(canvas);
			// invalidateView();
			// } else {
			// drawMovieFrame(canvas);
			// }

			long now = android.os.SystemClock.uptimeMillis();

			if (mMovieStart == 0) { // first time
				mMovieStart = now;
			}
			if (mMovie != null) {

				int dur = mMovie.duration();
				if (dur == 0) {
					dur = 5000;
				}

				int relTime = (int) ((now - mMovieStart) % dur);

				mMovie.setTime(relTime);
				// 根据屏幕大小计算缩放比例
				float saclex = (float) width / (float) mViewWidht;
				float sacley = (float) height / (float) mViewHeight;
				//float sameRate = saclex > sacley ? saclex : sacley;
				canvas.scale(saclex, sacley);
				mMovie.draw(canvas, 0, 0);

				invalidate();
			}
		}
	}

	/**
	 * Invalidates view only if it is visible. <br>
	 * {@link #postInvalidateOnAnimation()} is used for Jelly Bean and higher.
	 * 
	 */
	@SuppressLint("NewApi")
	private void invalidateView() {
		if (mVisible) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				postInvalidateOnAnimation();
			} else {
				invalidate();
			}
		}
	}

	/**
	 * Calculate current animation time
	 */
	private void updateAnimationTime() {
		long now = android.os.SystemClock.uptimeMillis();

		if (mMovieStart == 0) {
			mMovieStart = now;
		}

		int dur = mMovie.duration();

		if (dur == 0) {
			dur = DEFAULT_MOVIEW_DURATION;
		}

		mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
	}

	/**
	 * Draw current GIF frame
	 */
	private void drawMovieFrame(Canvas canvas) {

		mMovie.setTime(mCurrentAnimationTime);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(mScale, mScale);
		mMovie.draw(canvas, mLeft / mScale, mTop / mScale);
		canvas.restore();
	}

	@SuppressLint("NewApi")
	@Override
	public void onScreenStateChanged(int screenState) {
		super.onScreenStateChanged(screenState);
		mVisible = screenState == SCREEN_STATE_ON;
		invalidateView();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		mVisible = visibility == View.VISIBLE;
		invalidateView();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		mVisible = visibility == View.VISIBLE;
		invalidateView();
	}
}
