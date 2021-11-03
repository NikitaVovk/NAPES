package com.example.napes.runtime.service;

import com.example.napes.MainActivity;
import com.example.napes.clients.StaticClients;
import com.example.napes.clients.TcpClient;
import com.example.napes.clients.UdpClient;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.ports.Port;
import com.example.napes.runtime.domains.ports.stateflow.StateFlow;
import com.example.napes.runtime.domains.statemachine.StateMachine;

import java.util.Map;

public class ServiceFlows extends Thread {


    Port port;
    MainActivity handler;
    Map<String,String> map;
    StateMachine stateMachine;

    public ServiceFlows(Port port, MainActivity handler, Map<String, String> map,StateMachine stateMachine) {
        this.port = port;
        this.handler = handler;
        this.map = map;
        this.stateMachine = stateMachine;
    }

    @Override
    public void run() {
        if (port.getpTransport().equals("U")) {
            System.out.println("MAP IN SERVICE FLOW:"+map);
while (true){
    System.out.println("thru while "+ map.get(stateMachine.getmName()));
            for (StateFlow stateFlow: port.getClientInfo().getStateFlowList().getStateFlows()) {
                if (map.get(stateMachine.getmName()).equals(stateFlow.getsName())){
                    while(map.get(stateMachine.getmName()).equals(stateFlow.getsName())) {
                        long timeOut = stateFlow.getAnonFlow().getFlows().get(0).getTimeParam();
                        timeOut*=1000;


                        System.out.println("CONFIGURING...");
                        StaticClients.setUdpClient(new UdpClient(handler));
                        Config.udpPort = port.getClientInfo().getEndPoint().getPort();
                        Config.ipAddress = port.getClientInfo().getEndPoint().getIP();
                        StaticClients.getUdpClient().setParams(map.get(stateMachine.getmName()));
                        System.out.println("CONFIGURING22222");
                        try {
                            Thread.sleep(timeOut);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        StaticClients.getUdpClient().start();
                    }

                }
            }
}
        }
        if (port.getpTransport().equals("T")) {
            StaticClients.setTcpClient(new TcpClient(handler));
            Config.tcpPort = port.getClientInfo().getEndPoint().getPort();
            StaticClients.getTcpClient().setParams("hello");
            StaticClients.getTcpClient().start();
        }
    }
}
