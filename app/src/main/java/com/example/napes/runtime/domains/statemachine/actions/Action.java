package com.example.napes.runtime.domains.statemachine.actions;

public class Action {
    String eName;

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    @Override
    public String toString() {
        return "Action{" +
                "eName='" + eName + '\'' +
                '}';
    }
}
