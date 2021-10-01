package com.m520it.www.newsreader.splash.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.m520it.www.newsreader.splash.bean.AdBean;
import com.m520it.www.newsreader.splash.bean.AdsBean;
import com.m520it.www.newsreader.util.HashHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadIntentService extends IntentService {
    //Ctrl + Shift + U 大小写转换
    public static final String DOWNLOAD_AD_BEAN = "download_ad_bean";
    private OkHttpClient mOkHttpClient;

    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    //执行一些后台任务
    @Override
    protected void onHandleIntent(Intent intent) {
        //方法中逻辑都会执行在非UI线程中
        String name = Thread.currentThread().getName();
        Log.e("xmg", "onHandleIntent: name " + name);

        //接受传递过来的数据

        if (intent != null) {
            AdsBean adsBean = (AdsBean) intent.getSerializableExtra(DOWNLOAD_AD_BEAN);
            Log.e("xmg", "onHandleIntent: adsBean" + adsBean);
            mOkHttpClient = new OkHttpClient();
            //下载图片
            List<AdBean> ads = adsBean.getAds();
            for (int i = 0; i < ads.size(); i++) {
                AdBean adBean = ads.get(i);
                String resUrl = adBean.getRes_url()[0];

                //判断一下该图片是否已经存在
                File file = new File(getExternalCacheDir(), HashHelper.toHashCode(resUrl)+".jpg");
                if(file.exists()&&file.length()>0){
                    //继续下一个for循环
                    Log.e("xmg", "onHandleIntent: 图片已经有了,不再去下载");
                    continue;
                }
                //进行下载picture图片
                downPic(resUrl);
            }
        }
    }

    private void downPic(final String resUrl) {
        //准备Request对象
        Request request = new Request.Builder()
                .url(resUrl)
                .build();
        //请求方法
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("xmg", "onFailure: 图片下载失败: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //有响应,把响应的数据转成一个图片
                InputStream inputStream = response.body().byteStream();

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //输出流准备一下,把图片保存到SD卡
                //getExternalCacheDir() 保存SD卡的cache目录(mnt/sdcard/Android/data/自己包名),当应用卸载时,SD卡上的该目录的数据也会被干掉
                                        //如果手机没有SD卡,那么该方法==getCacheDir()
                //getCacheDir()     //data/data/自己的包名/cache
                //http://img1.126.net/channel6/2016/023518/1.jpg
                //注意加SD卡权限!
                File file = new File(getExternalCacheDir(), HashHelper.toHashCode(resUrl)+".jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);

                Log.e("xmg", "onResponse: 下载成功,并存储在本地磁盘中");
            }
        });
    }


}
