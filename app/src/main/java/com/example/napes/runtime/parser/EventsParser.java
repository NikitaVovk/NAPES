package com.example.napes.runtime.parser;



import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.domains.events.EventList;

import java.util.LinkedList;

public class EventsParser extends PayLoadParser{


    public EventsParser(LinkedList<String> linkedList) {
        super(linkedList);
    }
    public EventList parseEventList(){
        EventList eventList = new EventList();
        char till='0';
        if (linkedList.get(1).equals("["))
            till = ']';
        else if (linkedList.get(1).equals("{"))
            till='}';
        while (!linkedList.getFirst().equals(String.valueOf(till))&&
                !linkedList.get(1).equals(String.valueOf(till))){

            eventList.getEvents().add(parseEvent());

        }
        return eventList;
    }
    public Event parseEvent(){
        Event event = new Event();
        event.seteName(readTillTo('{',';'));
        event.seteType(readTillTo(';',';'));
        if (event.geteType().equals("l"))
            event.setTimeout(Integer.parseInt(readTillTo(';','}')));
        else
            event.setMqtt_eName(readTillTo(';','}'));

        return event;

    }
}
