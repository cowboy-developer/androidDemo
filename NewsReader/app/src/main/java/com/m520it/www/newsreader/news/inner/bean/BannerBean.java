package com.m520it.www.newsreader.news.inner.bean;

/**
 * Created by xmg on 2017/1/3.
 */
//Banner 认为是轮播图
public class BannerBean {
    String imgsrc;
    String title;
    String url;

    @Override
    public String toString() {
        return "BannerBean{" +
                "imgsrc='" + imgsrc + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
