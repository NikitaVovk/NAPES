package com.example.napes.runtime.domains.statemachine.transitions;


import com.example.napes.runtime.domains.statemachine.actions.ActionList;

public class Transition {
    String eName;
    String sName;
    ActionList actionList;

    public Transition() {
        actionList = new ActionList();
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public ActionList getActionList() {
        return actionList;
    }

    public void setActionList(ActionList actionList) {
        this.actionList = actionList;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "eName='" + eName + '\'' +
                ", sName='" + sName + '\'' +
                ", actionList=" + actionList +
                '}';
    }
}
