package com.ricarad.app.dailyanswer.model;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Question extends BmobObject {
    private String title;//标题-题干
    private BmobFile questionImg;//问题图片，可有可无
    private String itemA;
    private String itemB;
    private String itemC;
    private String itemD;
    private String answer;//答案
    private String analysis;//解析
    private String flag;//标签-分类
    private List<String> comment;//评论列表---存放


}
