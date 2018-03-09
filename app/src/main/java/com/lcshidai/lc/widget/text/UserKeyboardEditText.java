package com.lcshidai.lc.widget.text;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;

public class UserKeyboardEditText extends LinearLayout {

    public interface UserKeyBoardEditTextClickListener {
        void onDelete();

        void onUKBETClick();
    }

    private TextView value_tv;
    private Button delete_bt;
    private String valueText;
    private int textSize = 14;
    private int textColor = Color.parseColor("#DDDDDD");

    private String hintText = "";
    private int hintSize = 14;
    private int hintColor = textColor;
    private UserKeyBoardEditTextClickListener listener;

    public UserKeyboardEditText(Context context) {
        super(context);
    }

    public UserKeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_user_keyboard_edit_text, this, true);
        value_tv = (TextView) view.findViewById(R.id.user_keyboard_edit_text_tv_value);
        delete_bt = (Button) view.findViewById(R.id.user_keyboard_edit_text_bt_delete);

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onUKBETClick();
            }
        });

        delete_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onDelete();
                clearText();
            }
        });
    }

    public void setClickListner(UserKeyBoardEditTextClickListener listener) {
        this.listener = listener;
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(valueText)) {
            value_tv.setTextColor(textColor);
            value_tv.setTextSize(textSize);
        }
        this.valueText = text;
        if (!TextUtils.isEmpty(valueText)) {
            if (delete_bt.getVisibility() == View.GONE) {
                delete_bt.setVisibility(View.VISIBLE);
            }
            value_tv.setText(valueText);
        } else {
            setHint();
        }
    }

    public void editText(String editText) {
        if (TextUtils.isEmpty(valueText)) {
            value_tv.setTextColor(textColor);
            value_tv.setTextSize(textSize);
        }
        if (delete_bt.getVisibility() == View.GONE) {
            delete_bt.setVisibility(View.VISIBLE);
        }
        valueText = valueText + editText;
        value_tv.setText(valueText);
    }

    public void deleteText() {
        if (TextUtils.isEmpty(valueText)) return;
        valueText = valueText.substring(0, valueText.length() - 1);
        if (TextUtils.isEmpty(valueText))
            setHint();
    }

    public void clearText() {
        valueText = "";
        setHint();
    }

    public void setHintParams(String hintText, int hintColor, int textSize) {
        this.hintText = hintText;
        this.hintColor = hintColor;
        this.hintSize = textSize;
        setHint();

    }

    public void setHint() {
        value_tv.setTextSize(hintSize);
        value_tv.setTextColor(hintColor);
        value_tv.setText(hintText);
        delete_bt.setVisibility(View.GONE);
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public void setTextSize(int size) {
        this.textSize = size;
    }

    public void setDeleteButtonSize(int size) {
        android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) delete_bt.getLayoutParams();
        lp.width = size;
        lp.height = size;
        delete_bt.setLayoutParams(lp);
    }

    public void setSingleLine(boolean singleLine) {
        value_tv.setSingleLine(singleLine);
    }

    public void setEditPadding(int left, int top, int right, int bottom) {
        value_tv.setPadding(left, top, right, bottom);
    }

}
