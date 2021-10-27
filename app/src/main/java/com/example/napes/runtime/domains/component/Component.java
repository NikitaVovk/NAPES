 package com.example.napes.runtime.domains.component;


 import com.example.napes.runtime.domains.events.EventList;
 import com.example.napes.runtime.domains.flows.FlowList;
 import com.example.napes.runtime.domains.mqtt.MQTTBroker;
 import com.example.napes.runtime.domains.ports.PortList;
 import com.example.napes.runtime.domains.statemachine.StateMachineList;

 public class Component {
    String cName;
    int pid;
    EventList eventList;
    StateMachineList stateMachineList;
    FlowList flowList;
    PortList portList;
    MQTTBroker mqttBroker;

    public MQTTBroker getMqttBroker() {
        return mqttBroker;
    }

    public void setMqttBroker(MQTTBroker mqttBroker) {
        this.mqttBroker = mqttBroker;
    }

    public PortList getPortList() {
        return portList;
    }

    public void setPortList(PortList portList) {
        this.portList = portList;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public EventList getEventList() {
        return eventList;
    }

    public void setEventList(EventList eventList) {
        this.eventList = eventList;
    }

    public StateMachineList getStateMachineList() {
        return stateMachineList;
    }

    public void setStateMachineList(StateMachineList stateMachineList) {
        this.stateMachineList = stateMachineList;
    }

    public FlowList getFlowList() {
        return flowList;
    }

    public void setFlowList(FlowList flowList) {
        this.flowList = flowList;
    }

    @Override
    public String toString() {
        return "Component{" +
                "\n\ncName='" + cName + '\'' +
                ", \n\npid=" + pid +
                ", \n\neventList=" + eventList +
                ", \n\nstateMachineList=" + stateMachineList +
                ", \n\nflowList=" + flowList +
                ", \n\nportList=" + portList +
                ", \n\nmqttBroker=" + mqttBroker +
                '}';
    }
}
