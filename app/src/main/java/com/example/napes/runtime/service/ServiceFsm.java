package com.example.napes.runtime.service;

import android.graphics.Color;

import com.example.napes.MainActivity;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.component.Component;
import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.domains.statemachine.StateMachine;
import com.example.napes.runtime.domains.statemachine.StateMachineList;
import com.example.napes.runtime.service.payload.Colors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class ServiceFsm extends Thread {
    public  EventService eventService;
   // public ServiceActions serviceActions;
    Component component;
    MainActivity handler;
    StateMachineList stateMachineList;
    ArrayList<ServiceStates> serviceStatesArrayList ;

    public ArrayList<ServiceStates> getServiceStatesArrayList() {
        return serviceStatesArrayList;
    }

    public void setServiceStatesArrayList(ArrayList<ServiceStates> serviceStatesArrayList) {
        this.serviceStatesArrayList = serviceStatesArrayList;
    }

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

            handler.setText("STARTING SIMULATING FSM: "+stateMachine.getmName()+
                    "\n........................................................................\n", Colors.localColor);

            serviceStates.setInitialStateForFSM();


            serviceStatesArrayList.add(serviceStates);
        }
        LinkedList<Event> eventLinkedList = null;
//        for (ServiceStates ss:serviceStatesArrayList){
//            ss.setInitialStateForFSM();
//            // ss.setTempEvents(new LinkedList<>(eventService.getArrivedQueueEvents()));
//        }

        while(true&& Config.simulating){
                //    while(true){ zmiana 20_12_22


            synchronized (eventService) {

                try {


                    // jeśli lista kolejki zdarzeń nie zmieniła się,
                    // to system czeka na nowe zdarzenie wejściowe
                    if (!eventService.isChanged()) {
                        eventService.setArrivedQueueEvents(new LinkedList<>());
                        eventLinkedList = null;

                        // semafor, oczekujący na zdarzenie wejściowe
                        eventService.wait();
                    }
                    if (eventLinkedList==null)
                    eventLinkedList = new LinkedList<>(eventService.getArrivedQueueEvents());
                    //I need to pop this list
                    else //flushing the queue
                    {
                        ListIterator<Event> listIterator = eventLinkedList.listIterator();
                        while (listIterator.hasNext()){
                            listIterator.next();
                            eventService.getArrivedQueueEvents().pop();
                        }
                        eventLinkedList = new LinkedList<>(eventService.getArrivedQueueEvents());
                    }

                    eventService.setChanged(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



                    for (ServiceStates ss:serviceStatesArrayList){

                        ss.setTempEvents(new LinkedList<>(eventLinkedList));

                        System.out.println("LISTAAAAA: "+eventLinkedList);

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
                                try {
                                    Thread.currentThread().sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        //    System.out.println("YA TYTA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }












        }





    }
}
