package com.example.napes.runtime.parser;



import com.example.napes.runtime.domains.flows.Flow;
import com.example.napes.runtime.domains.flows.FlowList;

import java.util.LinkedList;

public class FlowParser extends PayLoadParser{


    public FlowParser(LinkedList<String> linkedList) {
        super(linkedList);
    }
    public FlowList parseFlowList(){
        FlowList flowList = new FlowList();
        char till='0';
        if (linkedList.get(1).equals("["))
            till = ']';
        else if (linkedList.get(1).equals("{"))
            till='}';
        while (!linkedList.getFirst().equals(String.valueOf(till))&&
                !linkedList.get(1).equals(String.valueOf(till))){

            flowList.getFlows().add(parseFlow());

        }
        return flowList;

    }


    public Flow parseFlow(){
        Flow flow = new Flow();
        flow.setfName(readTillTo('{',';'));
        flow.setfType(readTillTo('{',';'));
        divideParamUnit(flow,readTillTo(';',';'));
        flow.setfParametr(Integer.parseInt(readTillTo(';','}')));
        linkedList.pop();
        return flow;

    }
}
