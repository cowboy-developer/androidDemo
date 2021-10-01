package com.m520it.www.newsreader.util;

/**
 * Created by xmg on 2016/12/31.
 */

public class HashHelper {
    public static String toHashCode(String url){
        int hashCode = url.hashCode();
//        return hashCode+"";
        return String.valueOf(hashCode);
    }
}
