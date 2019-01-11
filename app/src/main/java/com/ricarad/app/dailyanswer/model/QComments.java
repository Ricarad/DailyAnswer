package com.ricarad.app.dailyanswer.model;

import cn.bmob.v3.BmobObject;

public class QComments extends BmobObject {
    private String content;//评论内容
    private User user;//评论的用户，在BMOB里是Pointer类型，一对一
    private Question question;//评论的问题，一个评论只能属于一个问题

    public QComments() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
