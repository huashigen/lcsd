package com.lcshidai.lc.widget.text;

import android.content.Context;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;

/**
 * 右边带单位的输入框
 *
 * @author 000814
 */
public class CustomEditTextRightUnit extends LinearLayout {
    protected EditText edt_text_input;
    protected TextView edt_unit;

    public CustomEditTextRightUnit(Context context) {
        super(context);
    }

    public CustomEditTextRightUnit(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_edit_rightunittext, this, true);
        edt_text_input = (EditText) view.findViewById(R.id.edt_text_input);
        edt_unit = (TextView) view.findViewById(R.id.tv_unit);
    }


//	/**
//	 * @return
//	 * 获取TextView的文本
//	 */
//	public String getText(){
//		return tv_text_account.getText().toString();
//	}

    /**
     * 单位
     *
     * @param unitStr
     */
    public void setUnit(String unitStr) {
        edt_unit.setText(unitStr);
    }

    /**
     * 填入EditText输入的内容
     */
    public void setText(String str) {
        edt_text_input.setText(str);
    }

    public EditText getET() {
        return edt_text_input;
    }

    /**
     * @param str 设置内容
     *            设置EditText的值
     */
    public void setHint(String str) {
        edt_text_input.setHint(str);
    }

    /**
     * @return 获取EditText输入的内容
     */
    public String getEdtText() {
        return edt_text_input.getText().toString();
    }


    public void setTextChangeListener(TextWatcher listener) {
        edt_text_input.addTextChangedListener(listener);
    }

    public void setInputType(int type) {
        edt_text_input.setInputType(type);
    }

    public void setPassword() {
        edt_text_input.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}
