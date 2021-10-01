package com.m520it.www.newsreader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.adapter.BigPicAdapter;
import com.m520it.www.newsreader.bean.ArticleImageBean;

import java.util.ArrayList;

public class SHowPicActivity extends AppCompatActivity {

    public static final String PIC_INDEX = "pic_index";
    public static final String PIC_LIST = "pic_list";

    private RelativeLayout mActivityShowPic;
    private TextView mTvIndex;
    private TextView mTvTotal;
    private ViewPager mViewPagerShowPic;
    private ArrayList<ArticleImageBean> mImgList;
    private int mIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        Intent intent = getIntent();
        mImgList = (ArrayList<ArticleImageBean>) intent.getSerializableExtra(PIC_LIST);
        mIndex = intent.getIntExtra(PIC_INDEX,0);
        Log.e("xmg", "onCreate: "+ mImgList.size()+" index " + mIndex);
        initView();
        initData();
    }

    private void initData() {
        BigPicAdapter bigPicAdapter = new BigPicAdapter(mImgList);
        mViewPagerShowPic.setAdapter(bigPicAdapter);
        //还得让下面的文本发生变化
        mTvIndex.setText(String.valueOf(mIndex+1));
        mTvTotal.setText("/"+mImgList.size());
        mViewPagerShowPic.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvIndex.setText(String.valueOf(position+1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPagerShowPic.setCurrentItem(mIndex);
    }

    private void initView() {
        mActivityShowPic = (RelativeLayout) findViewById(R.id.activity_show_pic);
        mTvIndex = (TextView) findViewById(R.id.tv_index);
        mTvTotal = (TextView) findViewById(R.id.tv_total);
        mViewPagerShowPic = (ViewPager) findViewById(R.id.viewPager_show_pic);
    }
}
