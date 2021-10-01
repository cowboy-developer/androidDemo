package com.m520it.www.newsreader.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.m520it.www.newsreader.R;

public class AdWebViewActivity extends AppCompatActivity {

    public static final String AD_LINK_URL = "ad_link_url";
    private Intent mIntent;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_web_view);
        mIntent = getIntent();
        initView();
        initData();
    }

    private void initData() {
        String link_url = mIntent.getStringExtra(AD_LINK_URL);
        Log.e("xmg", "initData: link_url "+link_url);
        //如果你的网页中产生了跳转,就会在跳转时默认打开系统的浏览器

        //修改它的配置,即使是跳转,也是在当前页面的WebVIew展示
        //为了看到正常的效果,不要禁用javaScript
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient(){

            //复写方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //默认super效果就会默认打开系统的浏览器来加载
                //新的跳转地址需要让WebView控件重新load一下
                mWebView.loadUrl(url);
                return true;
            }
        });

        mWebView.loadUrl(link_url);
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webView);
    }
}
