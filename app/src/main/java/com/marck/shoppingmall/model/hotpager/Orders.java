package com.marck.shoppingmall.model.hotpager;

public class Orders {

    private String orderType;
    private String field;

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getField() {
        return field;
    }
}
