package com.m520it.www.newsreader.util;

import android.widget.ImageView;

import com.m520it.www.newsreader.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xmg on 2017/1/3.
 */

public class ImageUtil {

    private  ImageLoader mImageLoader;
    private  DisplayImageOptions mOptions;

    private int mDefaultPicId = R.drawable.icon_default;
    private final DisplayImageOptions mNoLoadingOptions;

    private ImageUtil(){
        mImageLoader = ImageLoader.getInstance();
        // 展示一个默认图片
        // 是否要进行内存缓存
        // 是否要进行磁盘缓存
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(mDefaultPicId) // 展示一个默认图片
                .cacheInMemory(true) // 是否要进行内存缓存
                .cacheOnDisk(true) // 是否要进行磁盘缓存
                .build();
        // 是否要进行内存缓存
        // 是否要进行磁盘缓存
        mNoLoadingOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // 是否要进行内存缓存
                .cacheOnDisk(true) // 是否要进行磁盘缓存
                .build();
    }

    private static ImageUtil sImageUtil;

    public static ImageUtil getInstance(){
        if(sImageUtil==null){
            synchronized (ImageUtil.class){
                if(sImageUtil==null){
                    sImageUtil = new ImageUtil();
                }
            }
        }
        return sImageUtil;
    }

    public void display(String imgUrl, ImageView imageView){
        display(imgUrl,imageView,R.drawable.icon_default);
    }


    public void displayNoLoading(String imgUrl, ImageView imageView) {
        //如果传过来的默认图片跟先前不一致,就重新给option赋值,让新的设置生效
        mImageLoader.displayImage(imgUrl, imageView, mNoLoadingOptions);
    }
    //加载图片
    public void display(String imgUrl, ImageView imageView, int picRes) {
        //如果传过来的默认图片跟先前不一致,就重新给option赋值,让新的设置生效
        if(mDefaultPicId!=picRes){
            mDefaultPicId = picRes;
            mOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(mDefaultPicId) // 展示一个默认图片
                    .cacheInMemory(true) // 是否要进行内存缓存
                    .cacheOnDisk(true) // 是否要进行磁盘缓存
                    .build();
        }
        mImageLoader.displayImage(imgUrl, imageView, mOptions);
    }
}
