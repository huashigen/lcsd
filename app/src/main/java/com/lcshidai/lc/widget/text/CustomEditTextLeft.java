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
 * 左边有文字说明的输入框
 */
public class CustomEditTextLeft extends LinearLayout{
	private TextView tv_text_account,tv_text_note,tv_gain_code;
	private EditText edt_text_input;
	private ImageView btn_text_delete;

	public CustomEditTextLeft(Context context) {
		super(context);
	}
	
	public CustomEditTextLeft(Context context,AttributeSet attrs) {
		super(context,attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.custom_edit_lefttext,this, true);
		tv_text_account = (TextView)view.findViewById(R.id.tv_text_account);
		tv_gain_code = (TextView)view.findViewById(R.id.tv_gain_code);
		edt_text_input = (EditText)view.findViewById(R.id.edt_text_input);
		btn_text_delete = (ImageView)view.findViewById(R.id.btn_text_delete);
		tv_text_note = (TextView)view.findViewById(R.id.text_note);
		edt_text_input.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 0){
					btn_text_delete.setVisibility(View.VISIBLE);
				}else{
					btn_text_delete.setVisibility(View.GONE);
				}
			}
		});
		
		edt_text_input.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus&&edt_text_input.getText().toString().length() > 0){
					btn_text_delete.setVisibility(View.VISIBLE);
				}else{
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
	
	public ImageView getDeleteBT(){
		return btn_text_delete;
	}
	
	public TextView getGainTV(){
		return tv_gain_code;
	}
	
	/**
	 * @param str
	 * 设置TextView显示文本
	 */
	public void setText(String str){
		tv_text_account.setText(str);
	}
	
	/**
	 * @return
	 * 获取TextView的文本
	 */
	public String getText(){
		return tv_text_account.getText().toString();
	}
	
	/**
	 * @param str
	 * 设置EditText的默认值
	 */
	public void setHint(String str){
		edt_text_input.setHint(str);
	}
	
	public void setHint(SpannedString ss){
		edt_text_input.setHint(ss);
	}
	
	public void setEdtText(String str){
		edt_text_input.setText(str);
	}
	
	/**
	 * @return
	 * 获取EditText输入的内容
	 */
	public String getEdtText(){
		return edt_text_input.getText().toString();
	}
	
	/**
	 * @return
	 * 获取EditText输入的内容
	 */
	public void setTextNote(String str){
		tv_text_note.setText(str);
	}
}
