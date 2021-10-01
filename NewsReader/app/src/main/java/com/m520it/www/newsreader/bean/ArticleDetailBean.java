package com.m520it.www.newsreader.bean;

import java.util.List;

/**
 * Created by xmg on 2017/1/4.
 */

public class ArticleDetailBean {
    String title;
    String source;
    String articleTags;
    String ptime;
    String body;
    List<ArticleImageBean> img;
    int replyCount;

    @Override
    public String toString() {
        return "ArticleDetailBean{" +
                "title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", articleTags='" + articleTags + '\'' +
                ", ptime='" + ptime + '\'' +
                ", body='" + body + '\'' +
                ", img=" + img +
                ", replyCount=" + replyCount +
                '}';
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

    public String getArticleTags() {
        return articleTags;
    }

    public void setArticleTags(String articleTags) {
        this.articleTags = articleTags;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<ArticleImageBean> getImg() {
        return img;
    }

    public void setImg(List<ArticleImageBean> img) {
        this.img = img;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }
}
