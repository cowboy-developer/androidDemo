package com.m520it.www.newsreader.news.inner.bean;

import java.util.List;

/**
 * Created by xmg on 2017/1/3.
 */

public class HotNewsBean {
    List<NewsDetailBean> T1348647909107;

    public List<NewsDetailBean> getT1348647909107() {
        return T1348647909107;
    }

    public void setT1348647909107(List<NewsDetailBean> t1348647909107) {
        T1348647909107 = t1348647909107;
    }

    @Override
    public String toString() {
        return "HotNewsBean{" +
                "T1348647909107=" + getListText() +
                '}';
    }

    private String getListText() {
        StringBuilder stringBuilder = new StringBuilder();
        if(T1348647909107!=null){
            for (int i = 0; i < T1348647909107.size(); i++) {
                stringBuilder.append(T1348647909107.get(i));
            }
        }
        return stringBuilder.toString();
    }
}
