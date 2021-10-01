package com.m520it.www.newsreader.bean;

import java.io.Serializable;

/**
 * Created by xmg on 2017/1/4.
 */

public class ArticleImageBean implements Serializable{
    String ref;
    String src;

    @Override
    public String toString() {
        return "ArticleImageBean{" +
                "ref='" + ref + '\'' +
                ", src='" + src + '\'' +
                '}';
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
