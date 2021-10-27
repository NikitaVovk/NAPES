package com.example.napes.runtime.domains.statemachine.actions;


public class OnEntry {
    ActionList actionList;

    public OnEntry() {
        actionList= new ActionList();
    }

    public ActionList getActionList() {
        return actionList;
    }

    public void setActionList(ActionList actionList) {
        this.actionList = actionList;
    }

    @Override
    public String toString() {
        return "OnEntry{" +
                "actionList=" + actionList +
                '}';
    }
}
