package com.m520it.www.newsreader.http;

import android.content.Context;
import android.os.Handler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xmg on 2017/1/2.
 */

public class HttpHelper {

    private final OkHttpClient mOkHttpClient;
    private final Handler mHandler;

    private HttpHelper(Context context){
        mOkHttpClient = new OkHttpClient();

        //创建一个Handler,为了能够确保在主线程中handler
            //Looper  消息轮询器   参数当中,如果传递了一个Loop过来,那么这个Looper在哪个线程,这个Handler就跟哪个Thread通信
                //使用context.getMainLooper() 强制让handler与主线程通信
        mHandler = new Handler(context.getMainLooper());
    }

    private static HttpHelper sHttpHelper;

    public static HttpHelper getInstance(Context context){
        if(sHttpHelper==null){
            synchronized (HttpHelper.class){
                if(sHttpHelper==null){
                    sHttpHelper = new HttpHelper(context);
                }
            }
        }
        return sHttpHelper;
    }

    //Handler 在主线程创建  在哪个线程中创建,调起该handler就是在该那个线程进行通信

    //写一个请求方法   返回的数据为String类型的get请求
    public void requestGETStringResult(String url, final HttpStringCallBack callBack){
        //共性一:请求地址  不管用说明方式来请求都需要
        //
        //共性二:请求的结果(开启线程的写法 都是异步的请求)
        //没有返回值
        //怎么做?使用监听
        // 当请求成功  执行成功的方法
        // 失败的时候 执行失败的方法

        //准备request
        Request request = new Request.Builder()
                .url(url)
                .build();
        //调用请求方法
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //失败了,就调用咱们定义的监听器回调接口的方法
                callBack.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功了,就调用咱们定义的监听器回调接口的方法
                final String result = response.body().string();
                //让监听器回调接口的方法在主线程中运行
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccessResponse(result);
                    }
                });

            }
        });
    }
}
