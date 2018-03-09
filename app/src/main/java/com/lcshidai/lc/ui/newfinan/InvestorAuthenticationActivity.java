package com.lcshidai.lc.ui.newfinan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.AgreementActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
/**
 * 合格投资者认证
 * @author 000853
 *
 */
public class InvestorAuthenticationActivity extends TRJActivity implements OnClickListener {

	private TextView mTvTitle, mSubTitle;
	private Button  mSaveBtn;
	ImageButton mBackBtn;
	private Context mContext;
	
	private TextView agree_tv;
	private Button submit_bt, cancel_bt;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initView();
	}

	@Override
	protected boolean isNotApplyTranslucent() {
		return false;
	}

	private void initView(){
		mContext = this;
		setContentView(R.layout.activity_investor_authentication);
		mBackBtn=(ImageButton) findViewById(R.id.btn_back);
		mBackBtn.setVisibility(View.INVISIBLE);
		mSaveBtn=(Button)findViewById(R.id.btn_option);
		mSaveBtn.setVisibility(View.INVISIBLE);
		mTvTitle=(TextView)findViewById(R.id.tv_top_bar_title);
		mTvTitle.setText("合格投资者认证");
		mSubTitle=(TextView)findViewById(R.id.tv_subtitle);
		mSubTitle.setVisibility(View.GONE);
		
		submit_bt = (Button) findViewById(R.id.investor_authentication_bt_submit);
		cancel_bt = (Button) findViewById(R.id.investor_authentication_bt_cancel);
		submit_bt.setOnClickListener(this);
		cancel_bt.setOnClickListener(this);
		agree_tv = (TextView) findViewById(R.id.investor_authentication_tv_agree);
		agree_tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = agree_tv.getText();
        if (text instanceof Spannable) {
                int end = text.length();
                Spannable sp = (Spannable) agree_tv.getText();
                URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                SpannableStringBuilder style = new SpannableStringBuilder(text);
                style.clearSpans(); // should clear old spans
                for (URLSpan url : urls) {
                        MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
                        style.setSpan(myURLSpan, sp.getSpanStart(url),
                                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                agree_tv.setText(style);
        }
	}
    
	private class MyURLSpan extends ClickableSpan {
        private String mUrl;

        MyURLSpan(String url) {
                mUrl = url;
        }

        @Override
        public void onClick(View widget) {
        	if(mUrl.equals("1")){
        		Intent intent = new Intent(InvestorAuthenticationActivity.this,AgreementActivity.class);
				intent.putExtra("title","理财时代注册协议");
				intent.putExtra("url", "/Index/Protocol/view?id=1");
				startActivity(intent);
        	}
        }
        
        @Override
    	public void updateDrawState(TextPaint ds) {
    	    ds.setColor(Color.parseColor("#2B7EC2"));
    	    ds.setUnderlineText(false); //去掉下划线
    	}
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.investor_authentication_bt_submit:
			
			break;
		case R.id.investor_authentication_bt_cancel:
			InvestorAuthenticationActivity.this.finish();
			break;
		}
	}
	
}
