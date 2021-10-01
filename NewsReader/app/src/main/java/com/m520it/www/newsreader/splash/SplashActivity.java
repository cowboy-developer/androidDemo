package com.m520it.www.newsreader.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.m520it.www.newsreader.MainActivity;
import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.splash.bean.AdBean;
import com.m520it.www.newsreader.splash.bean.AdsBean;
import com.m520it.www.newsreader.splash.service.DownloadIntentService;
import com.m520it.www.newsreader.util.Constant;
import com.m520it.www.newsreader.util.HashHelper;
import com.m520it.www.newsreader.util.JsonUtil;
import com.m520it.www.newsreader.util.SharedPreferenceUtil;
import com.m520it.www.newsreader.widget.SkipView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private ImageView mIv_ad;
    private OkHttpClient mClient;
    private SkipView mSkipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();

    }

    private void initData() {
        //读取缓存,看有没有缓存,并且缓存的json没有超过有效期
        String ads_json = SharedPreferenceUtil.getString(getApplicationContext(),
                SharedPreferenceUtil.ADS_JSON);
        long ads_end_time = SharedPreferenceUtil.getLong(getApplicationContext(),
                SharedPreferenceUtil.ADS_END_TIME);
        //判断
        if (TextUtils.isEmpty(ads_json) || System.currentTimeMillis() > ads_end_time) {
            //请求网络拿json
            mClient = new OkHttpClient();
            requestJson();
        }else{
            Log.e("xmg", "initData: 有缓存,且缓存没有超过有效期");
        }

        //加载图片
        showPic();
    }


    private void showPic() {
        //要拿图片文件→需要图片的url地址→需要javaBean→需要那段生成javaBean的json→SP缓存中
        //先判断是否有缓存
        String ads_json = SharedPreferenceUtil.getString(getApplicationContext(),
                SharedPreferenceUtil.ADS_JSON);
        if (TextUtils.isEmpty(ads_json)) {
            //过个几秒就跳转页面了  第一次安装应用时就会出现该情况
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainPage();
                }
            },3000);

            return;
        }
        //加载图片
        //生成javaBean
        AdsBean adsBean = JsonUtil.parseJson(ads_json, AdsBean.class);
        //Url地址
        List<AdBean> ads = adsBean.getAds();

        //准备一个index 保存图片展示的顺序,避免总是展示同样的图片
        //从SP缓存中将先前图片展示的位置index读取出来
        int index = SharedPreferenceUtil.getInt(getApplicationContext(),SharedPreferenceUtil.ADS_PIC_INDEX);
        Log.e("xmg", "showPic: index "+ index + " ads.size " + ads.size());
        //先为了看效果,默认使用第一个广告图片
        final AdBean adBean = ads.get(index);
        //图片的地址拿到了
        String resUrl = adBean.getRes_url()[0];
        //图片的文件名
        File file = new File(getExternalCacheDir(), HashHelper.toHashCode(resUrl) + ".jpg");
        //如果存在这个图片,就展示ImageView上
        if (file.exists() && file.length() > 0) {
            //存在
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mIv_ad.setImageBitmap(bitmap);
            //开始自动读秒
            mSkipView.startAutoSkip(500);
            //设置图片控件的点击事件
            mIv_ad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),AdWebViewActivity.class);
                    String link_url = adBean.getAction_params().getLink_url();
                    intent.putExtra(AdWebViewActivity.AD_LINK_URL,link_url);
                    startActivity(intent);
                }
            });
            //让Index指向下一张
            index++;
            index = index%ads.size();
//            if(index == ads.size()){
//                index = 0;
//            }
            //把它存到SP里面
            SharedPreferenceUtil.putInt(getApplicationContext(),
                    SharedPreferenceUtil.ADS_PIC_INDEX,index);
        }
    }

    //1 OKHttpClient
    //2 创建 request
    //3 client.newCall(request).enqueue(new Callback()
    //Android 保存数据 方式有:SP 数据库 ContentProvider 网络 文件
    private void requestJson() {
//        OKHttp

        Request request = new Request.Builder()
                .url(Constant.ADS_JSON_URL)
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("xmg", "请求失败了:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //看清楚,不是toString()
                String result = response.body().string();
//                LogUtil.e("xmg",string);
                //GSON
                AdsBean adsBean = JsonUtil.parseJson(result, AdsBean.class);
//                Log.e("xmg", "onResponse: adsBean " + adsBean);
                //如果javaBean是有数据的,开始使用后台Service进行图片下载
                if (adsBean != null) {
                    Intent intent = new Intent();
                    intent.putExtra(DownloadIntentService.DOWNLOAD_AD_BEAN, adsBean);
                    intent.setClass(getApplicationContext(), DownloadIntentService.class);
                    startService(intent);
                }
                //有效期计算一下
                int next_req = adsBean.getNext_req();//认为是分钟单位
                long endTime = System.currentTimeMillis() + next_req * 60 * 1000;

                //缓存的处理,将json字符串存起来
                SharedPreferenceUtil.putString(getApplicationContext(),
                        SharedPreferenceUtil.ADS_JSON, result);

                SharedPreferenceUtil.putLong(getApplicationContext(),
                        SharedPreferenceUtil.ADS_END_TIME, endTime);
                //原生
//                try {
//                    JSONObject jsonObject = new JSONObject(string);
//                    JSONArray adsArray = jsonObject.optJSONArray("ads");
//                    int length = adsArray.length();
//                    for (int i = 0; i < length; i++) {
//                        JSONObject ads = adsArray.getJSONObject(i);
//                        //获得link_url
//                        JSONObject action_params = ads.optJSONObject("action_params");
//                        String link_url = action_params.optString("link_url");
//                        Log.e("xmg", "onResponse: link_url" + link_url);
//                        //获得图片req_url
//                        JSONArray res_urlArray = ads.optJSONArray("res_url");
//                        String req_url = res_urlArray.getString(0);
//                        Log.e("xmg", "onResponse: req_url" + req_url);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //请求
//                try {
//                    URL url = new URL(Constant.ADS_JSON_URL);
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.setConnectTimeout(5000);
//                    int code = urlConnection.getResponseCode();
//                    if(code==200){
//                        //有数据
//                        InputStream inputStream = urlConnection.getInputStream();
//
//                        //inputStream转String
//                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//                        byte[] buffer = new byte[1024];
//                        int count = 0;
//                        while((count = inputStream.read(buffer))!=-1){
//                            bos.write(buffer,0,count);
//                        }
//                        String str = new String(bos.toByteArray());
//                        LogUtil.e("xmg",str);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    private void initView() {
        mIv_ad = (ImageView) findViewById(R.id.iv_ad);
        mSkipView = (SkipView) findViewById(R.id.skipView);
        mSkipView.setOnSkipListener(new SkipView.OnSkipListener() {
            @Override
            public void onSkip() {
                //跳转页面
                startMainPage();
            }
        });
    }

    public void startMainPage(){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
