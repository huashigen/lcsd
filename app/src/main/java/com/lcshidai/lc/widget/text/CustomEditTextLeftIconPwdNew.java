package com.lcshidai.lc.widget.text;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;

import com.lcshidai.lc.R;

public class CustomEditTextLeftIconPwdNew extends CustomEditTextLeftIconPwdDel{
	public boolean isVisable=false;
	
	public CustomEditTextLeftIconPwdNew(Context context) {
		super(context);
		edt_text_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	}
	
	public CustomEditTextLeftIconPwdNew(Context context,AttributeSet attrs) {
		super(context,attrs);
		
		edt_text_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		btn_text_show.setBackgroundDrawable(getResources().getDrawable(R.drawable.pwd_invisible));
		
		btn_text_show.setVisibility(View.VISIBLE);
		
		btn_text_show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isVisable){
					btn_text_show.setBackgroundResource(R.drawable.pwd_invisible);
					edt_text_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					isVisable=false;
				}else{
					btn_text_show.setBackgroundResource(R.drawable.pwd_visible);
					edt_text_input.setInputType(InputType.TYPE_CLASS_TEXT);
					isVisable=true;
				}
			}
		});
		
//		edt_text_input.setOnFocusChangeListener(null);
	}
}
