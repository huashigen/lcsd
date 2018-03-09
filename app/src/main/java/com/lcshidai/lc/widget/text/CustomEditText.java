package com.lcshidai.lc.widget.text;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;

/**
 * @author 普通右边带删除按钮的输入框
 */
public class CustomEditText extends LinearLayout {

    private EditText edt_input;
    private ImageView btn_delete;
    private TextView tv_right;
    private LinearLayout accountLinear;
    private View.OnFocusChangeListener onFocusChangeListener;

    OnClickListener mClick;

    public void setOnFocusChangeListener(View.OnFocusChangeListener listener) {
        onFocusChangeListener = listener;
    }

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_edit_text, this, true);
        edt_input = (EditText) view.findViewById(R.id.edt_input);
        btn_delete = (ImageView) this.findViewById(R.id.btn_delete);
        tv_right = (TextView) view.findViewById(R.id.tv_right);
        accountLinear = (LinearLayout) view.findViewById(R.id.accountLinear);
        edt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btn_delete.setVisibility(View.VISIBLE);
                } else {
                    btn_delete.setVisibility(View.GONE);
                }
            }
        });

        edt_input.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && edt_input.getText().length() > 0) {
                    btn_delete.setVisibility(View.VISIBLE);
                } else {
                    btn_delete.setVisibility(View.GONE);
                }
            }
        });

        btn_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_input.setText("");
            }
        });

        tv_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mClick.onClick(v);
            }
        });
    }

    /**
     * @param str 设置输入框显示的默认值
     */
    public void setHint(String str) {
        edt_input.setHint(str);
    }

    /**
     * @return 获取输入框中的内容
     */
    public String getText() {
        return edt_input.getText().toString();
    }


    public void setBackground(Drawable resource) {
        accountLinear.setBackground(resource);
    }

    public void setBackgroundResource(int resource) {
        accountLinear.setBackgroundResource(resource);
    }

    public void setTextSize(float size) {
        edt_input.setTextSize(size);
    }

    public void setInputType(int type) {
        edt_input.setInputType(type);
    }

    public void setText(String text) {
        edt_input.setText(text);
    }

    public void setTextColor(ColorStateList color) {
        edt_input.setTextColor(color);
    }

    public void setTextColor(int color) {
        edt_input.setTextColor(color);
    }

    /**
     * 设置右边文字的颜色
     *
     * @param color
     */
    public void setRightTextColor(int color) {
        tv_right.setTextColor(color);
    }

    /**
     * 设置右边文字的大小
     *
     * @param size
     */
    public void setRightTextSize(float size) {
        tv_right.setTextSize(size);
    }

    /**
     * 右边显示删除图标
     */
    public void showBtn() {
        btn_delete.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.GONE);
    }

    /**
     * 右边显示文字
     */
    public void showRightTv(String text) {
        tv_right.setText(text);
        btn_delete.setVisibility(View.GONE);
        tv_right.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边文字的点击事件
     *
     * @param click
     */
    public void setRightTextClick(OnClickListener click) {
        mClick = click;
    }

    /**
     * 设置是否可获取焦点
     */
    public void setFocusable(boolean b) {
        if (b) {
            edt_input.setFocusableInTouchMode(true);
        }
        edt_input.setFocusable(b);
    }

    /**
     * 设置光标的位置
     */
    public void setSelection(int index) {
        edt_input.setSelection(index);
    }

}
