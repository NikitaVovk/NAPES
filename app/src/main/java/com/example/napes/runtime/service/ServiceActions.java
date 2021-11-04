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

    public ServiceActions(EventList eventList) {
         this.eventList = eventList;
    }


    public void doActions(ActionList actionList){

        for (Action action : actionList.getActions()){
            Event event = findEventByeName(action.geteName());
            if (event.geteType().equals("o")){
                System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOOOOOOWWWWWWWWWWWW");
                Config.mqttTopic = event.getMqtt_eName();
                StaticClients.getMqttCallback().clientPublish("test");

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
