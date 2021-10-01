package com.m520it.www.newsreader.http;

/**
 * Created by xmg on 2017/1/2.
 */

public interface HttpStringCallBack {
    //成功响应  可能需要一些请求到的String字符串做处理,写一个参数放在
    void onSuccessResponse(String result);
    //失败
    void onFail(Exception e);
}
