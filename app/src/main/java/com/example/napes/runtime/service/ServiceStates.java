package com.example.napes.runtime.service;

import android.graphics.Color;

import com.example.napes.MainActivity;
import com.example.napes.runtime.domains.component.Component;
import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.domains.events.EventList;
import com.example.napes.runtime.domains.statemachine.StateMachine;
import com.example.napes.runtime.domains.statemachine.StateMachineList;
import com.example.napes.runtime.domains.statemachine.transitions.Transition;

public class ServiceStates extends Thread{
    public  EventService eventService;
    Component component;
    MainActivity handler;

    public ServiceStates(Component component, MainActivity mainActivity, EventService eventService) {
        this.component = component;
        this.handler = mainActivity;
        this.eventService =eventService;
    }

    public static Event searchEventByTopic(EventList eventList, String topic){
        for (Event event: eventList.getEvents()){
            if (event.getMqtt_eName().equals(topic))
                return event;
        }
        return null;
    }


    @Override
    public void run() {
        handler.setText("\nSTARTING SIMULATING...\n", Color.GREEN);
        StateMachineList stateMachineList = component.getStateMachineList();
        //StaticClients.getMqttCallback().setTopics();

        com.example.napes.runtime.domains.statemachine.states.State currentState = getInitialState(stateMachineList.getStateMachines().get(0));
        handler.setText("Current state: " +currentState.getsName()+"\n",Color.MAGENTA);

        while(true){
            synchronized (eventService){
                try {
                    eventService.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (Transition currentTransition:currentState.getTransitionList().getTransitions()) {



                    System.out.println("TRUE OR FALSE : "+currentTransition.geteName().equals(eventService.getArrivedEvent().geteName()));
                    if (eventService.getArrivedEvent() != null && currentTransition.geteName().equals(eventService.getArrivedEvent().geteName()))
                        currentState = getStateByName(stateMachineList.getStateMachines().get(0), currentTransition.getsName());
                    eventService.setArrivedEvent(null);
                    handler.setText("Current state: " + currentState.getsName()+"\n",Color.MAGENTA);


            }
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
