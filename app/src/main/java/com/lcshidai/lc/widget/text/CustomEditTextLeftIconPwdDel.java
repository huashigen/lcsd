package com.lcshidai.lc.widget.text;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import com.lcshidai.lc.R;

public class CustomEditTextLeftIconPwdDel extends LinearLayout{
	protected TextView tv_text_note;
	protected EditText edt_text_input;
	protected ImageView btn_text_delete;
	protected ImageView iv_icon;
	protected ImageView btn_text_show;
	private int edit_let;

	public CustomEditTextLeftIconPwdDel(Context context) {
		super(context);
	}
	
	public CustomEditTextLeftIconPwdDel(Context context,AttributeSet attrs) {
		super(context,attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.custom_edit_lefttextpwddel,this, true);
		iv_icon = (ImageView)view.findViewById(R.id.iv_icon);
		edt_text_input = (EditText)view.findViewById(R.id.edt_text_input);
		btn_text_delete = (ImageView)view.findViewById(R.id.btn_text_delete);
		tv_text_note = (TextView)view.findViewById(R.id.text_note);
		btn_text_show = (ImageView)view.findViewById(R.id.btn_text_show);
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
				edit_let = s.length();
			}
		});
		
		edt_text_input.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus&&!tv_text_note.getText().equals("")){
					tv_text_note.setVisibility(View.VISIBLE);
				}else{
					tv_text_note.setVisibility(View.GONE);
				}
				
				if(hasFocus&&edit_let>0&&edt_text_input.getText().toString().length() > 0){
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
	
	/**
	 * @param draw
	 * 设置TextView左边图片
	 */
	public void setIcon(Drawable draw){
		iv_icon.setBackgroundDrawable(draw);
		iv_icon.setVisibility(View.VISIBLE);
	}
	
//	/**
//	 * @return
//	 * 获取TextView的文本
//	 */
//	public String getText(){
//		return tv_text_account.getText().toString();
//	}
	
	/**
	 *填入EditText输入的内容
	 */
	public void setText(String str){
		edt_text_input.setText(str);
	}
	
	public EditText getET(){
		return edt_text_input;
	}
	
	/**
	 * @param str 设置内容
	 * 设置EditText的值
	 */
	public void setHint(String str){
		edt_text_input.setHint(str);
	}
	
	
	public void setHint(SpannedString ss){
		edt_text_input.setHint(ss);
	}
	/**
	 * @return
	 * 获取EditText输入的内容
	 */
	public String getEdtText(){
		return edt_text_input.getText().toString();
	}
	
	/**
	 * 设置下方提示信息
	 * @param str 提示信息内容
	 */
	public void setTextNote(String str){
		tv_text_note.setText(str);
	}
	
	public void setTextChangeListener(TextWatcher listener){
		edt_text_input.addTextChangedListener(listener);
	}
	
	public void setInputType(int type){
		edt_text_input.setInputType(type);
    }
	
	public void setTextSize(int size){
		edt_text_input.setTextSize(size);
	}
}
