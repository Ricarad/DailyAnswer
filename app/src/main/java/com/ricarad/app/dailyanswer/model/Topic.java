package com.ricarad.app.dailyanswer.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by root on 2019-1-14.
 */

public class Topic extends BmobObject {
    private String title;
    private String hostPostId;
    private User author;
    private Integer replyCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHostPostId() {
        return hostPostId;
    }

    public void setHostPostId(String hostPostId) {
        this.hostPostId = hostPostId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "title='" + title + '\'' +
                ", hostPostId='" + hostPostId + '\'' +
                ", author=" + author +
                ", replyCount=" + replyCount +
                '}';
    }
}
