package com.example.napes.runtime.domains.flows;

import java.util.ArrayList;

public class FlowList {
    ArrayList<Flow> flows;

    public FlowList() {
        flows = new ArrayList<>();
    }

    public ArrayList<Flow> getFlows() {
        return flows;
    }

    public void setFlows(ArrayList<Flow> flows) {
        this.flows = flows;
    }

    @Override
    public String toString() {
        return "FlowList{" +
                "flows=" + flows +
                '}';
    }
}
