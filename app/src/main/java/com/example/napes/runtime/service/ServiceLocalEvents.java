package com.example.napes.runtime.service;

import com.example.napes.runtime.domains.events.Event;

import java.util.ListIterator;

public class ServiceLocalEvents extends Thread{

    Event localEvent;
    EventService eventService;

    public ServiceLocalEvents(Event localEvent, EventService eventService) {
        this.localEvent = localEvent;
        this.eventService = eventService;
    }

    public  void generateLocalEvent(){
        System.out.println("GENERATING LOCAL EVENT !!!!!!!!!!!" + localEvent.geteName());
        this.start();
    }
    @Override
    public void run(){
        synchronized (eventService){

            try {
                System.out.println("WAITIIIIIIING................................................"+localEvent.geteName());
             //   eventService.wait();
                Thread.sleep(localEvent.getTimeout()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
           // eventService.setArrivedEvent(localEvent);
          //  eventService.getArrivedQueueEvents().add(localEvent);
            ListIterator<Event> eventListIterator = eventService.getArrivedQueueEvents().listIterator();
            eventListIterator.add(localEvent);
            eventService.setChanged(true);
            eventService.notifyAll();
            System.out.println("IM WOKEN UP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+localEvent.geteName());
        }
    }
}
