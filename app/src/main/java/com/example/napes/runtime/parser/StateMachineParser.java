package com.example.napes.runtime.parser;


import com.example.napes.runtime.domains.statemachine.StateMachine;
import com.example.napes.runtime.domains.statemachine.StateMachineList;
import com.example.napes.runtime.domains.statemachine.actions.Action;
import com.example.napes.runtime.domains.statemachine.actions.ActionList;
import com.example.napes.runtime.domains.statemachine.states.State;
import com.example.napes.runtime.domains.statemachine.states.StateList;
import com.example.napes.runtime.domains.statemachine.transitions.Transition;

import java.util.LinkedList;

public class StateMachineParser  extends PayLoadParser{

    public StateMachineParser(LinkedList<String> linkedList) {
        super(linkedList);
    }
    public StateMachineList parseStateMachineList(){
        StateMachineList stateMachineList = new StateMachineList();
        char till='0';
        if (linkedList.get(1).equals("["))
            till = ']';
        else if (linkedList.get(1).equals("{"))
            till='}';
        while (!linkedList.getFirst().equals(String.valueOf(till))&&
                !linkedList.get(1).equals(String.valueOf(till))){

            stateMachineList.getStateMachines().add(parseStateMachine());

        }
        return stateMachineList;
    }
    public StateMachine parseStateMachine(){
        StateMachine stateMachine = new StateMachine();
        stateMachine.setmName(readTillTo('{',';'));
        stateMachine.setStateList(parseStateList());
        // stateMachine.setInitial_sName(readTillTo());
        stateMachine.setInitial_sName(readTillTo(';','}'));
        return stateMachine;
    }
    public StateList parseStateList(){
        StateList  stateList = new StateList();
        char till='0';
        if (linkedList.get(1).equals("["))
            till = ']';
        else if (linkedList.get(1).equals("{"))
            till='}';
        while (!linkedList.getFirst().equals(String.valueOf(till))&&
                !linkedList.get(1).equals(String.valueOf(till))){

            stateList.getStates().add(parseState());
            linkedList.pop();

        }
        return stateList;
    }
    public State parseState(){
        State state = new State();
        state.setsName(readTillTo('{',';'));

        if (!linkedList.get(1).equals(";")){
            state.getOnEntry().setActionList(parseActionList());

        }
        else linkedList.pop();



        if (!linkedList.get(1).equals(";")){
            state.getOnExit().setActionList(parseActionList());

        }
        else linkedList.pop();




        if (!linkedList.get(1).equals("}")){
            char till='0';
            if (linkedList.get(1).equals("["))
                till = ']';
            else if (linkedList.get(1).equals("{"))
                till='}';

            while (!linkedList.getFirst().equals(String.valueOf(till))&&
                    !linkedList.get(1).equals(String.valueOf(till))){

                state.getTransitionList().getTransitions().add(parseTransition());

            }
        }
        linkedList.pop();


        return state;
    }
    public Transition parseTransition(){
        Transition transition = new Transition();

        transition.seteName(readTillTo('{',';'));
        transition.setsName(readTillTo2(';', '}',';'));
        if (linkedList.getFirst().equals(";")&&
                linkedList.get(1).equals("[")){
            transition.setActionList(parseActionList() );
        }
        if (linkedList.getFirst().equals(";")){
            Action action = new Action();
            action.seteName(readTillTo(';','}'));
            transition.getActionList().getActions().add(action);
        }
        return transition;

    }
    public ActionList parseActionList(){
        ActionList actionList = new ActionList();
        char till='0';
        if (linkedList.get(1).equals("[")){
            till = ']';
            actionList.getActions().add(parseAction('[',',',','));
        }
        else if (!linkedList.get(1).equals(";")){
            till=';';
            actionList.getActions().add(parseAction(';',';',';'));
        }

        while (!linkedList.getFirst().equals(String.valueOf(till))&&
                !linkedList.get(1).equals(String.valueOf(till))){
            actionList.getActions().add(parseAction(',',',',']'));
        }
        if (linkedList.getFirst().equals("]"))
            linkedList.pop();
        return actionList;
    }
    public  Action parseAction(char from, char to, char to2){

        Action action = new Action();
        action.seteName(readTillTo2(from,to, to2));


        return action;

    }
}
