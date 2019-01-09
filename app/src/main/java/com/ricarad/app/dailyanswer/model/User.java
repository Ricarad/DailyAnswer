package com.ricarad.app.dailyanswer.model;

import java.util.List;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
    private String nickName; //昵称
    private List<String> questionAnswered;//回答过的题目
    private Double rightRatio; //答题正确率
    private List<String> myQuestions; //自己出过的题目
    private List<String> questionCollected;//收藏的题目
    private BmobFile userImg;//用户头像

    public String getNickName() {
        return nickName;
    }

    public User setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public List<String> getQuestionAnswered() {
        return questionAnswered;
    }

    public User setQuestionAnswered(List<String> questionAnswered) {
        this.questionAnswered = questionAnswered;
        return this;
    }

    public Double getRightRatio() {
        return rightRatio;
    }

    public User setRightRatio(Double rightRatio) {
        this.rightRatio = rightRatio;
        return this;
    }

    public List<String> getMyQuestions() {
        return myQuestions;
    }

    public User setMyQuestions(List<String> myQuestions) {
        this.myQuestions = myQuestions;
        return this;
    }

    public List<String> getQuestionCollected() {
        return questionCollected;
    }

    public User setQuestionCollected(List<String> questionCollected) {
        this.questionCollected = questionCollected;
        return this;
    }

    public BmobFile getUserImg() {
        return userImg;
    }

    public User setUserImg(BmobFile userImg) {
        this.userImg = userImg;
        return this;
    }
}
