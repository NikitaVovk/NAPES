package com.example.napes.runtime.service;

import android.graphics.Color;

import com.example.napes.MainActivity;
import com.example.napes.runtime.domains.component.Component;
import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.domains.events.EventList;
import com.example.napes.runtime.domains.statemachine.StateMachine;
import com.example.napes.runtime.domains.statemachine.StateMachineList;
import com.example.napes.runtime.domains.statemachine.states.State;
import com.example.napes.runtime.domains.statemachine.transitions.Transition;

import java.util.HashMap;
import java.util.Map;

public class ServiceStates extends Thread{
    public  EventService eventService;
    public ServiceActions serviceActions;
    Component component;
    MainActivity handler;
    StateMachine stateMachine;
    Map<String,String> map;

    public ServiceStates(Component component, MainActivity mainActivity, EventService eventService, StateMachine stateMachine) {
        this.component = component;
        this.handler = mainActivity;
        this.eventService =eventService;
        this.stateMachine = stateMachine;
    }

    public static Event searchEventByTopic(EventList eventList, String topic){
        for (Event event: eventList.getEvents()){
            if (event.getMqtt_eName().equals(topic))
                return event;
        }
        return null;
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public void run() {
        handler.setText("\nSTARTING SIMULATING FSM: "+stateMachine.getmName()+"\n", Color.GREEN);
        map= new HashMap<String, String>();


        //StateMachineList stateMachineList = component.getStateMachineList();
        //StaticClients.getMqttCallback().setTopics();

        com.example.napes.runtime.domains.statemachine.states.State currentState = getInitialState(stateMachine);
        ServiceActions sa = new ServiceActions(component.getEventList());
        sa.doActions(currentState.getOnEntry().getActionList());
        map.put(stateMachine.getmName(),currentState.getsName());
        handler.setText("Current state: " +currentState.getsName()+"\n",Color.MAGENTA);


        while(true){

            synchronized (eventService){

                System.out.println(Thread.currentThread().getName());
                Event arrivedEventTemp;
                try {

                    eventService.wait();
                    arrivedEventTemp = new Event(eventService.getArrivedEvent());
                    for (Transition currentTransition:currentState.getTransitionList().getTransitions()) {
                        //component.notify();


                        System.out.println("ARRIVED EVENT: "+arrivedEventTemp.geteName());

                        // System.out.println("TRUE OR FALSE : "+currentTransition.geteName().equals(eventService.getArrivedEvent().geteName()));
                        if (arrivedEventTemp != null && currentTransition.geteName().equals(arrivedEventTemp.geteName())){
                            //#########################################################
                            //do on exit action on currentState  and WITHOUT? transition actions
                            serviceActions = new ServiceActions(component.getEventList());
                            serviceActions.doActions(currentState.getOnExit().getActionList());

                            currentState = getStateByName(stateMachine, currentTransition.getsName());


                            serviceActions.doActions(currentTransition.getActionList());
                            serviceActions.doActions(currentState.getOnEntry().getActionList());

                            //currentTransition.getActionList();
                            //currentState.getOnEntry().getActionList()

                            //do on entry action on currentState  and   transition actions
                            map.put(stateMachine.getmName(),currentState.getsName());
                        }

                        handler.setText("Current state: " + currentState.getsName()+"\n",Color.MAGENTA);


                    }
                   // component.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(map);
               // eventService.setArrivedEvent(null);
            }

        }
    }

    private com.example.napes.runtime.domains.statemachine.states.State getInitialState(StateMachine stateMachine){
        for (com.example.napes.runtime.domains.statemachine.states.State state:stateMachine.getStateList().getStates()) {
            if( state.getsName().equals(stateMachine.getInitial_sName()))
                return state;
        }
        return null;
    }
    private com.example.napes.runtime.domains.statemachine.states.State getStateByName(StateMachine stateMachine, String sName){
        for (com.example.napes.runtime.domains.statemachine.states.State state:stateMachine.getStateList().getStates()) {
            if( state.getsName().equals(sName)){
                return state;
            }
        }
        return null;
    }

}
