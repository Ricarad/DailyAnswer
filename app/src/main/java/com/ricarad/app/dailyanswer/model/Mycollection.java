package com.ricarad.app.dailyanswer.model;

/**
 * Created by shy on 2019/1/14.
 */

public class Mycollection {
    //图片
    private int imgId;
    //标题
    private String username;
    //内容
    private String title;

    public Mycollection(int imgId, String username, String title) {

    }

    public int getImgId() {
        return imgId;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public void setImgId(int imgId) {

        this.imgId = imgId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
