package com.ricarad.app.dailyanswer.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by root on 2019-1-14.
 */

public class Topic extends BmobObject {
    private String title;
    private String hostPostId;

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

    @Override
    public String toString() {
        return "Topic{" +
                "title='" + title + '\'' +
                ", hostPostId='" + hostPostId + '\'' +
                '}';
    }
}
