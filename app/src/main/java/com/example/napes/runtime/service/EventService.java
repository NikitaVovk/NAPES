package com.example.napes.runtime.service;

import com.example.napes.MainActivity;
import com.example.napes.clients.MqttCallbackImpl;
import com.example.napes.clients.StaticClients;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.events.Event;

import java.util.LinkedList;

public class EventService  {
    private   Event arrivedEvent; //MAKE IT LIST
    private LinkedList<Event> arrivedQueueEvents;
    private MainActivity handler;
    private boolean isChanged;

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public EventService(MainActivity mainActivity) {
        arrivedEvent=null;
        this.handler = mainActivity;
        this.arrivedQueueEvents = new LinkedList<>();


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

    public LinkedList<Event> getArrivedQueueEvents() {
        return arrivedQueueEvents;
    }

    public void setArrivedQueueEvents(LinkedList<Event> arrivedQueueEvents) {
        this.arrivedQueueEvents = arrivedQueueEvents;
    }
}
