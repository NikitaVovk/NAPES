package com.example.napes.runtime.service;

import android.graphics.Color;
import android.net.Uri;

import com.example.napes.MainActivity;
import com.example.napes.clients.StaticClients;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.component.Component;
import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.domains.events.EventList;
import com.example.napes.runtime.domains.ports.Port;
import com.example.napes.runtime.domains.ports.PortList;
import com.example.napes.runtime.domains.statemachine.StateMachine;
import com.example.napes.runtime.domains.statemachine.StateMachineList;
import com.example.napes.runtime.domains.statemachine.states.State;
import com.example.napes.runtime.domains.statemachine.transitions.Transition;
import com.example.napes.runtime.service.servers.TcpServer;
import com.example.napes.runtime.service.servers.UdpServer;

import java.util.ArrayList;
import java.util.Date;

public class Service {

     EventService eventService;
     ServiceFsm serviceFsm;
     ServicePorts servicePorts;
     ServiceFlows serviceFlows;
    Component component;
    MainActivity handler;
    public static UdpServer udpServer;
    public  ServiceStates serviceStates;

    public Service(Component component,MainActivity mainActivity) {
        this.component = component;
        this.handler = mainActivity;

    }


    public  void serviceMain(){

        Config.ipAddressBroker= component.getMqttBroker().getEndPointDefp().getIP();
        eventService = new EventService(handler);
        eventService.startMqttClient();

        serviceFsm = new ServiceFsm(eventService,component,handler,component.getStateMachineList());
        serviceFsm.start();
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//#################################################################################################
        ArrayList<ServiceStates> serviceStatesArrayList =  new ArrayList<>(serviceFsm.getServiceStatesArrayList());
//
//        for (StateMachine stateMachine: component.getStateMachineList().getStateMachines()) {
//            serviceStates = new ServiceStates(component,handler,eventService,stateMachine);
//            serviceStatesArrayList.add(serviceStates);
//            //serviceFlows = new ServiceFlows(handler,serviceStates.map,stateMachine);
//            serviceStates.start();
//        }
        System.out.println("ArrayList<ServiceStates> serviceStatesArrayList: "+serviceStatesArrayList );
        servicePorts = new ServicePorts(component,handler,serviceStatesArrayList);

       servicePorts.serviceServerPorts();



    }

    public EventService getEventService() {
        return eventService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
    //    public synchronized void serviceMain(){
//        StateMachineList stateMachineList = component.getStateMachineList();
//        //StaticClients.getMqttCallback().setTopics();
//
//        State currentState = getInitialState(stateMachineList.getStateMachines().get(0));
//
//        while(true){
//            for (Transition currentTransition:currentState.getTransitionList().getTransitions()) {
//
//                System.out.println("123");
//                try {
//
//                  synchronized (this){
//                      wait();
//                  }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (arrivedEvent!=null && currentTransition.geteName().equals(arrivedEvent.geteName()))
//                    currentState = getStateByName(stateMachineList.getStateMachines().get(0),currentTransition.getsName());
//                    arrivedEvent= null;
//                handler.setText("Current state: " +currentState.getsName());
//            }
//
//        }
//
//
//    }







}
