package com.example.napes.runtime.service;

import android.graphics.Color;

import com.example.napes.MainActivity;
import com.example.napes.runtime.domains.component.Component;
import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.domains.events.EventList;
import com.example.napes.runtime.domains.statemachine.states.State;
import com.example.napes.runtime.domains.statemachine.StateMachine;
import com.example.napes.runtime.domains.statemachine.StateMachineList;

import com.example.napes.runtime.domains.statemachine.transitions.Transition;
import com.example.napes.runtime.service.payload.Colors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

public class ServiceStates extends Thread{
    public  EventService eventService;
    public ServiceActions serviceActions;
    Component component;
    MainActivity handler;
    StateMachine stateMachine;
    Map<String,String> map;
    public  boolean isServed ;

    public ServiceStates(Component component, MainActivity mainActivity, EventService eventService, StateMachine stateMachine) {
        super(stateMachine.getmName());
        this.component = component;
        this.handler = mainActivity;
        this.eventService =eventService;
        this.stateMachine = stateMachine;
        this.isServed = false;
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


    public void setInitialStateForFSM(){
        map= new HashMap<String, String>();
         currentState = getInitialState(stateMachine);
         sa = new ServiceActions(component.getEventList(),eventService,handler);
        sa.doActions(currentState.getOnEntry().getActionList());
        map.put(stateMachine.getmName(),currentState.getsName());
        tempEvents = new LinkedList<>();
        handler.setText("FSM: "+this.getStateMachine().getmName()+"   <------>   Initial state: " + currentState.getsName()+"\n", Colors.stateColor);


    }
    public void setTempEvents(LinkedList<Event> events){
        tempEvents = events;


    }

   private com.example.napes.runtime.domains.statemachine.states.State currentState;
    LinkedList<Event> tempEvents;
    ServiceActions sa;

    public com.example.napes.runtime.domains.statemachine.states.State getCurrentState() {
        return currentState;
    }

    @Override
    public  void run() {
        while (true){
       // handler.setText("\nSTARTING SIMULATING FSM: "+stateMachine.getmName()+"\n", Color.GREEN);
//        map= new HashMap<String, String>();

            System.out.println("YA TYTA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //StateMachineList stateMachineList = component.getStateMachineList();
        //StaticClients.getMqttCallback().setTopics();

//############################################################ this was correct
//        currentState = getInitialState(stateMachine);
//        ServiceActions sa = new ServiceActions(component.getEventList(),eventService);
//        sa.doActions(currentState.getOnEntry().getActionList());
//        map.put(stateMachine.getmName(),currentState.getsName());
//        handler.setText("Current state: " +currentState.getsName()+"\n",Color.MAGENTA);

            isServed = false;
//        while(true){
            Event arrivedEventTemp;
//          //  LinkedList<Event> tempEvents = new LinkedList<>();
//
//            synchronized (eventService){
//
//                System.out.println("THREAD RUNNING"+Thread.currentThread().getName());
//
//                try {
//
//
//                  //  eventService.notifyAll();
//                    if (!eventService.isChanged())
//                    eventService.wait();
//
//                    tempEvents =  new LinkedList<>(eventService.getArrivedQueueEvents());
//                    eventService.setArrivedQueueEvents(new LinkedList<>());
//                    eventService.setChanged(false);
//
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
            System.out.println("IM waiting and queue is : "+ tempEvents);
                    //##########################################################
                    for (ListIterator<Event> iteratorEvent = tempEvents.listIterator(); iteratorEvent.hasNext();) {
                        System.out.println("CURRENT STATE::::"+ currentState);

                          //  System.out.println("Iterator: " + iteratorEvent.next());
                           // arrivedEventTemp = new Event(iteratorEvent.next());

                            arrivedEventTemp = iteratorEvent.next();

                        // while (eventService.getArrivedEventLIST()!=null)
//                    arrivedEventTemp = new Event(eventService.getArrivedEvent());
                    for (Transition currentTransition:currentState.getTransitionList().getTransitions()) {
                        //component.notify();


                        System.out.println("ARRIVED EVENT: "+arrivedEventTemp.geteName()+" == "+currentTransition.geteName());

                        // System.out.println("TRUE OR FALSE : "+currentTransition.geteName().equals(eventService.getArrivedEvent().geteName()));
                        if (arrivedEventTemp != null && currentTransition.geteName().equals(arrivedEventTemp.geteName())){
                            //#########################################################
                            //do on exit action on currentState  and WITHOUT? transition actions

                            //serviceActions = new ServiceActions(component.getEventList());
                            sa.doActions(currentState.getOnExit().getActionList());

                            currentState = getStateByName(stateMachine, currentTransition.getsName());

                            handler.setText("FSM: "+this.getStateMachine().getmName()+"   <------>   Current state: " + currentState.getsName()+"\n",Colors.stateColor);

                            sa.doActions(currentTransition.getActionList());
                            System.out.println("HERE 11111111222222 "+currentState.getOnEntry().getActionList());
                            sa.doActions(currentState.getOnEntry().getActionList());

                            //currentTransition.getActionList();
                            //currentState.getOnEntry().getActionList()

                            //do on entry action on currentState  and   transition actions
                            map.put(stateMachine.getmName(),currentState.getsName());
                        }

                      //  handler.setText("FSM: "+this.getStateMachine().getmName()+"   <------>   Current state changed to: " + currentState.getsName()+"\n",Color.rgb(14,63,84));


                    }

                   // component.wait();


                System.out.println(map);
               // eventService.setArrivedEvent(null);
            }

                    isServed = true;
                synchronized (this){
                    try {

                        System.out.println("CZEKAJU");
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
