package com.example.napes.runtime.service;

import android.graphics.Color;

import com.example.napes.MainActivity;
import com.example.napes.runtime.domains.component.Component;
import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.domains.statemachine.StateMachine;
import com.example.napes.runtime.domains.statemachine.StateMachineList;

import java.util.ArrayList;
import java.util.LinkedList;

public class ServiceFsm extends Thread {
    public  EventService eventService;
   // public ServiceActions serviceActions;
    Component component;
    MainActivity handler;
    StateMachineList stateMachineList;
    ArrayList<ServiceStates> serviceStatesArrayList ;

    public ServiceFsm(EventService eventService, Component component, MainActivity handler, StateMachineList stateMachineList) {
        this.eventService = eventService;
        this.component = component;
        this.handler = handler;
        this.stateMachineList = stateMachineList;
        serviceStatesArrayList =  new ArrayList<>();
    }

    @Override
    public synchronized void  run() {
        for (StateMachine stateMachine: component.getStateMachineList().getStateMachines()) {
            ServiceStates serviceStates = new ServiceStates(component,handler,eventService,stateMachine);
            serviceStates.setInitialStateForFSM();
            handler.setText("\nSTARTING SIMULATING FSM: "+stateMachine.getmName()+"\n", Color.GREEN);
            serviceStatesArrayList.add(serviceStates);
        }
//        for (ServiceStates ss:serviceStatesArrayList){
//            ss.setInitialStateForFSM();
//            // ss.setTempEvents(new LinkedList<>(eventService.getArrivedQueueEvents()));
//        }
                    while(true){


            synchronized (eventService){

                try {

                    if (!eventService.isChanged()){
                        System.out.println("UnderIfWaiting");
                    eventService.wait();


                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

                        eventService.setChanged(false);

                    for (ServiceStates ss:serviceStatesArrayList){

                        ss.setTempEvents(new LinkedList<>(eventService.getArrivedQueueEvents()));

                        System.out.println("LISTAAAAA: "+eventService.getArrivedQueueEvents());

                        if (!ss.isAlive())
                        ss.start();
                        else{
                            System.out.println("NOTIFYING!!!!!!!!!");
                          //  synchronized (ss.tempEvents){
                            synchronized (ss){
                                ss.notifyAll();
                            }
                        }

                    }
                        System.out.println("STARTED SOME");
                        for (ServiceStates ss:serviceStatesArrayList){
                            while (!ss.isServed){

                            }

                        //    System.out.println("YA TYTA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }





                        eventService.setArrivedQueueEvents(new LinkedList<>());





        }





    }
}
