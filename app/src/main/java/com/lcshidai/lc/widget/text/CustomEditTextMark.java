package com.lcshidai.lc.widget.text;

import com.lcshidai.lc.R;
import com.lcshidai.lc.widget.animation.ChangeMarginAnimation;

import android.content.Context;
import android.text.Editable;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * @author test 左边有文字说明的输入框
 */
public class CustomEditTextMark extends LinearLayout {
	private TextView tv_text_account, tv_text_note,custom_edit_mark_reg,tologin;
	private EditText edt_text_input;
	private ImageView btn_text_delete;
	private ImageView img_icon;
	// private boolean misshow;
	private ViewSwitcher vs_box;
	private View rl_submit;

	public CustomEditTextMark(Context context) {
		super(context);
	}

	public CustomEditTextMark(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(
				R.layout.custom_edit_mark_reg, this, true);
		tv_text_account = (TextView) view.findViewById(R.id.tv_text_account);
		edt_text_input = (EditText) view.findViewById(R.id.edt_text_input);
		btn_text_delete = (ImageView) view.findViewById(R.id.btn_text_delete);
		tv_text_note = (TextView) view.findViewById(R.id.text_note);
		tologin = (TextView) view.findViewById(R.id.tologin);
		vs_box = (ViewSwitcher) view.findViewById(R.id.vs_box);
		rl_submit = view.findViewById(R.id.rl_submit);
		img_icon=(ImageView) view.findViewById(R.id.img_icon);
		android.view.ViewGroup.LayoutParams para;
		para = img_icon.getLayoutParams();
		para.height =60;
		para.width = 60;
		img_icon.setLayoutParams(para);
		// if (!misshow) {
		// tv_text_note.setVisibility(View.VISIBLE);
		// }
		edt_text_input.addTextChangedListener(new TextWatcher() {
			boolean isChanged = false;
			char[] tempChar;
			StringBuffer buffer = new StringBuffer();

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				buffer.append(s.toString());
				isChanged = true;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
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

					tv_text_note.setText(str);
					isChanged = false;
				}
				// if (misshow) {

				if (s.length() > 0) {
					if (android.os.Build.VERSION.SDK_INT > 12) {
						Animation anim_show_sort = new ChangeMarginAnimation(
								vs_box, tv_text_note.getHeight());
						anim_show_sort.setDuration(500);
						anim_show_sort.setFillAfter(true);
						vs_box.startAnimation(anim_show_sort);
					}
					btn_text_delete.setVisibility(View.VISIBLE);
				} else {
					btn_text_delete.setVisibility(View.GONE);
					if (android.os.Build.VERSION.SDK_INT > 12) {
						tv_text_note.setText("");
						Animation anim_close_sort = new ChangeMarginAnimation(
								vs_box, 0);
						anim_close_sort.setDuration(500);
						anim_close_sort.setFillAfter(true);
						vs_box.startAnimation(anim_close_sort);
					}
				}
				// }else {
				// if(s.length() > 0){
				// btn_text_delete.setVisibility(View.VISIBLE);
				// }else{
				// btn_text_delete.setVisibility(View.GONE);
				// tv_text_note.setText("");
				// }
				// }
			}
		});

		btn_text_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edt_text_input.setText("");
			}
		});
	}

	// public void setisShow(boolean isshow){
	// misshow = isshow;
	// }

	public ImageView getDeleteBT() {
		return btn_text_delete;
	}

	public EditText getEditTextET() {
		return edt_text_input;
	}

	/**
	 * @param str
	 *            设置TextView显示文本
	 */
	public void setText(String str) {
		tv_text_account.setText(str);
	}
	public void setTextET(String str) {
		edt_text_input.setText(str);
	}
	/**
	 * @param str
	 * 设置TextView左边图片
	 */
	public void setIcon(int draw){
		img_icon.setBackgroundResource(draw);
		img_icon.setVisibility(View.VISIBLE);
	}
	
	/**
	 * @return 获取TextView的文本
	 */
	public String getText() {
		return tv_text_account.getText().toString();
	}

	/**
	 * @param str
	 *            设置EditText的默认值
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
	public void setTextNote(String str) {
		tv_text_note.setText(str);
	}

	public void setInputType(int type) {
		edt_text_input.setInputType(type);
	}

	public View getrl_submit() {
		// TODO Auto-generated method stub
		return rl_submit;
	}

	public TextView gettologin() {
		// TODO Auto-generated method stub
		return tologin;
	}
}
