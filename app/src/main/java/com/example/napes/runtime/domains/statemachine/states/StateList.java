package com.example.napes.runtime.domains.statemachine.states;

import java.util.ArrayList;

public class StateList {
    ArrayList <State> states;

    public StateList() {
        states = new ArrayList<>();
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

    @Override
    public String toString() {
        return "StateList{" +
                "states=" + states +
                '}';
    }
}
