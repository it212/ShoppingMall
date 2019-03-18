package com.marck.shoppingmall.model.homepager;

import java.util.List;

public class HomePagerData {
    private String ecode;

    private String emsg;

    private Data data;

    public void setEcode(String ecode){
        this.ecode = ecode;
    }

    public String getEcode(){
        return this.ecode;
    }

    public void setEmsg(String emsg){
        this.emsg = emsg;
    }

    public String getEmsg(){
        return this.emsg;
    }

    public void setData(Data data){
        this.data = data;
    }

    public Data getData(){
        return this.data;
    }

    public class Data {
        private List<Head> head ;

        private List<Recommend> recommend ;

        public void setHead(List<Head> head){
            this.head = head;
        }
        public List<Head> getHead(){
            return this.head;
        }
        public void setRecommend(List<Recommend> recommend){
            this.recommend = recommend;
        }
        public List<Recommend> getRecommend(){
            return this.recommend;
        }

    }
}
