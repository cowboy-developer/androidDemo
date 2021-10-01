package com.m520it.www.newsreader.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xmg on 2016/12/31.
 */

public class SharedPreferenceUtil {

    public static final String SP_FILE_NAME="cache";
    public static final String ADS_JSON="ads_json";
    public static final String ADS_END_TIME="ads_end_time";
    public static final String ADS_PIC_INDEX="ads_pic_index";
    public static final String SHOW_TITLE_DATA="show_title_data";
    public static final String TO_ADD_TITLE_DATA="to_add_title_data";

    public static void putString(Context context,String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key,value);
        //commit 同步的提交,有返回值
//        edit.commit();
        //apply 异步的提交,没有返回值
        edit.apply();
    }

    public static void putLong(Context context,String key,long value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(key,value);
        //commit 同步的提交,有返回值
//        edit.commit();
        //apply 异步的提交,没有返回值
        edit.apply();
    }

    public static void putInt(Context context,String key,int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key,value);
        //commit 同步的提交,有返回值
//        edit.commit();
        //apply 异步的提交,没有返回值
        edit.apply();
    }


    public static String getString(Context context,String key){
        return getString(context,key,"");
    }

    public static String getString(Context context,String key,String defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME,
                Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(key, defValue);
        return string;
    }

    public static long getLong(Context context,String key){
        return getLong(context,key,0);
    }

    public static long getLong(Context context,String key,long defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME,
                Context.MODE_PRIVATE);
        long string = sharedPreferences.getLong(key, defValue);
        return string;
    }

    public static int getInt(Context context,String key){
        return getInt(context,key,0);
    }

    public static int getInt(Context context,String key,int defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME,
                Context.MODE_PRIVATE);
        int string = sharedPreferences.getInt(key, defValue);
        return string;
    }
}
