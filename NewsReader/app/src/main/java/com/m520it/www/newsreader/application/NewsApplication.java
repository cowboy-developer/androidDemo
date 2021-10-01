package com.m520it.www.newsreader.application;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by xmg on 2017/1/3.
 */

public class NewsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Create global configuration and initialize ImageLoader with this config
        File cacheDir = getExternalCacheDir();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                //后面再来做配置
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
        .build();
        ImageLoader.getInstance().init(config);

        //推送JPUSH激光
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
