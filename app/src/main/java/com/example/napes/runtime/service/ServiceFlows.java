package com.example.napes.runtime.service;

import android.os.Process;

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
import com.example.napes.runtime.payloads.WriteToFileUtil;

import java.util.Map;

public class ServiceFlows extends Thread {


    Port port;
    MainActivity handler;
    Map<String, String> map;
    StateMachine stateMachine;
    FlowList flowList;
    long realClientTimeTCP;

    public ServiceFlows(Port port, MainActivity handler, Map<String, String> map, StateMachine stateMachine, FlowList flowList) {
        super("ServiceFlow");
        this.port = port;
        this.handler = handler;
        this.map = map;
        this.stateMachine = stateMachine;
        this.flowList = flowList;
    }

    public Flow findByfName(String fName) {

        for (Flow fl : flowList.getFlows()) {
            if (fl.getfName().equals(fName))
                return fl;
        }
        return null;
    }


    public Flow getCurrentFlow(StateFlow stateFlow) {
        Flow currentFlow = null;
        if (stateFlow.getAnonFlow() == null) {
            currentFlow = findByfName(stateFlow.getfName());

        } else
            currentFlow = stateFlow.getAnonFlow().getFlows().get(0);
        return currentFlow;

    }

    private int dedictPriority(String fName) {
        return Integer.parseInt(fName.replaceAll("f", ""));
    }

    @Override
    public void run() {
        //Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);


        // android.os.Process.setThreadPriority(Process.);
        boolean logLoaderUdp = false;
        String currentFlowBuf = "";
        long tempTime;
        boolean logLoaderTcp = false;
        //CHECK HASHMAP ::: IF IT CHANGES
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (port.getpTransport().equals("U")) {
            System.out.println("MAP IN SERVICE FLOW:" + map);
            while (true && Config.simulating) {
                System.out.println("thru while " + map.get(stateMachine.getmName()));
                for (StateFlow stateFlow : port.getClientInfo().getStateFlowList().getStateFlows()) {
                    if (map.get(stateMachine.getmName()).equals(stateFlow.getsName())) {

                        String currentTime = (Long.toString(System.currentTimeMillis()));
//                    handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":" + currentTime + ",\"ph\":\"B\",\"cat\":\"service_states\",\"name\":\"" +
//                            getCurrentFlow(stateFlow).getfType()
//                            + "\",\"args\":{}},", handler);
                        handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":" + currentTime +
                                ",\"ph\":\"b\",\"cat\":\"service_flows\",\"name\":\"" + getCurrentFlow(stateFlow).getfType()
                                + "\",\"id\": 2,\"args\":{}},", handler);

                        this.setPriority(dedictPriority(getCurrentFlow(stateFlow).getfName()));

                        UdpClient udpClient = new UdpClient(handler, port);
                        //StaticClients.setUdpClient(new UdpClient(handler,port));
                        long timer = System.nanoTime();
                        while (map.get(stateMachine.getmName()).equals(stateFlow.getsName()) && Config.simulating) {


                            Flow currentFlow = getCurrentFlow(stateFlow);
                            long timeOut = currentFlow.getRealTimeDelay() * 1_000_000;
                            currentFlowBuf = currentFlow.getfType();
                            //  this.setPriority(dedictPriority(getCurrentFlow(stateFlow).getfName()));


                            //timeOut*=1000;

                            logLoaderUdp = !logLoaderUdp;

                            udpClient.setParams("message", currentFlow, logLoaderUdp);
                            //StaticClients.getUdpClient().setParams("message",currentFlow,logLoaderUdp);

                            //System.out.println("CONFIGURING...");
                        /* 21.12.2022 poprawka
                        logLoaderUdp = !logLoaderUdp;
                        StaticClients.setUdpClient(new UdpClient(handler,logLoaderUdp));

                        Config.udpPort = port.getClientInfo().getEndPoint().getPort();
                        Config.ipAddress = port.getClientInfo().getEndPoint().getIP();
                        StaticClients.getUdpClient().setParams("{"+handler.getComponent().getcName()+"} --- {"+stateMachine.getmName()+"} --- {"+
                                map.get(stateMachine.getmName())+"} --- {"+currentFlow.getfType()+"}",currentFlow);
*/

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


                            // StaticClients.getUdpClient().start();
                            udpClient.sendThroughLink();
                            //   StaticClients.getUdpClient().sendThroughLink();


                            tempTime = System.nanoTime() - timer;

                            //     synchronized (map){
                            // try {
                            if (tempTime - timeOut >= 0) {
                                timer = System.nanoTime();

                                // map.wait(1);
                                continue;
                            } else {
                                //  System.out.println("Splyu!!!!!!!!!!!"+timer);
                                timer = System.nanoTime() - timer;
                                //  System.out.println("TIME TO SLEEP: "+(timeOut-timer));
//                                    if ((timeOut-timer)<=0)
//                                        continue;
//                                    System.out.println("BEEEEEEE "+ timeOut);
//                                    try {
//                                        long ttwMs =  (timeOut-timer)/1000_000;
//                                        int ttwNs =  (int)(timeOut-timer)%1_000_000;
//                                        // thread to sleep for 1000 milliseconds plus 500 nanoseconds
//                                        Thread.sleep(ttwMs, ttwNs);
//                                    } catch (Exception e) {
//                                        System.out.println(e);
//                                    }
                                busySleep(timeOut - timer);
                                // map.wait(timeOut-timer);
                                timer = System.nanoTime();//System.currentTimeMillis();
                            }
                            //  System.out.println("Ne splyu!!!!!!!!!!!"+timeOut);

                            // }
                        }
                        WriteToFileUtil wfUtil = new WriteToFileUtil(handler.getApplicationContext().getFilesDir(),
                                stateFlow.getfName(), udpClient.getSentTimeList());
                        wfUtil.start();
                        //  udpClient.finalize();
                        // StaticClients.getUdpClient().finalize();
                        String currentTimeEnd = (Long.toString(System.currentTimeMillis()));
//                    handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":"+currentTimeEnd+",\"ph\":\"E\",\"cat\":\"service_states\",\"name\":\""+
//                            currentFlowBuf
//                            +"\",\"args\":{}},",handler);
                        handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":" + currentTimeEnd +
                                ",\"ph\":\"e\",\"cat\":\"service_flows\",\"name\":\"" + currentFlowBuf
                                + "\",\"id\": 2,\"args\":{}},", handler);


                    }
                }
            }
        }
        if (port.getpTransport().equals("T")) {
            TcpClientNIO tcpClientNIO = new TcpClientNIO(port.getClientInfo().getEndPoint().getIP(),
                    port.getClientInfo().getEndPoint().getPort(), port.getEndPointHere().getPort(), handler);
            tcpClientNIO.connect();

            //System.out.println(" Is CONNECTED : "+tcpClient.isConnected());

            System.out.println("MAP IN SERVICE FLOW:" + map);
            while (true && Config.simulating) {

//                Config.tcpPort = port.getClientInfo().getEndPoint().getPort();
//                Config.ipAddressTcp = port.getClientInfo().getEndPoint().getIP();
//                System.out.println("HEREEEEEEE"+Config.ipAddressTcp+Config.tcpPort );
//                TcpClient tcpClient = new TcpClient(handler);


                System.out.println("thru while " + map.get(stateMachine.getmName()));
                for (StateFlow stateFlow : port.getClientInfo().getStateFlowList().getStateFlows()) {
                    if (map.get(stateMachine.getmName()).equals(stateFlow.getsName())) {
                        while (map.get(stateMachine.getmName()).equals(stateFlow.getsName()) && Config.simulating) {

                            realClientTimeTCP = System.currentTimeMillis();

                            Flow currentFlow = getCurrentFlow(stateFlow);
                            long timeOut = currentFlow.getRealTimeDelay();
//                            long timeOut = stateFlow.getAnonFlow().getFlows().get(0).getTimeParam();
                            // timeOut*=1000;


                            System.out.println("CONFIGURING..." + map.get(stateMachine.getmName()));

                            //  StaticClients.setTcpClient(new TcpClient(handler));

                            // StaticClients.getTcpClient().setParams(map.get(stateMachine.getmName()));

                            //    tcpClient.setParams(map.get(stateMachine.getmName()));

                            tcpClientNIO.setParams("{" + handler.getComponent().getcName() + "} --- {" + stateMachine.getmName() + "} --- {" +
                                    map.get(stateMachine.getmName()) + "} --- {" + currentFlow.getfType() + "}", currentFlow);

//                            try {
//                                Thread.currentThread().sleep(timeOut);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }

                            tcpClientNIO.run();
                            synchronized (map) {
                                try {
                                    map.wait(timeOut);
                                    realClientTimeTCP = System.currentTimeMillis() - realClientTimeTCP;
                                    System.out.println("REAL TIME CLIENT TCP :::::::: " + realClientTimeTCP);

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

    public void busySleep(long nanos) {
        long elapsed;
        final long startTime = System.nanoTime();
        do {
            elapsed = System.nanoTime() - startTime;
        } while (elapsed < nanos);
    }
}
