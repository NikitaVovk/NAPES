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

public class Service extends Thread {

     EventService eventService;         //obiekt uruchamiający wątek obsługi zdarzeń
     ServiceFsm serviceFsm;             //obiekt uruchamiający wątek obsługi maszyny stanów
     ServicePorts servicePorts;         //obiekt uruchamiający wątek obsługi portów
     ServiceFlows serviceFlows;         //obiekt uruchamiający wątek obsługi przepływów
    Component component;                //obiekt zawierający dane komponentu
    MainActivity handler;               //wskaźnik do interfejsu użytkownika (dla wyświetlenia logów)
    public static UdpServer udpServer;  //obiekt serwera UDP
    public  ServiceStates serviceStates;//obiekt uruchamiający wątek obsługi stanów

    public Service(Component component,MainActivity mainActivity) { //konstruktor klasy
        this.component = component;
        this.handler = mainActivity;

    }

    public  void serviceMain(){         //metoda zaczynająca emulację



        //Ustawienia parametrów oraz uruchamianie innych wątków obsługi

        Config.simulating = true;

        Config.ipAddressBroker= component.getMqttBroker().getEndPointDefp().getIP();
        eventService = new EventService(handler);
        eventService.startMqttClient(); // Start klienta MQTT

        serviceFsm = new ServiceFsm(eventService,component,handler,component.getStateMachineList());
        serviceFsm.start();             // Start zarządzania maszynami stanów

        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<ServiceStates> serviceStatesArrayList =  new ArrayList<>(serviceFsm.
                getServiceStatesArrayList()); // Pobranie zsynchronizowanych danych

        //
//        for (StateMachine stateMachine: component.getStateMachineList().getStateMachines()) {
//            serviceStates = new ServiceStates(component,handler,eventService,stateMachine);
//            serviceStatesArrayList.add(serviceStates);
//            //serviceFlows = new ServiceFlows(handler,serviceStates.map,stateMachine);
//            serviceStates.start();
//        }

        System.out.println("ArrayList<ServiceStates> serviceStatesArrayList: "+serviceStatesArrayList );
        // Start obsługi portów
        servicePorts = new ServicePorts(component,handler,serviceStatesArrayList);

        servicePorts.start();
       //servicePorts.serviceServerPorts();
        try {
            System.out.println("Service sleeping");
            Thread.currentThread().sleep(225_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Config.simulating = false;
        System.out.println("Changed Config "+ Config.simulating);
        try {
            System.out.println("Service sleeping");
            Thread.currentThread().sleep(5_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
