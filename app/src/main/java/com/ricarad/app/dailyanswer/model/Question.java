package com.ricarad.app.dailyanswer.model;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

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
    private User author; //问题的上传者
    private BmobRelation collected;  //存储所有收藏该问题的人
    private BmobRelation answeredUser;  //回答过该题目的用户
    private BmobRelation mistakeUser; //做错该题目的人

    public Question() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobFile getQuestionImg() {
        return questionImg;
    }

    public void setQuestionImg(BmobFile questionImg) {
        this.questionImg = questionImg;
    }

    public String getItemA() {
        return itemA;
    }

    public void setItemA(String itemA) {
        this.itemA = itemA;
    }

    public String getItemB() {
        return itemB;
    }

    public void setItemB(String itemB) {
        this.itemB = itemB;
    }

    public String getItemC() {
        return itemC;
    }

    public void setItemC(String itemC) {
        this.itemC = itemC;
    }

    public String getItemD() {
        return itemD;
    }

    public void setItemD(String itemD) {
        this.itemD = itemD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public BmobRelation getCollected() {
        return collected;
    }

    public void setCollected(BmobRelation collected) {
        this.collected = collected;
    }

    public BmobRelation getAnsweredUser() {
        return answeredUser;
    }

    public void setAnsweredUser(BmobRelation answeredUser) {
        this.answeredUser = answeredUser;
    }

    public BmobRelation getMistakeUser() {
        return mistakeUser;
    }

    public void setMistakeUser(BmobRelation mistakeUser) {
        this.mistakeUser = mistakeUser;
    }
}
