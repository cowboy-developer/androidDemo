package com.m520it.www.newsreader.util;

import com.google.gson.Gson;

/**
 * Created by xmg on 2016/12/31.
 */

public class JsonUtil {


    public static <T> T parseJson(String ads_json,Class<T> clz){
        Gson gson = new Gson();
        T adsBean = gson.fromJson(ads_json, clz);
        return adsBean;
    }

//    public static 阿猫阿狗 parseJson(String ads_json){
//        Gson gson = new Gson();
//        阿猫阿狗 adsBean = gson.fromJson(ads_json, 阿猫阿狗.class);
//        return adsBean;
//    }
//
//    public static 阿狗阿猫 parseJson(String ads_json){
//        Gson gson = new Gson();
//        阿狗阿猫 adsBean = gson.fromJson(ads_json, 阿狗阿猫.class);
//        return adsBean;
//    }
}
