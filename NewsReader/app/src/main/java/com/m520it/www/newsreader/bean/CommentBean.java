package com.m520it.www.newsreader.bean;

/**
 * Created by xmg on 2017/1/6.
 */

public class CommentBean {
    String content;
    String createTime;
    int vote;
    DeviceInfoBean deviceInfo;
    UserBean user;

    @Override
    public String toString() {
        return "CommentBean{" +
                "content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", vote=" + vote +
                ", deviceInfo=" + deviceInfo +
                ", user=" + user +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public DeviceInfoBean getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfoBean deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
