package com.lcshidai.lc.widget.text;

import com.lcshidai.lc.R;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
/**
 * @author test
 * 银行卡卡号4位加空格输入框
 */
public class CustomEditBank extends LinearLayout{
	
	private EditText edt_bank_input;
	private ImageView btn_bank_delete;
	protected RelativeLayout rl_main;

	public CustomEditBank(Context context) {
		super(context);
	}
	
	public CustomEditBank(Context context,AttributeSet attrs) {
		super(context,attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.custom_edit_text,this, true); 
		rl_main = (RelativeLayout)view.findViewById(R.id.accountLinear);
		edt_bank_input = (EditText)view.findViewById(R.id.edt_input);
		btn_bank_delete = (ImageView)this.findViewById(R.id.btn_delete);
		
		edt_bank_input.addTextChangedListener(new TextWatcher() {
			
			int beforeTextLength = 0;  
			int onTextLength = 0;  
			boolean isChanged = false;  
			int location = 0;// 记录光标的位置  
			char[] tempChar;  
			StringBuffer buffer = new StringBuffer();  
			int konggeNumberB = 0;  
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				beforeTextLength = s.length();  
				if (buffer.length() > 0) {  
					buffer.delete(0, buffer.length());  
				}  
				konggeNumberB = 0;  
				for (int i = 0; i < s.length(); i++) {  
					if (s.charAt(i) == ' ') {  
						konggeNumberB++;  
					}  
				}  
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				onTextLength = s.length();  
				buffer.append(s.toString());  
				if (onTextLength == beforeTextLength || onTextLength <= 3  
						|| isChanged) {  
					isChanged = false;  
					return;  
				}  
				isChanged = true;  
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (isChanged) {  
					location = edt_bank_input.getSelectionEnd();  
					int index = 0;  
					while (index < buffer.length()) {  
						if (buffer.charAt(index) == ' ') {  
							buffer.deleteCharAt(index);  
						} else {  
							index++;  
						}  
					}  
					index = 0;  
					int konggeNumberC = 0;  
					while (index < buffer.length()) {  
						if ((index == 4 || index == 9 || index == 14 || index == 19)) {  
							buffer.insert(index, ' ');  
							konggeNumberC++;  
						}  
						index++;  
					}  
					if (konggeNumberC > konggeNumberB) {  
						location += (konggeNumberC - konggeNumberB);  
					}  
					tempChar = new char[buffer.length()];  
					buffer.getChars(0, buffer.length(), tempChar, 0);  
					String str = buffer.toString();  
					if (location > str.length()) {  
						location = str.length();  
					} else if (location < 0) {  
						location = 0;  
					}  
					edt_bank_input.setText(str);  
					Editable etable = edt_bank_input.getText();  
					Selection.setSelection(etable, location);  
					isChanged = false;  
				}
				if(s.length() > 0){
					btn_bank_delete.setVisibility(View.VISIBLE);
				}else{
					btn_bank_delete.setVisibility(View.GONE);
				}
			}
		});
		
		btn_bank_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edt_bank_input.setText("");
			}
		});
	}
	
	/**
	 * @param str
	 * 设置输入框显示的默认值
	 */
	public void setHint(String str){
		edt_bank_input.setHint(str);
	}
	
	public EditText getET(){
		return edt_bank_input;
	}
	
	/**
	 * @return
	 * 获取输入框中的内容
	 */
	public String getEdtText(){
		return edt_bank_input.getText().toString();
	}
	
    public void setInputType(int type){
    	edt_bank_input.setInputType(type);
    }
    
    public void setEdtText(String str){
    	edt_bank_input.setText(str);
    }
    
    public void setEdtSelection(int len){
    	edt_bank_input.setSelection(len);
    }
    
    public void setEditMargin(int left, int top, int right, int bottom){
		android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) edt_bank_input.getLayoutParams();
		lp.setMargins(left, top, right, bottom);
		edt_bank_input.setLayoutParams(lp);
	}
    
    public void setDeleteButtonMargin(int left, int top, int right, int bottom){
		android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) btn_bank_delete.getLayoutParams();
		lp.setMargins(left, top, right, bottom);
		btn_bank_delete.setLayoutParams(lp);
	}
	
	public void setEditPadding(int left, int top, int right, int bottom){
		btn_bank_delete.setPadding(left, top, right, bottom);
	}
	
	public void setDeleteButtonSize(int size){
		android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) btn_bank_delete.getLayoutParams();
		lp.width = size;
		lp.height = size;
		btn_bank_delete.setLayoutParams(lp);
	}
	
	public void setTextSize(int size){
		edt_bank_input.setTextSize(size);
	}
	
	public void setBackgroundColor(int color){
		rl_main.setBackgroundColor(color);
	}

}
