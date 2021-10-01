package com.m520it.www.newsreader.event;

/**
 * Created by xmg on 2017/1/7.
 */

public class ShowTabHostEvent {
    boolean isShowTabHost;

    public ShowTabHostEvent(boolean isShowTabHost) {
        this.isShowTabHost = isShowTabHost;
    }

    public boolean isShowTabHost() {
        return isShowTabHost;
    }

    public void setShowTabHost(boolean showTabHost) {
        isShowTabHost = showTabHost;
    }
}
