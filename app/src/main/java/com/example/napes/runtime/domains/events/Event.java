package com.example.napes.runtime.domains.events;

public class Event {
    String eName;
    String eType;
    String mqtt_eName;
    int timeout;


    public Event() {
    }

    public Event(String eName, String eType, String mqtt_eName) {
        this.eName = eName;
        this.eType = eType;
        this.mqtt_eName = mqtt_eName;
    }
    public Event(Event event) {
        this.eName = event.eName;
        this.eType = event.eType;
        this.mqtt_eName = event.mqtt_eName;
    }

    public Event(String eName, String eType, String mqtt_eName, int timeout) {
        this.eName = eName;
        this.eType = eType;
        this.mqtt_eName = mqtt_eName;
        this.timeout = timeout;
    }

//    public Event(Event event) {
//        this.eName = event.eName;
//        this.eType = event.eType;
//        this.mqtt_eName =event.mqtt_eName;
//        this.timeout = event.timeout;
//    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String getMqtt_eName() {
        return mqtt_eName;
    }

    public void setMqtt_eName(String mqtt_eName) {
        this.mqtt_eName = mqtt_eName;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "\n\tEvent{" +
                "eName='" + eName + '\'' +
                ", eType='" + eType + '\'' +
                ", mqtt_eName='" + mqtt_eName + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
