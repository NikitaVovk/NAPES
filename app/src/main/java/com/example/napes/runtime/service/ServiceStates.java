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

public class ServiceStates extends Thread{
    public  EventService eventService;
    Component component;
    MainActivity handler;
    StateMachine stateMachine;

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


    @Override
    public void run() {
        handler.setText("\nSTARTING SIMULATING FSM: "+stateMachine.getmName()+"\n", Color.GREEN);

        //StateMachineList stateMachineList = component.getStateMachineList();
        //StaticClients.getMqttCallback().setTopics();

        com.example.napes.runtime.domains.statemachine.states.State currentState = getInitialState(stateMachine);
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
                        if (arrivedEventTemp != null && currentTransition.geteName().equals(arrivedEventTemp.geteName()))
                            currentState = getStateByName(stateMachine, currentTransition.getsName());

                        handler.setText("Current state: " + currentState.getsName()+"\n",Color.MAGENTA);


                    }
                   // component.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
