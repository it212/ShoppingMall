package com.marck.shoppingmall.model.categorypager;

public class Wares {
    private int id;

    private int categoryId;

    private int campaignId;

    private String name;

    private String imgUrl;

    private double price;

    private int sale;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setCategoryId(int categoryId){
        this.categoryId = categoryId;
    }
    public int getCategoryId(){
        return this.categoryId;
    }
    public void setCampaignId(int campaignId){
        this.campaignId = campaignId;
    }
    public int getCampaignId(){
        return this.campaignId;
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
    public void setPrice(double price){
        this.price = price;
    }
    public double getPrice(){
        return this.price;
    }
    public void setSale(int sale){
        this.sale = sale;
    }
    public int getSale(){
        return this.sale;
    }
}
