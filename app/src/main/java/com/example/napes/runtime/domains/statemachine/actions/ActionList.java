package com.example.napes.runtime.domains.statemachine.actions;


import java.util.ArrayList;

public class ActionList {
    ArrayList<Action> actions;

    public ActionList() {
        actions = new ArrayList<>();
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "ActionList{" +
                "actions=" + actions +
                '}';
    }
}
