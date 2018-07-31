package com.yang.cupwar.utils;

public class StarRecord {

    private int order;
    private int stars;

    @Override
    public String toString() {
        return new String("order：" + order + " stars：" + stars + "\n");
    }

    public StarRecord(int order, int stars) {
        this.order = order;
        this.stars = stars;
    }

    public StarRecord(StarRecord temp) {
        this.order = temp.order;
        this.stars = temp.stars;
    }


    public StarRecord() {
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
