package com.m520it.www.newsreader.splash.bean;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by xmg on 2016/12/31.
 */

public class AdBean implements Serializable{
    ActionParams action_params;
    String[] res_url;

    @Override
    public String toString() {
        return "AdBean{" +
                "action_params=" + action_params +
                ", res_url=" + Arrays.toString(res_url) +
                '}';
    }

    public ActionParams getAction_params() {
        return action_params;
    }

    public void setAction_params(ActionParams action_params) {
        this.action_params = action_params;
    }

    public String[] getRes_url() {
        return res_url;
    }

    public void setRes_url(String[] res_url) {
        this.res_url = res_url;
    }
}
