package com.m520it.www.newsreader.splash.bean;

import java.io.Serializable;

/**
 * Created by xmg on 2016/12/31.
 */

public class ActionParams implements Serializable{
    String link_url;

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    @Override
    public String toString() {
        return "ActionParams{" +
                "link_url='" + link_url + '\'' +
                '}';
    }
}
