package com.example.ec.bean;

/**
 * Created by HP on 2019/6/1.
 */

public class ResultBean {

    private String msg;
    private ResourceBean res;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResourceBean getRes() {
        return res;
    }

    public void setRes(ResourceBean res) {
        this.res = res;
    }



}
