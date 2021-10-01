package com.m520it.www.newsreader.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.m520it.www.newsreader.bean.ArticleImageBean;
import com.m520it.www.newsreader.util.ImageUtil;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by xmg on 2017/1/6.
 */

public class BigPicAdapter extends PagerAdapter {

    private ArrayList<ArticleImageBean> mImgList;

    public BigPicAdapter(ArrayList<ArticleImageBean> imgList) {
        this.mImgList = imgList;

    }

    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
//        ImageView imageView = new ImageView(container.getContext());
        ImageUtil.getInstance().display(mImgList.get(position).getSrc(),photoView);
//        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
//        mAttacher.update();
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
