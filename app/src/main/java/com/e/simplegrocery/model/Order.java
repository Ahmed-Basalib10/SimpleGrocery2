package com.e.simplegrocery.model;

public class Order {
    private String userId;
    private String addrss;
    private Object tinmdate;
    private String state;
    private String total;
    private String orderId;

    public Order() {
    }

    public Order(String userId, String addrss, Object tinmdate, String state,String total,String orderId) {
        this.userId = userId;
        this.addrss = addrss;
        this.tinmdate = tinmdate;
        this.state = state;
        this.total = total;
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddrss() {
        return addrss;
    }

    public void setAddrss(String addrss) {
        this.addrss = addrss;
    }

    public Object getTinmdate() {
        return tinmdate;
    }

    public void setTinmdate(Object tinmdate) {
        this.tinmdate = tinmdate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
