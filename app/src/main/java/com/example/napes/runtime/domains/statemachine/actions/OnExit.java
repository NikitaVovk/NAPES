package com.example.napes.runtime.domains.statemachine.actions;


public class OnExit {
    ActionList actionList;

    public OnExit() {
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
        return "OnExit{" +
                "actionList=" + actionList +
                '}';
    }
}
