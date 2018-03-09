package com.lcshidai.lc.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Allin on 2016/7/28.
 */
public class ArcLineView extends View {
    private Paint mPaint = new Paint();

    public ArcLineView(Context context) {
        super(context);
    }

    public ArcLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArcLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        float cx = width / 2;
        float cy = - width / 2;
        float radius = (float)(width * Math.sqrt(2) / 2);

        mPaint.setColor(Color.parseColor("#E6E6E6"));
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(cx, cy, radius, mPaint);
    }

}
