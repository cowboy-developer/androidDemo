package com.m520it.www.newsreader.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xmg on 2017/1/2.
 */

//页面较少时,使用FragmentPagerAdapter
//页面较多时,使用FragmentStatePagerAdapter

    //前者: destory时是detach Fragment没有被销毁掉,只是取消关联
    //后者: destory时是remove Fragment对象被干掉了.
        //但是状态得到了保存,下次再创建一个新的Fragment对象时会将状态还原

public class NewsFragmentAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mFragments;
    private List<String> mTitles;

    public NewsFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments, List<String> titles) {
        super(fm);
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public void update(ArrayList<Fragment> fragments,List<String> titles){
        this.mFragments = fragments;
        this.mTitles = titles;
        notifyDataSetChanged();
    }
}
