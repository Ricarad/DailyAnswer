package com.ricarad.app.dailyanswer.model;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by shy on 2019/1/14.
 */

public class Mycollection {

    //图片
    private int userimg;
    //标题
    private String username;
    //内容
    private String collectedTopics;

    public Mycollection(int userimg, String username, String collectedTopics) {
        this.userimg = userimg;
        this.username = username;
        this.collectedTopics = collectedTopics;
    }


    public int getUserimg() {
        return userimg;
    }

    public void setUserimg(int userimg) {
        this.userimg = userimg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCollectedTopics() {
        return collectedTopics;
    }

    public void setCollectedTopics(String collectedTopics) {
        this.collectedTopics = collectedTopics;
    }
}
