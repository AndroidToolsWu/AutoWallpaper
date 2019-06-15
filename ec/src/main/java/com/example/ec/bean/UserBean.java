package com.example.ec.bean;

import java.io.Serializable;

/**
 * Created by HP on 2019/3/20.
 */

public class UserBean implements Serializable{

    private int uid;
    private String phoneNumber;
    private String nickName;
    private int headImg;

    public UserBean(int uid,String phoneNumber,String nickName,int headImg){
        this.uid=uid;
        this.phoneNumber=phoneNumber;
        this.nickName=nickName;
        this.headImg=headImg;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getHeadImg() {
        return headImg;
    }

    public void setHeadImg(int headImg) {
        this.headImg = headImg;
    }


}
