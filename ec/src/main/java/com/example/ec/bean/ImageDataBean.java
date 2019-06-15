package com.example.ec.bean;

import java.util.List;

/**
 * Created by HP on 2019/6/1.
 */

public class ImageDataBean {

    private String thumb;
    private String img;
    private String ncos;
    private String rank;
    private List<String> tag;
    private String id;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNcos() {
        return ncos;
    }

    public void setNcos(String ncos) {
        this.ncos = ncos;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
