package com.example.napes.runtime.domains.ports.stateflow;

import com.example.napes.runtime.domains.flows.FlowList;

public class StateFlow {
    String sName;
    String fName;
    FlowList anonFlow;

    public FlowList getAnonFlow() {
        return anonFlow;
    }

    public void setAnonFlow(FlowList anonFlow) {
        this.anonFlow = anonFlow;
    }

    public FlowList getFlow() {
        return anonFlow;
    }

    public void setFlow(FlowList flow) {
        this.anonFlow = flow;
    }

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
        return "\nStateFlow{" +
                "sName='" + sName + '\'' +
                ", fName='" + fName + '\'' +
                ", anonFlow=" + anonFlow +
                '}';
    }
}