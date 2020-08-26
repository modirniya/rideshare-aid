package com.modirniya.rideshareaid.modules;

public class Stat {
    private int excellent, good, slow, dead, total;

    public Stat() {
        this.excellent = 0;
        this.good = 0;
        this.slow = 0;
        this.dead = 0;
        this.total = 0;
    }

    public int getSlow() {
        return slow;
    }

    public void setSlow(int slow) {
        this.slow = slow;
    }

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getExcellent() {
        return excellent;
    }

    public void setExcellent(int excellent) {
        this.excellent = excellent;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }
}
