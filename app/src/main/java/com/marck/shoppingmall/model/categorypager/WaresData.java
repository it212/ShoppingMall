package com.marck.shoppingmall.model.categorypager;

import com.marck.shoppingmall.model.hotpager.Orders;

import java.util.List;

public class WaresData {
    private String copyright;

    private int totalCount;

    private int currentPage;

    private int totalPage;

    private int pageSize;

    private List<Orders> orders ;

    private List<Wares> list ;

    public void setCopyright(String copyright){
        this.copyright = copyright;
    }
    public String getCopyright(){
        return this.copyright;
    }
    public void setTotalCount(int totalCount){
        this.totalCount = totalCount;
    }
    public int getTotalCount(){
        return this.totalCount;
    }
    public void setCurrentPage(int currentPage){
        this.currentPage = currentPage;
    }
    public int getCurrentPage(){
        return this.currentPage;
    }
    public void setTotalPage(int totalPage){
        this.totalPage = totalPage;
    }
    public int getTotalPage(){
        return this.totalPage;
    }
    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }
    public int getPageSize(){
        return this.pageSize;
    }
    public void setOrders(List<Orders> orders){
        this.orders = orders;
    }
    public List<Orders> getOrders(){
        return this.orders;
    }
    public void setList(List<Wares> list){
        this.list = list;
    }
    public List<Wares> getList(){
        return this.list;
    }
}
