package com.m520it.www.newsreader.widget.banner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by xmg on 2017/1/3.
 */

public class BannerAdapter extends PagerAdapter {

    private ArrayList<ImageView> mImageViews;

    public BannerAdapter(ArrayList<ImageView> imgUrls) {
        this.mImageViews = imgUrls;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //每个页面都是一个ImageView
        ImageView imageView = mImageViews.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
//        return Integer.MAX_VALUE;
        return mImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    public void updateData(ArrayList<ImageView> imageViews) {
        mImageViews.clear();
        mImageViews.addAll(imageViews);
        notifyDataSetChanged();
    }
}
