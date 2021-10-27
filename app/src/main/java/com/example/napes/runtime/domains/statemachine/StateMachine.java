package com.example.napes.runtime.domains.statemachine;


import com.example.napes.runtime.domains.statemachine.states.StateList;

public class StateMachine {
    String mName;
    StateList stateList;
    String initial_sName;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public StateList getStateList() {
        return stateList;
    }

    public void setStateList(StateList stateList) {
        this.stateList = stateList;
    }

    public String getInitial_sName() {
        return initial_sName;
    }

    public void setInitial_sName(String initial_sName) {
        this.initial_sName = initial_sName;
    }

    @Override
    public String toString() {
        return "\n\tStateMachine{" +
                "mName='" + mName + '\'' +
                ", stateList=" + stateList +
                ", initial_sName='" + initial_sName + '\'' +
                '}';
    }
}
