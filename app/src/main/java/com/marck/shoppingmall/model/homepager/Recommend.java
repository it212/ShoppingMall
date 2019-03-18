package com.marck.shoppingmall.model.homepager;

public class Recommend {

    private CpOne cpOne;

    private CpTwo cpTwo;

    private CpThree cpThree;

    private int id;

    private String title;

    private int campaignOne;

    private int campaignTwo;

    private int campaignThree;

    public void setCpOne(CpOne cpOne){
        this.cpOne = cpOne;
    }

    public CpOne getCpOne(){
        return this.cpOne;
    }

    public void setCpTwo(CpTwo cpTwo){
        this.cpTwo = cpTwo;
    }

    public CpTwo getCpTwo(){
        return this.cpTwo;
    }

    public void setCpThree(CpThree cpThree){
        this.cpThree = cpThree;
    }

    public CpThree getCpThree(){
        return this.cpThree;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setCampaignOne(int campaignOne){
        this.campaignOne = campaignOne;
    }

    public int getCampaignOne(){
        return this.campaignOne;
    }

    public void setCampaignTwo(int campaignTwo){
        this.campaignTwo = campaignTwo;
    }

    public int getCampaignTwo(){
        return this.campaignTwo;
    }

    public void setCampaignThree(int campaignThree){
        this.campaignThree = campaignThree;
    }

    public int getCampaignThree(){
        return this.campaignThree;
    }

}
