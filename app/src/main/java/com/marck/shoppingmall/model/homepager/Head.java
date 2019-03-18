package com.marck.shoppingmall.model.homepager;

public class Head {
    private int id;

    private String name;

    private String imgUrl;

    private int type;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public String getImgUrl(){
        return this.imgUrl;
    }

    public void setType(int type){
        this.type = type;
    }

    public int getType(){
        return this.type;
    }
}
