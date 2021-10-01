package com.m520it.www.newsreader.bean;

/**
 * Created by xmg on 2017/1/6.
 */

public class DeviceInfoBean {
    String deviceName;

    @Override
    public String toString() {
        return "DeviceInfoBean{" +
                "deviceName='" + deviceName + '\'' +
                '}';
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
