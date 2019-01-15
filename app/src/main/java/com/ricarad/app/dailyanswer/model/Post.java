package com.ricarad.app.dailyanswer.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by root on 2019-1-14.
 */

public class Post extends BmobObject {
    private String content;
    private User author;
    private Topic topic;
    private Integer replyCount;

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Post{" +
                "content='" + content + '\'' +
                ", author=" + author +
                ", topic=" + topic +
                ", replyCount=" + replyCount +
                '}';
    }
}
