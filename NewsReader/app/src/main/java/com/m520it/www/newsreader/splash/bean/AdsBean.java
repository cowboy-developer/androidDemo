package com.m520it.www.newsreader.splash.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xmg on 2016/12/31.
 */

public class AdsBean implements Serializable{


    List<AdBean> ads;
    int next_req;

    @Override
    public String toString() {
        return "AdsBean{" +
                "ads=" + getadsListText() +
                ", next_req=" + next_req +
                '}';
    }

    private String getadsListText() {
        StringBuilder stringBuilder = new StringBuilder();
        if(ads!=null){
            for (int i = 0; i < ads.size(); i++) {
                AdBean adBean = ads.get(i);
                stringBuilder.append(adBean.toString());
            }
        }
        return stringBuilder.toString();
    }

    public List<AdBean> getAds() {
        return ads;
    }

    public void setAds(List<AdBean> ads) {
        this.ads = ads;
    }

    public int getNext_req() {
        return next_req;
    }

    public void setNext_req(int next_req) {
        this.next_req = next_req;
    }
}
