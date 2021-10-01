package com.m520it.www.newsreader.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.bean.ArticleDetailBean;
import com.m520it.www.newsreader.bean.ArticleImageBean;
import com.m520it.www.newsreader.http.HttpHelper;
import com.m520it.www.newsreader.http.HttpStringCallBack;
import com.m520it.www.newsreader.util.Constant;
import com.m520it.www.newsreader.util.InputUtil;
import com.m520it.www.newsreader.util.JsonUtil;
import com.m520it.www.newsreader.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.m520it.www.newsreader.activity.SHowPicActivity.PIC_INDEX;
import static com.m520it.www.newsreader.activity.SHowPicActivity.PIC_LIST;

public class NewsDetailActivity extends AppCompatActivity {

    public static final String DOC_ID = "doc_id";
    private String mDocid;
    private WebView mWebView;
    private EditText mEtReply;
    private TextView mTvReply;
    private ImageView mIvShare;
    private TextView mTvSendReply;
    private ArrayList<ArticleImageBean> mImgList;//WebView中所有图片的集合
    private Drawable mDrawableLeft;
    private Drawable mDrawableBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_tetail);
        Intent intent = getIntent();
        mDocid = intent.getStringExtra(DOC_ID);
//        Log.e("xmg", "onCreate: docid "+ mDocid);
        initView();
        initData();
    }

    private void initData() {
        requestData();
    }

    private void requestData() {
        String url = Constant.getNewsDetailUrl(mDocid);
        Log.e("xmg", "requestData: "+url);
        HttpHelper.getInstance(getApplicationContext()).requestGETStringResult(url, new HttpStringCallBack() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject detailJsonObj = jsonObject.optJSONObject(mDocid);
                    String s = detailJsonObj.toString();
                    ArticleDetailBean articleDetailBean = JsonUtil.parseJson(s, ArticleDetailBean.class);
                    LogUtil.e("xmg", "onSuccessResponse: articleDetailBean "+articleDetailBean.toString());
                    //加载数据到WebView上
                    setDataToWebView(articleDetailBean);
                } catch (JSONException e) {
                    Log.e("xmg", "onSuccessResponse: "+"解析json异常, e:"+e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private void setDataToWebView(ArticleDetailBean articleDetailBean) {
        mTvReply.setText(String.valueOf(articleDetailBean.getReplyCount()));
        //直接加载会样式很丑,而且没标题
        String body = articleDetailBean.getBody();
        //将body中的<!--img#0-->这样的标签进行替换
        mImgList = (ArrayList<ArticleImageBean>) articleDetailBean.getImg();
        int size = mImgList.size();
        for (int i = 0; i < size; i++) {
            ArticleImageBean articleImageBean = mImgList.get(i);
            //<img src="ic_launcher.png"/>

            body = body.replace(articleImageBean.getRef(),"<img  onClick=\"show("+i+")\" src = \""+articleImageBean.getSrc()+"\"/>");
            //
        }

            //设置标题的一些处理
        String titleHTML = "<p style = \"margin:25px 0px 0px 0px\"><span style='font-size:22px;'><strong>" + articleDetailBean.getTitle() + "</strong></span></p>";// 标题
        titleHTML = titleHTML+ "<p><span style='color:#999999;font-size:14px;'>"+articleDetailBean.getSource()+"&nbsp&nbsp"+articleDetailBean.getPtime()+"</span></p>";//来源与时间
        //加条虚线
        titleHTML = titleHTML+"<div style=\"border-top:1px dotted #999999;margin:20px 0px\"></div>";
        //设置正文的字体和行间距
        body = "<div style='line-height:180%;'><span style='font-size:15px;'>"+body+"</span></div>";
        body = titleHTML+body;
        String script = "<script>function show(i){window.news.showBigPic(i);}</script>";
        body = "<html><head><style>img{width:100%;}</style>"+script+"</head><body>"+body+"</body></html>";

//        body = "<html><head><style>img{width:100%}</style><script type='text/javascript'>function show(s){window.demo.javaShow(s);}</script></head>"+body+"</html>";
        mWebView.loadDataWithBaseURL(null,body,null,"utf-8",null);


    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webView);
        mEtReply = (EditText) findViewById(R.id.et_reply);
        mTvReply = (TextView) findViewById(R.id.tv_reply);
        mTvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),KeybroadManActivity.class);
                intent.putExtra(DOC_ID,mDocid);
                startActivity(intent);
            }
        });
        mIvShare = (ImageView) findViewById(R.id.iv_share);
        mTvSendReply = (TextView) findViewById(R.id.tv_send_reply);
        //允许js可用
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(NewsDetailActivity.this,"news");
        //给WebView设置触摸,一旦触摸,就把焦点抢过来
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //请求焦点
                mWebView.requestFocus();
                InputUtil.closeSoftInput(NewsDetailActivity.this);
                return false;
            }
        });

        //准备两个Drawable出来
//        android:drawableLeft="@drawable/icon_edit_icon"
//        android:drawableBottom="@drawable/bg_edit_text"
        //左边
        mDrawableLeft = getResources().getDrawable(R.drawable.icon_edit_icon);
        //使用Drawable之前,需要给其设置setBounds方法,
        mDrawableLeft.setBounds(0,0,mDrawableLeft.getIntrinsicWidth(),mDrawableLeft.getIntrinsicHeight());
        //下边的
        mDrawableBottom = getResources().getDrawable(R.drawable.bg_edit_text);
        mDrawableBottom.setBounds(0,0,mDrawableBottom.getIntrinsicWidth(),mDrawableBottom.getIntrinsicHeight());

        MyFocusChangeListener changeListener = new MyFocusChangeListener();
        mEtReply.setOnFocusChangeListener(changeListener);
    }

    @JavascriptInterface
    public void showBigPic(int index){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),SHowPicActivity.class);
        intent.putExtra(PIC_INDEX,index);
        intent.putExtra(PIC_LIST,mImgList);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(mIsEditTextFoucs){
            //请求焦点
            mWebView.requestFocus();
            InputUtil.closeSoftInput(NewsDetailActivity.this);
            return;
        }
        super.onBackPressed();
        //知道EditText焦点是否有拿到
    }

    private boolean mIsEditTextFoucs = false;

    private class MyFocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //如果有焦点
            if(hasFocus){
                mEtReply.setCompoundDrawables(null,null,null,mDrawableBottom);
                mIsEditTextFoucs =true;
                //就隐藏左边的图片,取消hint,隐藏右边的两个控件,展示发送控件出来
                mEtReply.setHint("");
                mTvReply.setVisibility(View.GONE);
                mIvShare.setVisibility(View.GONE);
                mTvSendReply.setVisibility(View.VISIBLE);
            }else{
                mEtReply.setCompoundDrawables(mDrawableLeft,null,null,mDrawableBottom);
                mIsEditTextFoucs =false;
                //没焦点
                //就展示左边的图片,展示hint,展示右边的两个控件,隐藏发送控件出来
                mEtReply.setHint("写跟帖");
                mTvReply.setVisibility(View.VISIBLE);
                mIvShare.setVisibility(View.VISIBLE);
                mTvSendReply.setVisibility(View.GONE);
            }


        }
    }
}
