package com.example.napes.runtime.domains.statemachine;

import java.util.ArrayList;

public class StateMachineList {
    ArrayList<StateMachine> stateMachines;

    public StateMachineList() {
        stateMachines = new ArrayList<>();
    }

    public ArrayList<StateMachine> getStateMachines() {
        return stateMachines;
    }

    public void setStateMachines(ArrayList<StateMachine> stateMachines) {
        this.stateMachines = stateMachines;
    }

    @Override
    public String toString() {
        return "StateMachineList{" +
                "stateMachines=" + stateMachines +
                '}';
    }
}
