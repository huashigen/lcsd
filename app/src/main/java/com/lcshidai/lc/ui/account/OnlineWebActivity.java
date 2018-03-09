package com.lcshidai.lc.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lcshidai.lc.R;


/**
 * Created by admin on 2017/8/14.
 */

class OnlineWebActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_web);

        Intent intent = getIntent();
        String onlineUrl = intent.getStringExtra("online");
        WebView mWebView = (WebView) findViewById(R.id.online_web);
        mWebView.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        mWebView.loadUrl(onlineUrl);
        //设置Web视图
        mWebView.setWebViewClient(new WebViewClient());

    }

}
