package com.example.napes.runtime.service;

import com.example.napes.MainActivity;
import com.example.napes.clients.MqttCallbackImpl;
import com.example.napes.clients.StaticClients;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.events.Event;

public class EventService {
    private   Event arrivedEvent;
    private MainActivity handler;



    public EventService(MainActivity mainActivity) {
        arrivedEvent=null;
        this.handler = mainActivity;


    }

    public void startMqttClient(){
        StaticClients.setMqttCallback(new MqttCallbackImpl(handler));
        StaticClients.getMqttCallback().setEventService(this);
        StaticClients.getMqttCallback().setParams();
    }

    public Event getArrivedEvent() {
        return arrivedEvent;
    }

    public void setArrivedEvent(Event arrivedEvent) {
        this.arrivedEvent = arrivedEvent;
    }
}
