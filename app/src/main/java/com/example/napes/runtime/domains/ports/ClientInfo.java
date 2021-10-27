package com.example.napes.runtime.domains.ports;


import com.example.napes.runtime.domains.payloads.EndPoint;
import com.example.napes.runtime.domains.ports.stateflow.StateFlowList;

public class ClientInfo {
    EndPoint endPoint;
    String mName;
    StateFlowList stateFlowList;

    public ClientInfo() {
        stateFlowList= new StateFlowList();
    }

    public EndPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(EndPoint endPoint) {
        this.endPoint = endPoint;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public StateFlowList getStateFlowList() {
        return stateFlowList;
    }

    public void setStateFlowList(StateFlowList stateFlowList) {
        this.stateFlowList = stateFlowList;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "endPoint=" + endPoint +
                ", mName='" + mName + '\'' +
                ", stateFlowList=" + stateFlowList +
                '}';
    }
}
