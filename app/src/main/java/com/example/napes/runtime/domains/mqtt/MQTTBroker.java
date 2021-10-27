package com.example.napes.runtime.domains.mqtt;


import com.example.napes.runtime.domains.payloads.EndPoint;

public class MQTTBroker
{
    EndPoint endPointDefp;

    public EndPoint getEndPointDefp() {
        return endPointDefp;
    }

    public void setEndPointDefp(EndPoint endPointDefp) {
        this.endPointDefp = endPointDefp;
    }

    @Override
    public String toString() {
        return "MQTTBroker{" +
                "endPointDefp=" + endPointDefp +
                '}';
    }
}
