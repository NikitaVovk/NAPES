package com.example.napes.runtime.service;

import com.example.napes.MainActivity;
import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.service.payload.Colors;

import java.util.ListIterator;

public class ServiceLocalEvents extends Thread{

    Event localEvent;
    EventService eventService;
    MainActivity handler;

    public ServiceLocalEvents(Event localEvent, EventService eventService, MainActivity handler) {
        this.localEvent = localEvent;
        this.eventService = eventService;
        this.handler = handler;
    }

    public  void generateLocalEvent(){
        System.out.println("GENERATING LOCAL EVENT !!!!!!!!!!!" + localEvent.geteName());
        this.start();
    }
    @Override
    public void run(){


            try {
                handler.setText("#Local/Local event starting ...\n" +
                        "@     Event  >>>  "+localEvent.geteName()+
                        "\n@     Time  >>>  "+localEvent.getTimeout()+" [s]\n", Colors.localColor);

                String currentTime = (Long.toString(System.currentTimeMillis()));
                handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":"+currentTime+
                        ",\"ph\":\"b\",\"cat\":\"service_events\",\"name\":\""+localEvent.geteName()
                        +"\",\"id\": 1,\"args\":{}},",handler);

                System.out.println("WAITIIIIIIING................................................"+localEvent.geteName());
             //   eventService.wait();
                Thread.sleep(localEvent.getTimeout()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        synchronized (eventService){
           // eventService.setArrivedEvent(localEvent);
            eventService.getArrivedQueueEvents().add(localEvent);

//            ListIterator<Event> eventListIterator = eventService.getArrivedQueueEvents().listIterator();
//            eventListIterator.add(localEvent);

            eventService.setChanged(true);
            eventService.notifyAll();
            System.out.println("IM WOKEN UP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+localEvent.geteName());
        }
        String currentTime = (Long.toString(System.currentTimeMillis()));
        handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":"+currentTime+
                ",\"ph\":\"e\",\"cat\":\"service_events\",\"name\":\""+localEvent.geteName()
                +"\",\"id\": 1,\"args\":{}},",handler);
        handler.setText("#Local/Local event arrived ...\n" +
                "@     Event  >>>  "+localEvent.geteName()+
                "\n@     Time  >>>  "+localEvent.getTimeout()+" [s]\n", Colors.localColor);
    }
}


