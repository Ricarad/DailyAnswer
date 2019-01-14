package com.ricarad.app.dailyanswer.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by root on 2019-1-14.
 */

public class Post extends BmobObject {
    private String content;
    private User author;
    private Topic topic;
}
