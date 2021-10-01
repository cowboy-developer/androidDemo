package com.m520it.www.newsreader.util;

import android.content.Context;

/**
 * Created by xmg on 2017/1/7.
 */

public class TitleUtil {
    //获得状态栏的高度
    public static  int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
