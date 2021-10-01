package com.m520it.www.testwebview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webView);
         mIv =  (ImageView) findViewById(R.id.iv);
        //允许使用js
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        //加载本地文件网页
        mWebView.loadUrl("file:///android_asset/my.html");
//        mWebView.loadUrl("http://www.qq.com");
//        String body = "<html><head></head><body>我是一朵发</body></html>";
//        mWebView.loadDataWithBaseURL(null,body,"text/html","utf-8",null);
        //WebChromeClient  与js交互    如果不设置WebChromeClient,js也可能不能弹出框
        mWebView.setWebChromeClient(new WebChromeClient(){
            //进度的监听


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.e("xmg", "onProgressChanged: 进度:"+newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.e("xmg", "onReceivedTitle: 标题:"+title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                Log.e("xmg", "onReceivedIcon: "+"接收到网站的图标了");
                mIv.setImageBitmap(icon);
            }

//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                    Log.e("xmg", "onJsAlert: "+"我检测到js中有弹框alert警告  "+message);
////                return super.onJsAlert(view, url, message, result);
//                result.confirm();
//                return true;
//            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Log.e("xmg", "onJsConfirm: "+"我检测到js中有弹框Confirm  "+message);
                //相当于点击确定
                result.confirm();
                //相当于点击取消
//                result.cancel();
//                return super.onJsConfirm(view, url, message, result);
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.e("xmg", "onJsPrompt: "+"我检测到js中有弹框Prompt  "+message+ " defaultValue ");
                if("老司机".equals(defaultValue)){
                    result.confirm("滴!学生卡");
                }else{
                    result.confirm("你根本不是司机");
                }
                return true;
//                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("xmg", "onPageStarted: "+"开始加载");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("xmg", "onPageFinished: "+"加载完毕");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("xmg", "onReceivedError: "+"加载错误");
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.e("xmg", "onLoadResource: "+"url "+url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if(url.startsWith("http://")){
                    mWebView.loadUrl(url);
                }else if(url.startsWith("siji://")){
                    String data = url.replace("siji://", "");
                    Toast.makeText(MainActivity.this,data,Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        mWebView.addJavascriptInterface(MainActivity.this,"test");
    }

    //一定要写注解,不然js不然调用这个方法
    @JavascriptInterface
    public void startPage(){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),TwoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //能够后退
        if(mWebView.canGoBack()){
            mWebView.goBack();
            return;
        }
        super.onBackPressed();

    }

    public void click(View view) {
        Log.e("xmg", "click: "+"你点击了btn");
        mWebView.loadUrl("javascript:display_alert()");
    }
}
