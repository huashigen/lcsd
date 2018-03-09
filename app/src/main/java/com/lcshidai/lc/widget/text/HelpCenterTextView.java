package com.lcshidai.lc.widget.text;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class HelpCenterTextView extends TextView {

    public HelpCenterTextView(Context context) {
        super(context);
    }

    public HelpCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HelpCenterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setRefitText(int width, String text) {
        Paint paint = new Paint();
        paint.set(this.getPaint());
        char[] textChars = text.toCharArray();
        float[] widths = new float[text.length()];
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        paint.getTextWidths(text, widths);
        StringBuilder sb = new StringBuilder();
        float lineWidth = 0;
        for (int i = 0; i < widths.length; i++) {
            lineWidth += widths[i];
            if (lineWidth > width) {
                lineWidth = 0;
                if (textChars[i] > 65280 && textChars[i] < 65375) {    //顿号、是12289
                    sb.insert(sb.length() - 1, "\n");
                    lineWidth = widths[i - 1] + widths[i];
                } else {
                    sb.append("\n");
                    lineWidth = widths[i];
                }
            }
            sb.append(textChars[i]);
        }
        setText(sb.toString());
    }

    public void setRefitText(int width, String text, int start, int end, String color) {
        Paint paint = new Paint();
        paint.set(this.getPaint());
        char[] textChars = text.toCharArray();
        float[] widths = new float[text.length()];
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        paint.getTextWidths(text, widths);
        StringBuilder sb = new StringBuilder();
        float lineWidth = 0;
        for (int i = 0; i < widths.length; i++) {
            lineWidth += widths[i];
            if (lineWidth > width) {
                lineWidth = 0;
                if (textChars[i] > 65280 && textChars[i] < 65375) {    //顿号、是12289
                    sb.insert(sb.length() - 1, "\n");
                    lineWidth = widths[i - 1] + widths[i];
                } else {
                    sb.append("\n");
                    lineWidth = widths[i];
                }
            }
            sb.append(textChars[i]);
        }
        String value = sb.toString();
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '\n') {
                count++;
            }
        }
        SpannableString ss = new SpannableString(value);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(ss);
    }

    public void setRefitText(String text, String value, String color) {
        String str = text + value;
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor(color)), text.length(), str.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(ss);
    }
}
