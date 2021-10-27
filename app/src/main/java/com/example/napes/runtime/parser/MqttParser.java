package com.example.napes.runtime.parser;



import com.example.napes.runtime.domains.mqtt.MQTTBroker;
import com.example.napes.runtime.domains.payloads.EndPoint;

import java.util.LinkedList;

public class MqttParser extends PayLoadParser {

    public MqttParser(LinkedList<String> linkedList) {
        super(linkedList);
    }
    public MQTTBroker parseMQTTBroker(){
        MQTTBroker mqttBroker = new MQTTBroker();
        EndPoint endPointDefp = new EndPoint();
        endPointDefp.setIP(readTillTo('{',';'));
        String port = readTillTo(';','}');
        if (!port.equals(""))
            endPointDefp.setPort(Integer.parseInt(port));

        mqttBroker.setEndPointDefp(endPointDefp);
        linkedList.pop();
        return mqttBroker;

    }
}
