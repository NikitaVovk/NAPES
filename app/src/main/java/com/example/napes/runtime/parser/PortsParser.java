package com.example.napes.runtime.parser;



import com.example.napes.runtime.domains.payloads.EndPoint;
import com.example.napes.runtime.domains.ports.ClientInfo;
import com.example.napes.runtime.domains.ports.Port;
import com.example.napes.runtime.domains.ports.PortList;
import com.example.napes.runtime.domains.ports.stateflow.StateFlow;
import com.example.napes.runtime.domains.ports.stateflow.StateFlowList;

import java.util.LinkedList;

public class PortsParser  extends PayLoadParser{

    public PortsParser(LinkedList<String> linkedList) {
        super(linkedList);
    }
    public PortList parsePortList(){
        PortList portList = new PortList();
        char till='0';
        if (linkedList.get(1).equals("["))
            till = ']';
        else if (linkedList.get(1).equals("{"))
            till='}';

        while (!linkedList.getFirst().equals(String.valueOf(till))&&
                !linkedList.get(1).equals(String.valueOf(till))){
            portList.getPorts().add(parsePort());
            //flowList.getFlows().add(parseFlow());

        }
        return portList;

    }

    public Port parsePort(){
        Port port = new Port();
        port.setpName(readTillTo('{',';'));
        port.setpType(readTillTo(';',';'));
        port.setpTransport(readTillTo(';',';'));
        EndPoint endPointHere = new EndPoint();
        endPointHere.setIP(readTillTo('{',';'));
        endPointHere.setPort(Integer.parseInt(readTillTo(';','}')));
        port.setEndPointHere(endPointHere);
        linkedList.pop();
        if (!linkedList.getFirst().equals("}"))
        {
            ClientInfo clientInfo = new ClientInfo();
            EndPoint endPoint = new EndPoint();
            endPoint.setIP(readTillTo('{',';'));
            endPoint.setPort(Integer.parseInt(readTillTo(';','}')));
            clientInfo.setEndPoint(endPoint);
            linkedList.pop();
            clientInfo.setmName(readTillTo(';',';'));////////////////////////
            clientInfo.setStateFlowList(parseStateFlowList());
            port.setClientInfo(clientInfo);

        }

        return port;

    }
    public StateFlowList parseStateFlowList(){
        StateFlowList stateFlowList = new StateFlowList();
        char till='0';
        if (linkedList.get(1).equals("["))
            till = ']';
        else if (linkedList.get(1).equals("{"))
            till='}';

        while (!linkedList.getFirst().equals(String.valueOf(till))&&
                !linkedList.get(1).equals(String.valueOf(till))){

            stateFlowList.getStateFlows().add(parseStateFlow());
//            flowList.getFlows().add(parseFlow());

        }
        linkedList.pop();
        return stateFlowList;

    }
    public StateFlow parseStateFlow(){
        StateFlow stateFlow = new StateFlow();
        stateFlow.setsName(readTillTo('{',';'));
        stateFlow.setfName(readTillTo(';','}'));
        //linkedList.pop();
        linkedList.pop();
        return stateFlow;

    }
}
