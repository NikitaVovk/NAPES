package com.example.napes.runtime.domains.flows;

public class Flow {
    String fName;
    String fType;
    int timeParam;
    int fParametr;
    String unit;
    long realTimeDelay;

    public long getRealTimeDelay() {
        return realTimeDelay;
    }

    public void setRealTimeDelay(long realTimeDelay) {
        this.realTimeDelay = realTimeDelay;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public int getTimeParam() {
        return timeParam;
    }

    public void setTimeParam(int timeParam) {
        this.timeParam = timeParam;
    }

    public int getfParametr() {
        return fParametr;
    }

    public void setfParametr(int fParametr) {
        this.fParametr = fParametr;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "\n\tFlow{" +
                "fName='" + fName + '\'' +
                ", fType='" + fType + '\'' +
                ", timeParam=" + timeParam +
                ", fParametr=" + fParametr +
                ", unit='" + unit + '\'' +
                '}';
    }
}
