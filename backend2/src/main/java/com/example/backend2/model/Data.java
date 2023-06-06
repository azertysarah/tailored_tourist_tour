package com.example.backend2.model;

public class Data {
    private String monumentName;
    private String region;
    private String period;
    private int time;

    private boolean needRealTime;

    public String getMonumentName() {
        return monumentName;
    }

    public void setMonumentName(String monumentName) {
        this.monumentName = monumentName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isNeedRealTime() {
        return needRealTime;
    }

    public void setNeedRealTime(boolean needRealTime) {
        this.needRealTime = needRealTime;
    }
}
