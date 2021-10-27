package com.example.napes.runtime.parser;


import com.example.napes.runtime.domains.component.Component;

import java.util.LinkedList;

public class ComponentParser extends PayLoadParser {
    private EventsParser eventsParser;
    private FlowParser flowParser;
    private MqttParser mqttParser;
    private PortsParser portsParser;
    private StateMachineParser stateMachineParser;

    public ComponentParser(LinkedList<String> linkedList) {
        super(linkedList);
        eventsParser = new EventsParser(linkedList);
        flowParser = new FlowParser(linkedList);
        mqttParser= new MqttParser(linkedList);
        portsParser = new PortsParser(linkedList);
        stateMachineParser = new StateMachineParser(linkedList);
    }

    public Component parseComponent(){
        Component component = new Component();
        component.setcName(readTillTo('{',';'));


        component.setPid(Integer.parseInt(readTillTo(';',';')));
        readTillTo('{',';');


        component.setEventList(eventsParser.parseEventList());
        readTillTo('{',';');
        System.out.println(component.getEventList());


        component.setStateMachineList(stateMachineParser.parseStateMachineList());
        readTillTo('{',';');
        System.out.println(component.getStateMachineList());

        if (!readTillTo(';',';').equals("")){

            component.setFlowList(flowParser.parseFlowList(false));
        }
        readTillTo('{',';');
        System.out.println(component.getFlowList());

        component.setPortList(portsParser.parsePortList());
        readTillTo('{',';');
        System.out.println(component.getPortList());


        component.setMqttBroker(mqttParser.parseMQTTBroker());
        return component;
    }
}
