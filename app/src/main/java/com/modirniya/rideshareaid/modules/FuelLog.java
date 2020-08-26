package com.modirniya.rideshareaid.modules;

public class FuelLog {
    private int Mileage;
    private String id, Time, Price, Memo, Tag;

    public FuelLog() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMileage(int mileage) {
        Mileage = mileage;
    }

    public int getMileage() {
        return Mileage;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTime() {
        return Time;
    }

    public void setCost(String price) {
        Price = price;
    }

    public String getCost() {
        return Price;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public String getMemo() {
        return Memo;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
