package com.ricarad.app.dailyanswer.model;

import java.util.List;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
    private String nickName; //昵称
    private Double rightRatio; //答题正确率
    private BmobFile userImg;//用户头像
    private Integer number;//答题数目
    private Integer days;//活跃天数
    private BmobDate lastLoginDate;//上次登录时间
    private Integer rightNumber; //回答正确的题目数量
    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Double getRightRatio() {
        return rightRatio;
    }

    public void setRightRatio(Double rightRatio) {
        this.rightRatio = rightRatio;
    }

    public BmobFile getUserImg() {
        return userImg;
    }

    public void setUserImg(BmobFile userImg) {
        this.userImg = userImg;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public BmobDate getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(BmobDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickName='" + nickName + '\'' +
                ", rightRatio=" + rightRatio +
                ", userImg=" + userImg +
                ", number=" + number +
                ", days=" + days +
                ", lastLoginDate=" + lastLoginDate.getDate() +
                '}';
    }
}
