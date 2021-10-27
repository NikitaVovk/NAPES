package com.example.napes.runtime.domains.ports.stateflow;

public class StateFlow {
    String sName;
    String fName;

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    @Override
    public String toString() {
        return "StateFlow{" +
                "sName='" + sName + '\'' +
                ", fName='" + fName + '\'' +
                '}';
    }
}
