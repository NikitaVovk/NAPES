package com.example.napes.runtime.domains.events;

import java.util.ArrayList;

public class EventList {
    ArrayList<Event> events;

    public EventList() {
        events = new ArrayList<>();
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "EventList{" +
                "events=" + events +
                '}';
    }
}
