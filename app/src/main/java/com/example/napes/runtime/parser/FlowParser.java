package com.example.napes.runtime.parser;



import com.example.napes.runtime.domains.flows.Flow;
import com.example.napes.runtime.domains.flows.FlowList;

import java.util.LinkedList;

public class FlowParser extends PayLoadParser{


    public FlowParser(LinkedList<String> linkedList) {
        super(linkedList);
    }
    public  FlowList parseFlowList(boolean isAnonymous){
        FlowList flowList = new FlowList();
        char till='0';
        if (linkedList.get(1).equals("["))
            till = ']';
        else if (linkedList.get(1).equals("{"))
            till='}';
        while (!linkedList.getFirst().equals(String.valueOf(till))&&
                !linkedList.get(1).equals(String.valueOf(till))){

            if (isAnonymous)
                flowList.getFlows().add(parseAnonFlow());
            else
                flowList.getFlows().add(parseFlow());

        }
        return flowList;

    }


    public  Flow parseFlow(){
        Flow flow = new Flow();
        flow.setfName(readTillTo('{',';'));

        flow.setfType(readTillTo('{',';'));
        divideParamUnit(flow,readTillTo(';',';'));
        setRealTimeDelay(flow);
        flow.setfParametr(Integer.parseInt(readTillTo(';','}')));
        linkedList.pop();
        return flow;

    }public  Flow parseAnonFlow(){
        Flow flow = new Flow();

        flow.setfType(readTillTo('{',';'));
        divideParamUnit(flow,readTillTo(';',';'));
        setRealTimeDelay(flow);
        flow.setfParametr(Integer.parseInt(readTillTo(';','}')));
        linkedList.pop();
        return flow;

    }
}
