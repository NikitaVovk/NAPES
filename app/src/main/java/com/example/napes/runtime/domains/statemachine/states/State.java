package com.example.napes.runtime.domains.statemachine.states;


import com.example.napes.runtime.domains.statemachine.actions.OnEntry;
import com.example.napes.runtime.domains.statemachine.actions.OnExit;
import com.example.napes.runtime.domains.statemachine.transitions.TransitionList;

public class State {
    String sName;
    OnEntry onEntry;
    OnExit onExit;
    TransitionList transitionList;

    public State(String sName) {
        this.sName = sName;
    }

    public State() {
        onEntry = new OnEntry();
        onExit = new OnExit();
        transitionList = new TransitionList();
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public OnEntry getOnEntry() {
        return onEntry;
    }

    public void setOnEntry(OnEntry onEntry) {
        this.onEntry = onEntry;
    }

    public OnExit getOnExit() {
        return onExit;
    }

    public void setOnExit(OnExit onExit) {
        this.onExit = onExit;
    }

    public TransitionList getTransitionList() {
        return transitionList;
    }

    public void setTransitionList(TransitionList transitionList) {
        this.transitionList = transitionList;
    }

    @Override
    public String toString() {
        return "\n\t\t\tState{" +
                "sName='" + sName + '\'' +
                ", onEntry=" + onEntry +
                ", onExit=" + onExit +
                ", transitionList=" + transitionList +
                '}';
    }
}
