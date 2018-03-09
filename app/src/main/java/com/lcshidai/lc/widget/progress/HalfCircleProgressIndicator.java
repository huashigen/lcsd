package com.lcshidai.lc.widget.progress;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * A custom view which display round style progress indicator.
 *
 * @author Vipul Purohit
 */

public class HalfCircleProgressIndicator extends View {

    private static final int CIRCLE_RADIUS = 180;
//    private static final int CIRCLE_RADIUS = 360;
    // For progress outline
    Paint mProgressOutlinePaint = new Paint();
    RectF mProgressOutlineRectF;
    int mProgressOutlineRadius = 0;

    int mCanvasWidth = 0;
    int mCanvasHeight = 0;

    private int mProgressOutlineRadius_Left = 10;
    private int mProgressOutlineRadius_Rigth = 10;
    private int mProgressOutlineRadius_Top = 10;
    private int mProgressOutlineRadius_Bottom = 10;

    private float mProgressOutlineWidth = 0;

    private int mRequiredRadius = 0;

    // Start end marker
//    private int mProgressStartPosition = 90;
//    private int mProgressEndPosition = 0;

    private int mProgressStartPosition = 180;
    private int mProgressEndPosition = 180;

    private boolean initilise = false;

    private boolean startDrawingFromCenter = false;

    private Style mProgressStyle = Style.STROKE;

    private int mProgressColor = Color.GRAY;
    ProgressHandler ph = new ProgressHandler();

    public HalfCircleProgressIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * Initialize the control. This code is in a separate method so that it can
     * be called from both constructors.
     */
    private void init() {
//        mProgressOutlinePaint = new Paint();
        mProgressOutlineRectF = new RectF();

        mCanvasWidth = getWidth();
        mCanvasHeight = getHeight()*2;

        if (mCanvasHeight > mCanvasWidth) {
            mRequiredRadius = mCanvasWidth;
        } else {
            mRequiredRadius = mCanvasHeight;
        }

        mProgressOutlineRadius_Rigth = mRequiredRadius / 2 - getPaddingRight();
        mProgressOutlineRadius_Left = mRequiredRadius / 2 - getPaddingRight();
        mProgressOutlineRadius_Top = mRequiredRadius / 2 - getPaddingTop();
        mProgressOutlineRadius_Bottom = mRequiredRadius / 2 - getPaddingBottom();

        // Set arc values
        mProgressOutlineRectF.set(mCanvasWidth / 2 - mProgressOutlineRadius_Left + mProgressOutlineWidth/2, mCanvasHeight / 2 - mProgressOutlineRadius_Top + mProgressOutlineWidth/2, mCanvasWidth / 2
                + mProgressOutlineRadius_Rigth - mProgressOutlineWidth/2, mCanvasHeight / 2 + mProgressOutlineRadius_Bottom);

        //颜色
        mProgressOutlinePaint.setColor(mProgressColor);
        //线宽
        mProgressOutlinePaint.setStrokeWidth(mProgressOutlineWidth);
        //消除锯齿
        mProgressOutlinePaint.setAntiAlias(true);
        //笔刷
        mProgressOutlinePaint.setStrokeCap(Paint.Cap.SQUARE);
//	mProgressOutlinePaint.setStrokeJoin(Paint.Join.BEVEL);
//	mProgressOutlinePaint.setStrokeCap(Paint.Join.ROUND);
        //描边
        mProgressOutlinePaint.setStyle(mProgressStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (!initilise) {
            init();
            initilise = true;
        }

        // Draw arc on canvas
        canvas.drawArc(mProgressOutlineRectF, mProgressStartPosition, mProgressEndPosition, startDrawingFromCenter, mProgressOutlinePaint);
        super.onDraw(canvas);
    }

    private void refreshView() {
        invalidate();
        requestLayout();
    }

    /**
     * Set progress of Round Progress Indicator.
     *
     * @param mProgress Progress value must be between 0 to 100
     */
    public void setProgress(int mProgress) {
        if (mProgress >= 0 && mProgress <= 100) {
            mProgressEndPosition = (CIRCLE_RADIUS * mProgress) / 100;

            refreshView();
        } else {
            throw new RuntimeException("Invalid progress value. Progress value must be between 0 to 100");
        }
    }

    public void setProgressMovie(int mProgress) {
        if (mProgress >= 0 && mProgress <= 100) {
//    		this.mProgress=mProgress;
            for (int index = 0; index <= mProgress; index++) {
                ph.sendEmptyMessageDelayed(index, 30 * index);
            }

        } else {
            throw new RuntimeException("Invalid progress value. Progress value must be between 0 to 100");
        }
    }

    /**
     * Set progress style to stroke, fill etc
     *
     * @param mProgressStyle Progress style in Paint.Style format
     */
    public void setProgressStyle(Style mProgressStyle) {
        try {
            startDrawingFromCenter = mProgressStyle != Style.STROKE;
            this.mProgressStyle = mProgressStyle;

            refreshView();
        } catch (Exception e) {
            throw new RuntimeException("Invalid progress style");
        }
    }

    /**
     * Set progress color.
     *
     * @param mProgressColor Progress color
     */
    public void setProgresColor(int mProgressColor) {
        try {
            this.mProgressColor = mProgressColor;

            refreshView();
        } catch (Exception e) {
            throw new RuntimeException("Invalid progress color");
        }
    }

    public void setProgressStrokeWidth(int mProgressWidth) {
        try {
            this.mProgressOutlineWidth = mProgressWidth;
            init();
            refreshView();
        } catch (Exception e) {
            throw new RuntimeException("Invalid progress width value");
        }
    }

    public Paint getPaint() {
        return mProgressOutlinePaint;
    }

    class ProgressHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            try {
                if (msg.what >= 0 && msg.what <= 100)
                    HalfCircleProgressIndicator.this.setProgress(msg.what);
            } catch (Exception e) {
            }
        }

    }
}
