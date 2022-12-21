package com.example.napes.runtime.service;

import com.example.napes.MainActivity;
import com.example.napes.clients.StaticClients;
import com.example.napes.clients.TcpClient;
import com.example.napes.clients.TcpClientNIO;
import com.example.napes.clients.UdpClient;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.flows.Flow;
import com.example.napes.runtime.domains.flows.FlowList;
import com.example.napes.runtime.domains.ports.Port;
import com.example.napes.runtime.domains.ports.stateflow.StateFlow;
import com.example.napes.runtime.domains.statemachine.StateMachine;

import java.util.Map;

public class ServiceFlows extends Thread {


    Port port;
    MainActivity handler;
    Map<String,String> map;
    StateMachine stateMachine;
    FlowList flowList;
    long realClientTimeTCP;

    public ServiceFlows(Port port, MainActivity handler, Map<String, String> map,StateMachine stateMachine, FlowList flowList) {
        super("ServiceFlow");
        this.port = port;
        this.handler = handler;
        this.map = map;
        this.stateMachine = stateMachine;
        this.flowList = flowList;
    }

    public Flow findByfName (String fName){

        for (Flow fl: flowList.getFlows()) {
            if (fl.getfName().equals(fName))
                return fl;
        }
        return null;
    }


    public Flow getCurrentFlow(StateFlow stateFlow){
        Flow currentFlow = null;
        if (stateFlow.getAnonFlow()==null){
            currentFlow = findByfName(stateFlow.getfName());

        }
        else
            currentFlow = stateFlow.getAnonFlow().getFlows().get(0);
        return currentFlow;

    }


    @Override
    public void run() {
        boolean logLoaderUdp = false;
        String currentFlowBuf = "";

        boolean logLoaderTcp = false;
        //CHECK HASHMAP ::: IF IT CHANGES
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (port.getpTransport().equals("U")) {
            System.out.println("MAP IN SERVICE FLOW:"+map);
while (true&&Config.simulating){
    System.out.println("thru while "+ map.get(stateMachine.getmName()));
            for (StateFlow stateFlow: port.getClientInfo().getStateFlowList().getStateFlows()) {
                if (map.get(stateMachine.getmName()).equals(stateFlow.getsName())){

                    String currentTime = (Long.toString(System.currentTimeMillis()));
//                    handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":" + currentTime + ",\"ph\":\"B\",\"cat\":\"service_states\",\"name\":\"" +
//                            getCurrentFlow(stateFlow).getfType()
//                            + "\",\"args\":{}},", handler);
                    handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":"+currentTime+
                            ",\"ph\":\"b\",\"cat\":\"service_flows\",\"name\":\""+getCurrentFlow(stateFlow).getfType()
                            +"\",\"id\": 2,\"args\":{}},",handler);
                    while(map.get(stateMachine.getmName()).equals(stateFlow.getsName())&&Config.simulating) {




                        Flow currentFlow = getCurrentFlow(stateFlow);
                        long timeOut = currentFlow.getRealTimeDelay();
                        currentFlowBuf = currentFlow.getfType();


                        //timeOut*=1000;


                        //System.out.println("CONFIGURING...");
                        logLoaderUdp = !logLoaderUdp;
                        StaticClients.setUdpClient(new UdpClient(handler,logLoaderUdp));

                        Config.udpPort = port.getClientInfo().getEndPoint().getPort();
                        Config.ipAddress = port.getClientInfo().getEndPoint().getIP();
                        StaticClients.getUdpClient().setParams("{"+handler.getComponent().getcName()+"} --- {"+stateMachine.getmName()+"} --- {"+
                                map.get(stateMachine.getmName())+"} --- {"+currentFlow.getfType()+"}",currentFlow);


//                        UdpClient udpClient =  new UdpClient(handler,logLoaderUdp);
//                        udpClient.setParams("mes",currentFlow);
                      //  System.out.println("CONFIGURING22222");
//                        try {
//                            Thread.currentThread().sleep(timeOut); //its better to wait (long timeout) and notify when state changes in State fsm
//                            //object for synchronization will be hashmap which is idential for each fsm
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                      //  udpClient.run();
                        StaticClients.getUdpClient().start();
                        synchronized (map){
                            try {
                                map.wait(timeOut);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    String currentTimeEnd = (Long.toString(System.currentTimeMillis()));
//                    handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":"+currentTimeEnd+",\"ph\":\"E\",\"cat\":\"service_states\",\"name\":\""+
//                            currentFlowBuf
//                            +"\",\"args\":{}},",handler);
                    handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":"+currentTimeEnd+
                            ",\"ph\":\"e\",\"cat\":\"service_flows\",\"name\":\""+currentFlowBuf
                            +"\",\"id\": 2,\"args\":{}},",handler);


                }
            }
}
        }
        if (port.getpTransport().equals("T")) {
            TcpClientNIO tcpClientNIO = new TcpClientNIO(port.getClientInfo().getEndPoint().getIP(),
                    port.getClientInfo().getEndPoint().getPort(),port.getEndPointHere().getPort(),handler);
            tcpClientNIO.connect();

            //System.out.println(" Is CONNECTED : "+tcpClient.isConnected());

            System.out.println("MAP IN SERVICE FLOW:"+map);
            while (true&&Config.simulating){

//                Config.tcpPort = port.getClientInfo().getEndPoint().getPort();
//                Config.ipAddressTcp = port.getClientInfo().getEndPoint().getIP();
//                System.out.println("HEREEEEEEE"+Config.ipAddressTcp+Config.tcpPort );
//                TcpClient tcpClient = new TcpClient(handler);


                System.out.println("thru while "+ map.get(stateMachine.getmName()));
                for (StateFlow stateFlow: port.getClientInfo().getStateFlowList().getStateFlows()) {
                    if (map.get(stateMachine.getmName()).equals(stateFlow.getsName())){
                        while(map.get(stateMachine.getmName()).equals(stateFlow.getsName())&&Config.simulating) {

                            realClientTimeTCP= System.currentTimeMillis();

                            Flow currentFlow = getCurrentFlow(stateFlow);
                            long timeOut = currentFlow.getRealTimeDelay();
//                            long timeOut = stateFlow.getAnonFlow().getFlows().get(0).getTimeParam();
                           // timeOut*=1000;


                            System.out.println("CONFIGURING..."+map.get(stateMachine.getmName()));

                          //  StaticClients.setTcpClient(new TcpClient(handler));

                           // StaticClients.getTcpClient().setParams(map.get(stateMachine.getmName()));

                        //    tcpClient.setParams(map.get(stateMachine.getmName()));

                            tcpClientNIO.setParams("{"+handler.getComponent().getcName()+"} --- {"+stateMachine.getmName()+"} --- {"+
                                    map.get(stateMachine.getmName())+"} --- {"+currentFlow.getfType()+"}",currentFlow);

//                            try {
//                                Thread.currentThread().sleep(timeOut);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }

                            tcpClientNIO.run();
                            synchronized (map){
                                try {
                                    map.wait(timeOut);
                                    realClientTimeTCP = System.currentTimeMillis() - realClientTimeTCP;
                                    System.out.println("REAL TIME CLIENT TCP :::::::: "+ realClientTimeTCP);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                          //  tcpClient.run();
                            //StaticClients.getTcpClient().start();
                           // StaticClients.setTcpClient(null);
                        }
                        System.out.println("CLosing socket");


                    }
                }
              //  tcpClient.closeSocket();
            }
            //tcpClientNIO.closeSocket();
        }
    }
}
