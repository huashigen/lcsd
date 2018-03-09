package com.lcshidai.lc.widget;


import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lcshidai.lc.R;

/**
 * 分段控件
 * 
 * @ClassName: SegmentButton
 * @Description: TODO
 * @date: 2014年3月12日 下午11:13:55
 */
public class SegmentButton extends LinearLayout implements View.OnClickListener {
	public OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(int position, Button button) {
			
		}
	};

	public void setOnCheckedChangeListener(
			OnCheckedChangeListener onCheckedChangeListener) {
		this.onCheckedChangeListener = onCheckedChangeListener;
	}

	private Context context;
	private ArrayList<Button> buttonList;
	private String[] contentStr;
	private int position = 0;

	private int textPressColor;
	private int textNormalColor;
	private int buttonCount;
	public float mtextSize;
	public String buttonContent;

	public SegmentButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public SegmentButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.SegmentButton);
		textPressColor = typedArray.getColor(
				R.styleable.SegmentButton_textPressColor, Color.WHITE);
		textNormalColor = typedArray.getColor(
				R.styleable.SegmentButton_textNormalColor,
				Color.rgb(0x0b, 0x55, 0x92));
		mtextSize =15f;// typedArray.getFloat(R.styleable.SegmentButton_textSize, 16);
		buttonContent =typedArray.getString(R.styleable.SegmentButton_buttonContent);
		if (buttonContent != null && (!"".equals(buttonContent))) {
			contentStr = buttonContent.split(";");
			if (contentStr != null) {
				buttonCount = contentStr.length;
			} else {
				buttonCount = 0;
			}
		} else {
			buttonCount = 0;
		}
		setView();
	}

	private void setView() {
		buttonList = new ArrayList<Button>();
		if (buttonCount == 1) {
			Button button = new Button(context);
			button.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.segment_n));
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP,mtextSize);
			button.setTextColor(textPressColor);
			button.setGravity(Gravity.CENTER);
			button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.MATCH_PARENT));
			button.setTag(0);
			this.addView(button);
			button.setOnClickListener(this);
			buttonList.add(button);
		} else {
			for (int i = 0; i < buttonCount; i++) {
				Button button = new Button(context);
				if (i == 0) {
					button.setBackgroundDrawable(context.getResources()
							.getDrawable(R.drawable.segment_press_left));
					button.setTextColor(textNormalColor);
				} else if (i == buttonCount - 1) {
					button.setBackgroundDrawable(context.getResources()
							.getDrawable(R.drawable.segmengt_r_normal));
					button.setTextColor(textPressColor);
				} else {
					button.setBackgroundDrawable(context.getResources()
							.getDrawable(R.drawable.segmengt_r_normal));
					button.setTextColor(textPressColor);
				}
				button.setTextSize(TypedValue.COMPLEX_UNIT_SP,mtextSize);
				button.setGravity(Gravity.CENTER);
				button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.MATCH_PARENT));
				button.setTag(i);
				button.setPadding(28, 12, 28, 12);
				this.addView(button);
				button.setOnClickListener(this);
				buttonList.add(button);
			}
		}

		for (int i = 0; i < buttonCount; i++) {
			buttonList.get(i).setText(contentStr[i]);
		}
	}

	public int getPosition() {
		return position;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		for (int i = 0; i < buttonCount; i++) {
			if (i == 0) {
				if (((Integer) v.getTag()) == i) {
					buttonList.get(i).setBackgroundDrawable(
							context.getResources().getDrawable(
									R.drawable.segment_press_left));
					buttonList.get(i).setTextColor(textNormalColor);
					position = i;
					onCheckedChangeListener.onCheckedChanged(i,
							buttonList.get(i));
				} else {
					buttonList.get(i).setBackgroundDrawable(
							context.getResources().getDrawable(
									R.drawable.segment_l_normal));
					buttonList.get(i).setTextColor(textPressColor);
				}
			} else if (i == buttonCount - 1) {
				if (((Integer) v.getTag()) == i) {
					buttonList.get(i).setBackgroundDrawable(
							context.getResources().getDrawable(
									R.drawable.segment_press));
					buttonList.get(i).setTextColor(textNormalColor);
					position = i;
					onCheckedChangeListener.onCheckedChanged(i,
							buttonList.get(i));
				} else {
					buttonList.get(i).setBackgroundDrawable(
							context.getResources().getDrawable(
									R.drawable.segmengt_r_normal));
					buttonList.get(i).setTextColor(textPressColor);
				}
			} else {
				if (((Integer) v.getTag()) == i) {
					buttonList.get(i).setBackgroundDrawable(
							context.getResources().getDrawable(
									R.drawable.segment_press));
					buttonList.get(i).setTextColor(textNormalColor);
					position = i;
					onCheckedChangeListener.onCheckedChanged(i,
							buttonList.get(i));
				} else {
					buttonList.get(i).setBackgroundDrawable(
							context.getResources().getDrawable(
									R.drawable.segmengt_r_normal));
					buttonList.get(i).setTextColor(textPressColor);
				}
			}
		}
	}

	public Button getSegmentButton(int position) {
		return buttonList.get(position);
	}

	public interface OnCheckedChangeListener {
		void onCheckedChanged(int position, Button button);
	}
}
