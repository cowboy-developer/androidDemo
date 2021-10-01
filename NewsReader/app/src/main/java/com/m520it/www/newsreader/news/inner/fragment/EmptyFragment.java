package com.m520it.www.newsreader.news.inner.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by xmg on 2017/1/2.
 */

//这个是新闻NewsFragment中的内部Fragment,展示的是头条新闻
public class EmptyFragment extends Fragment{
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setText("空空如也得一个Fragment");
        textView.setTextSize(40);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
