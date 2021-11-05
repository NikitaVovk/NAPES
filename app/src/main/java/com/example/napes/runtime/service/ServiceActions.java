package com.example.napes.runtime.service;

import com.example.napes.clients.StaticClients;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.domains.events.EventList;
import com.example.napes.runtime.domains.statemachine.actions.Action;
import com.example.napes.runtime.domains.statemachine.actions.ActionList;
import com.example.napes.runtime.domains.statemachine.states.State;

public class ServiceActions {
    EventList eventList;
    EventService eventService;

    public ServiceActions(EventList eventList) {
         this.eventList = eventList;
    }

    public ServiceActions(EventList eventList, EventService eventService) {
        this.eventList = eventList;
        this.eventService = eventService;
    }

    public void doActions(ActionList actionList){

        for (Action action : actionList.getActions()){
            Event event = findEventByeName(action.geteName());
            System.out.println(event);
            System.out.println("DOING ACTION + "+ action.geteName());
            if (event.geteType().equals("o")){
                System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOOOWWWWWWWWWWWW");
                Config.mqttTopic = event.getMqtt_eName();
                StaticClients.getMqttCallback().clientPublish("test");
                System.out.println("IVE SENT A MESSAGE AND CONTINUING MY THREAD!!!");

            }
            if (event.geteType().equals("l")){
                System.out.println("GENERUJ Z ACTIONS");
                System.out.println("LOCAL ACTION");
                ServiceLocalEvents serviceLocalEvents = new ServiceLocalEvents(event,eventService);
                serviceLocalEvents.generateLocalEvent();

            }

        }




    }

    public Event findEventByeName(String eName){
        for (Event event: eventList.getEvents()){
            if (event.geteName().equals(eName))
                return event;
        }
        return null;
    }
}
