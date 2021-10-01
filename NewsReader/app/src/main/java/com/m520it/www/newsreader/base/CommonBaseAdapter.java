package com.m520it.www.newsreader.base;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by xmg on 2017/1/3.
 */

//BaseAdapter的简单基类
    //泛型的声明位置   当前class名字的后面<T>
                     //如果是方法中的泛型,就在返回值的前面<T>
public abstract class CommonBaseAdapter<T> extends BaseAdapter{

    protected List<T> mDatas;

    public CommonBaseAdapter(List<T> datas){
        this.mDatas = datas;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
