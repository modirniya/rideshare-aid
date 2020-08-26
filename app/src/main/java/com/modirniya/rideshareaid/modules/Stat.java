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

    public int getDead() {
        return dead;
    }

    public int getExcellent() {
        return excellent;
    }

    public int getGood() {
        return good;
    }

}
