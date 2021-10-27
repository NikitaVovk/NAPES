package com.example.napes.runtime.domains.statemachine.transitions;

import java.util.ArrayList;

public class TransitionList {
    ArrayList<Transition> transitions;

    public TransitionList() {
        transitions = new ArrayList<>();
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(ArrayList<Transition> transitions) {
        this.transitions = transitions;
    }

    @Override
    public String toString() {
        return "TransitionList{" +
                "transitions=" + transitions +
                '}';
    }
}
