package com.example.napes.runtime.domains.ports.stateflow;

import java.util.ArrayList;

public class StateFlowList {
    ArrayList<StateFlow> stateFlows;

    public StateFlowList() {
        stateFlows = new ArrayList<>();
    }

    public ArrayList<StateFlow> getStateFlows() {
        return stateFlows;
    }

    public void setStateFlows(ArrayList<StateFlow> stateFlows) {
        this.stateFlows = stateFlows;
    }

    @Override
    public String toString() {
        return "StateFlowList{" +
                "stateFlows=" + stateFlows +
                '}';
    }
}
