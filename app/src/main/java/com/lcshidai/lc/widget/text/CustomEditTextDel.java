package com.lcshidai.lc.widget.text;

import com.lcshidai.lc.R;

import android.content.Context;
import android.text.Editable;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author test
 *         左边有文字说明的输入框
 */
public class CustomEditTextDel extends LinearLayout {
    private TextView tv_text_account;
    private EditText edt_text_input;
    private ImageView btn_text_delete;
    private int edit_let;

    public CustomEditTextDel(Context context) {
        super(context);
    }

    public CustomEditTextDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_edit_mark, this, true);
        tv_text_account = (TextView) view.findViewById(R.id.tv_text_account);
        edt_text_input = (EditText) view.findViewById(R.id.edt_text_input);
        btn_text_delete = (ImageView) view.findViewById(R.id.btn_text_delete);
        edt_text_input.addTextChangedListener(new TextWatcher() {
            boolean isChanged = false;
            char[] tempChar;
            StringBuffer buffer = new StringBuffer();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buffer.append(s.toString());
                isChanged = true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }
                    index = 0;
                    while (index < buffer.length()) {
                        if ((index == 3 || index == 8)) {
                            buffer.insert(index, ' ');
                        }
                        index++;
                    }
                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();

                    isChanged = false;
                }
                if (s.length() > 0) {
                    btn_text_delete.setVisibility(View.VISIBLE);
                } else {
                    btn_text_delete.setVisibility(View.GONE);
                }
                edit_let = s.length();
            }
        });

        edt_text_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && edit_let > 0) {//获得焦点
                    btn_text_delete.setVisibility(View.VISIBLE);
                } else {//失去焦点
                    btn_text_delete.setVisibility(View.GONE);
                }
            }
        });

        btn_text_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_text_input.setText("");
            }
        });
    }

    public ImageView getDeleteBT() {
        return btn_text_delete;
    }

    public EditText getEditTextET() {
        return edt_text_input;
    }

    /**
     * @param str 设置TextView显示文本
     */
    public void setText(String str) {
        tv_text_account.setText(str);
    }

    /**
     * @return 获取TextView的文本
     */
    public String getText() {
        return tv_text_account.getText().toString();
    }

    /**
     * @param str 设置EditText的默认值
     */
    public void setHint(String str) {
        edt_text_input.setHint(str);
    }

    public void setHint(SpannedString ss) {
        edt_text_input.setHint(ss);
    }

    /**
     * @return 获取EditText输入的内容
     */
    public String getEdtText() {
        return edt_text_input.getText().toString().trim();
    }

    public void setEdtText(String str) {
        edt_text_input.setText(str);
    }

    /**
     * @return 获取EditText输入的内容
     */

    public void setInputType(int type) {
        edt_text_input.setInputType(type);
    }
}
