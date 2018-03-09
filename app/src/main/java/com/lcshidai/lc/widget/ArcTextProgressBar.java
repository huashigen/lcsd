package com.lcshidai.lc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.lcshidai.lc.R;

/**
 * Created by Randy on 2016/5/10.
 */
public class ArcTextProgressBar extends View {
    private int diameter = 600;                                                 // 圆弧直径

    private int mStokeWidth = 40;                                               // 进度条宽度
    private int mTextBgCircleDiameter = 20;                                     // 进度条圆形背景上的致敬
    private String mTextStringPrefix = "已完成";                                 // 进度提示文字前缀
    private int mMinProgress = 0;
    private int mCurrentProgress = 0;                                          // 进度条当前进度
    private int mMaxProgress = 100;
    private String mTextStringSuffix = "%";                                     // 进度提示文字后缀

    private int mTotalArcColor = Color.GRAY;
    private int mCurrentArcColor = Color.CYAN;
    private int mCircleTextBgColor = Color.CYAN;
    private int mCircleTextColor = Color.WHITE;

    private int mCircleTextSize = dipToPx(8);

    private int mStartAngle = 180;                                              // 进度条开始绘制的弧度
    private int mSweepAngle = 90;                                               // 进度条滑过的弧度
    private int mTotalAngle = 180;                                              // 进度条总共的弧度

    private Point mPointTextBgCircleCenter = null;                              // 文字背景圆的圆心坐标
    private RectF mArcRect = null;                                              // 圆弧背景矩形
    //    private RectF mBgRect = null;                                               // 整个控件背景矩形（测试）
    private Paint mPaintAllArc = null;                                          // 全弧画笔
    private Paint mPaintCurrentArc = null;                                      // 当前进度弧画笔
    private Paint mPaintCircleStroke = null;                                 //
    private Paint mPaintCircleBg = null;                                        // 进度圆画笔
    //    private Paint mPaintBg = null;                                              // 整个控件背景矩形画笔（测试）
//    private Paint mPaintArcRect = null;                                         // 圆弧背景矩形画笔（测试）
    private StaticLayout mStaticLayout = null;
    private TextPaint mPaintCircleText = null;

    public ArcTextProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public ArcTextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initView();
        initConfig(context, attrs);
    }

    public ArcTextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initConfig(context, attrs);
    }

    private void initConfig(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcTextProgressBar);
        mStokeWidth = typedArray.getInteger(R.styleable.ArcTextProgressBar_arc_text_progress_stroke_width, 40);
        mTextBgCircleDiameter = typedArray.getInteger(R.styleable.ArcTextProgressBar_arc_text_progress_text_bg_circle_diameter, 20);
        mTextStringPrefix = typedArray.getString(R.styleable.ArcTextProgressBar_arc_text_progress_text_string_prefix);
        mTextStringSuffix = typedArray.getString(R.styleable.ArcTextProgressBar_arc_text_progress_text_string_suffix);
        mMinProgress = typedArray.getInteger(R.styleable.ArcTextProgressBar_arc_text_progress_min_progress, 0);
        mMaxProgress = typedArray.getInteger(R.styleable.ArcTextProgressBar_arc_text_progress_max_progress, 100);
        mCurrentProgress = typedArray.getInteger(R.styleable.ArcTextProgressBar_arc_text_progress_current_progress, 0);
        mTotalArcColor = typedArray.getColor(R.styleable.ArcTextProgressBar_arc_text_progress_total_arc_color, Color.GRAY);
        mCurrentArcColor = typedArray.getColor(R.styleable.ArcTextProgressBar_arc_text_progress_current_arc_color, Color.CYAN);
        mCircleTextBgColor = typedArray.getColor(R.styleable.ArcTextProgressBar_arc_text_progress_circle_text_bg_color, Color.CYAN);
        mCircleTextColor = typedArray.getColor(R.styleable.ArcTextProgressBar_arc_text_progress_circle_text_color, Color.WHITE);
        mCircleTextSize = typedArray.getInteger(R.styleable.ArcTextProgressBar_arc_text_progress_circle_text_size, dipToPx(8));

        // 进行初始化设置
        if (null == mTextStringPrefix) {
            mTextStringPrefix = "已完成";
        }
        if (null == mTextStringSuffix) {
            mTextStringSuffix = "%";
        }
        setStokeWidth(mStokeWidth);
        setTextBgCircleDiameter(mTextBgCircleDiameter);
        setTextStringPrefix(mTextStringPrefix);
        setTextStringSuffix(mTextStringSuffix);
        setMinProgress(mMinProgress);
        setMaxProgress(mMaxProgress);
        setCurrentProgress(mCurrentProgress);
        setTotalArcColor(mTotalArcColor);
        setCurrentArcColor(mCurrentArcColor);
        setCircleTextBgColor(mCircleTextBgColor);
        setCircleTextColor(mCircleTextColor);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 2 * Math.max(mTextBgCircleDiameter, mStaticLayout.getWidth()) + diameter;
        int height = 2 * Math.max(mTextBgCircleDiameter, mStaticLayout.getWidth()) + diameter / 2;
        setMeasuredDimension(width, height);
    }


    private void initView() {
        diameter = getScreenWidth() * 8 / 10 - getPaddingLeft() - getPaddingRight();
        // 绘制文字
        mPaintCircleText = new TextPaint();
        mPaintCircleText.setColor(mCircleTextColor);
        mPaintCircleText.setAntiAlias(true);
        mPaintCircleText.setTextSize(mCircleTextSize);
        float startChangeLinePos = mPaintCircleText.measureText(mTextStringPrefix);
        mStaticLayout = new StaticLayout(mTextStringPrefix + mCurrentProgress + mTextStringSuffix,
                mPaintCircleText, (int) startChangeLinePos, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);

        // 计算当前滑过的弧度
        mSweepAngle = (int) (mCurrentProgress * 1.0 / 100 * mTotalAngle);

        // 绘制弧形的Rect
        mArcRect = new RectF();
        mArcRect.left = Math.max(mTextBgCircleDiameter, mStaticLayout.getWidth());
        mArcRect.top = Math.max(mTextBgCircleDiameter, mStaticLayout.getWidth());
        mArcRect.right = Math.max(mTextBgCircleDiameter, mStaticLayout.getWidth()) + diameter;
        mArcRect.bottom = Math.max(mTextBgCircleDiameter, mStaticLayout.getWidth()) + diameter;

        // 背景Rect
//        mBgRect = new RectF();
//        mBgRect.left = 0;
//        mBgRect.top = 0;
//        mBgRect.right = 2 * Math.max(mTextBgCircleDiameter, mStaticLayout.getWidth()) + diameter;
//        mBgRect.bottom = 2 * Math.max(mTextBgCircleDiameter, mStaticLayout.getWidth()) + diameter;
//
//        mPaintBg = new Paint();
//        mPaintBg.setColor(Color.TRANSPARENT);

//        mPaintArcRect = new Paint();
//        mPaintArcRect.setColor(Color.CYAN);

        // 绘制全部进度圆弧
        mPaintAllArc = new Paint();
        mPaintAllArc.setAntiAlias(true);
        mPaintAllArc.setStyle(Paint.Style.STROKE);
        mPaintAllArc.setStrokeWidth(mStokeWidth);
        mPaintAllArc.setColor(mTotalArcColor);
        mPaintAllArc.setStrokeCap(Paint.Cap.ROUND);

        // 当前进度的弧形
        mPaintCurrentArc = new Paint();
        mPaintCurrentArc.setAntiAlias(true);
        mPaintCurrentArc.setStyle(Paint.Style.STROKE);
        mPaintCurrentArc.setStrokeCap(Paint.Cap.ROUND);
        mPaintCurrentArc.setStrokeWidth(mStokeWidth);
        mPaintCurrentArc.setColor(mCurrentArcColor);

        // 绘制当前文字圆形描边
        mPaintCircleStroke = new Paint();
        mPaintCircleStroke.setAntiAlias(true);
        mPaintCircleStroke.setStrokeWidth(1);
        mPaintCircleStroke.setStyle(Paint.Style.STROKE);
        mPaintCircleStroke.setColor(Color.WHITE);

        // 绘制当前文字圆形背景
        mPaintCircleBg = new Paint();
        mPaintCircleBg.setAntiAlias(true);
        mPaintCircleBg.setStyle(Paint.Style.FILL);
        mPaintCircleBg.setColor(mCircleTextBgColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 整个控件矩形背景
//        canvas.drawRect(mBgRect, mPaintBg);
        // 圆弧矩形背景
//        canvas.drawRect(mArcRect, mPaintArcRect);
        // 整个弧
        canvas.drawArc(mArcRect, mStartAngle, mTotalAngle, false, mPaintAllArc);
        // 当前进度
        canvas.drawArc(mArcRect, mStartAngle, mSweepAngle, false, mPaintCurrentArc);
        // 得到文字背景圆的圆心
        mPointTextBgCircleCenter = calculateTextBgCircleCenter();

        if (mCurrentProgress == 0 || mCurrentProgress == 100) {
            canvas.drawCircle(mPointTextBgCircleCenter.x, mPointTextBgCircleCenter.y - mStaticLayout.getWidth() / 2,
                    ((Math.max(mStokeWidth, mTextBgCircleDiameter) + mStaticLayout.getWidth()) / 2), mPaintCircleStroke);
            canvas.drawCircle(mPointTextBgCircleCenter.x, mPointTextBgCircleCenter.y - mStaticLayout.getWidth() / 2,
                    ((Math.max(mStokeWidth, mTextBgCircleDiameter) + mStaticLayout.getWidth()) / 2), mPaintCircleBg);
            //开始绘制文字的位置
            canvas.translate(mPointTextBgCircleCenter.x - mStaticLayout.getWidth() / 2,
                    mPointTextBgCircleCenter.y - mStaticLayout.getHeight());
        } else {
            canvas.drawCircle(mPointTextBgCircleCenter.x, mPointTextBgCircleCenter.y,
                    ((Math.max(mStokeWidth, mTextBgCircleDiameter) + mStaticLayout.getWidth()) / 2), mPaintCircleStroke);
            canvas.drawCircle(mPointTextBgCircleCenter.x, mPointTextBgCircleCenter.y,
                    ((Math.max(mStokeWidth, mTextBgCircleDiameter) + mStaticLayout.getWidth()) / 2), mPaintCircleBg);
            //开始绘制文字的位置
            canvas.translate(mPointTextBgCircleCenter.x - mStaticLayout.getWidth() / 2,
                    mPointTextBgCircleCenter.y - mStaticLayout.getHeight() / 2);
        }

        mStaticLayout.draw(canvas);
    }

    /**
     * 关键点：计算文字背景圆的圆心
     *
     * @return the textBgCircleCenter
     */
    private Point calculateTextBgCircleCenter() {
        // 得到圆环中心圆对应的半径：  外半径 - 内半径 / 2
        float arcRadius = mArcRect.width() / 2;
        //圆心
        Point point = new Point();
        point.x = (int) (mArcRect.width() / 2 + mArcRect.left - arcRadius * Math.cos(mSweepAngle * 3.14 / 180));
        point.y = (int) (mArcRect.height() / 2 + mArcRect.top - arcRadius * Math.sin(mSweepAngle * 3.14 / 180));

        return point;
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 设置progressBar宽度
     *
     * @param stokeWidth 宽度
     */
    public void setStokeWidth(int stokeWidth) {
        this.mStokeWidth = stokeWidth;
        mPaintAllArc.setStrokeWidth(mStokeWidth);
        mPaintCurrentArc.setStrokeWidth(mStokeWidth);
        invalidate();
    }

    /**
     * 设置progressBar进度提示圆形直径
     *
     * @param textBgCircleDiameter 直径
     **/
    public void setTextBgCircleDiameter(int textBgCircleDiameter) {
        this.mTextBgCircleDiameter = textBgCircleDiameter;
        // todo 根据直径大小来设置里面文字的大小
        invalidate();
    }

    public void setTextStringPrefix(String textStringPrefix) {
        this.mTextStringPrefix = textStringPrefix;
        invalidate();
    }

    public void setMinProgress(int minProgress) {
        this.mMinProgress = minProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        if (currentProgress < mMinProgress) {
            currentProgress = mCurrentProgress;
        } else if (currentProgress > mMaxProgress) {
            currentProgress = mMaxProgress;
        }
        this.mCurrentProgress = currentProgress;
        mSweepAngle = (int) (mCurrentProgress * 1.0 / 100 * mTotalAngle);
        float startChangeLinePos = mPaintCircleText.measureText(mTextStringPrefix);
        mStaticLayout = new StaticLayout(mTextStringPrefix + mCurrentProgress + mTextStringSuffix,
                mPaintCircleText, (int) startChangeLinePos, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);
        // todo 设置动画
        invalidate();
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setTextStringSuffix(String textStringSuffix) {
        this.mTextStringSuffix = textStringSuffix;
        invalidate();
    }

    public void setTotalArcColor(int mTotalArcColor) {
        this.mTotalArcColor = mTotalArcColor;
        mPaintAllArc.setColor(mTotalArcColor);
        invalidate();
    }

    public void setCurrentArcColor(int currentArcColor) {
        this.mCurrentArcColor = currentArcColor;
        mPaintCurrentArc.setColor(mCurrentArcColor);
        invalidate();
    }

    public void setCircleTextBgColor(int circleTextBgColor) {
        this.mCircleTextBgColor = circleTextBgColor;
        mPaintCircleBg.setColor(mCircleTextBgColor);
        invalidate();
    }

    public void setCircleTextColor(int circleTextColor) {
        this.mCircleTextColor = circleTextColor;
        mPaintCircleText.setColor(mCircleTextColor);
        invalidate();
    }

    public void setCircleTextSize(int circleTextSize) {
        this.mCircleTextSize = dipToPx(circleTextSize);
        mPaintCircleText.setTextSize(mCircleTextSize);
        invalidate();
    }
}
