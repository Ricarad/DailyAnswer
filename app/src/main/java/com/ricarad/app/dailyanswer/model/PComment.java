package com.ricarad.app.dailyanswer.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by root on 2019-1-14.
 */

public class PComment extends BmobObject {
    private String content;
    private Post post;
    private User author;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    @Override
    public String toString() {
        return "PComment{" +
                "content='" + content + '\'' +
                ", post=" + post +
                ", author=" + author +
                '}';
    }
}
