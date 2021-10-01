package com.m520it.www.newsreader.news.inner.bean;

import java.util.List;

/**
 * Created by xmg on 2017/1/3.
 */

public class NewsDetailBean {
    List<BannerBean> ads;//轮播图
    String imgsrc;//图片地址
    String title;//标题
    String source;//新闻来源
    String replyCount;//跟帖数
    String docid;//新闻的id,用来标记当前是哪条新闻,方便后面展示详情
    String specialID;//是否为专题,我这里简单处理,认为有specialID,就是置顶帖

    @Override
    public String toString() {
        return "NewsDetailBean{" +
                "ads=" + getListText() +
                ", imgsrc='" + imgsrc + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", replyCount='" + replyCount + '\'' +
                ", docid='" + docid + '\'' +
                ", specialID='" + specialID + '\'' +
                '}';
    }

    private String getListText() {
        StringBuilder stringBuilder = new StringBuilder();
        if(ads!=null){
            for (int i = 0; i < ads.size(); i++) {
                stringBuilder.append(ads.get(i));
            }
        }
        return stringBuilder.toString();
    }

    public List<BannerBean> getAds() {
        return ads;
    }

    public void setAds(List<BannerBean> ads) {
        this.ads = ads;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getSpecialID() {
        return specialID;
    }

    public void setSpecialID(String specialID) {
        this.specialID = specialID;
    }
}
