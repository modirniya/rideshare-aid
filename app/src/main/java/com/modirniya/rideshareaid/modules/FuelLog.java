package com.modirniya.rideshareaid.modules;

public class FuelLog {
    private int Mileage;
    private String id, Time, Price, Memo, Tag;

    public FuelLog() {
    }

    public FuelLog(String time, String tag, int mileage, String price, String memo) {
        this.Mileage = mileage;
        this.Time = time;
        this.Tag = tag;
        this.Price = price;
        this.Memo = memo;
    }

    public FuelLog(String id, String time, String tag, int mileage, String price, String memo) {
        this.id = id;
        this.Mileage = mileage;
        this.Time = time;
        this.Tag = tag;
        this.Price = price;
        this.Memo = memo;
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
