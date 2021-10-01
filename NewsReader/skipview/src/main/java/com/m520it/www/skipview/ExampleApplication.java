package com.m520it.www.skipview;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}