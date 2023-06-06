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
import java.util.concurrent.TimeUnit;

public class ServiceFlows extends Thread {


    Port port;
    MainActivity handler;
    Map<String, String> map;
    StateMachine stateMachine;
    FlowList flowList;
    long realClientTimeTCP;

    //Konstruktor klasy
    public ServiceFlows(Port port, MainActivity handler, Map<String, String> map, StateMachine stateMachine, FlowList flowList) {
        super("ServiceFlow");
        this.port = port;
        this.handler = handler;
        this.map = map;
        this.stateMachine = stateMachine;
        this.flowList = flowList;
    }

    // Funkcja pomocnicza zwracająca obiekt typu Flow za jego nazwą
    public Flow findByfName(String fName) {

        for (Flow fl : flowList.getFlows()) {
            if (fl.getfName().equals(fName))
                return fl;
        }
        return null;
    }


    // Funkcja zwracająca aktualny stan FSM
    public Flow getCurrentFlow(StateFlow stateFlow) {
        Flow currentFlow = null;
        if (stateFlow.getAnonFlow() == null) {
            currentFlow = findByfName(stateFlow.getfName());

        } else
            currentFlow = stateFlow.getAnonFlow().getFlows().get(0);
        return currentFlow;

    }

    private int getPriority(Flow flow) {
        return Integer.parseInt(flow.getfType());
    }

    @Override
    public void run() {

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


        if (port.getpTransport().equals("U")) {   // warunek na sprawdzenie typu klienta


            while ( Config.simulating) { // do póki flaga symulacji == true


                // pętla sprawdzająca każdy state flow
                for (StateFlow stateFlow : port.getClientInfo().getStateFlowList().getStateFlows()) {

                    // deklaracja parametrów StateFlow
                    String stateName = stateFlow.getsName();
                    String stateMachineName = stateMachine.getmName();

                    // sprawdź czy przetwarzany StateFlow  zgadza się z aktualnym stanem FSM
                    if (map.get(stateMachineName).equals(stateName)) {

                        // zapis czasu rozpoczęcia przepływu do pliku JSON
                        String currentTime = (Long.toString(System.currentTimeMillis()));
                        handler.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":" + currentTime +
                                ",\"ph\":\"b\",\"cat\":\"service_flows\",\"name\":\"" + getCurrentFlow(stateFlow).getfType()
                                + "\",\"id\": 2,\"args\":{}},", handler);





                        // ustaw aktualny przepływ na porcie
                        Flow currentFlow = getCurrentFlow(stateFlow);

                        Thread.currentThread().setName(currentFlow.getfName()+"("+currentFlow.getRealTimeDelay()+" ms, "+currentFlow.getfParametr()+")"); // ustaw nazwę wątku
                        android.os.Process.setThreadPriority(getPriority(currentFlow)); //ustawienie priorytetu wątków
                       // android.os.Process.se

                       // android.os.Process.sendSignal(tid, Process.SCHED_FIFO, prio);

                        //tworzenie klienta
                        UdpClient udpClient = new UdpClient(handler, port,currentFlow);

                        // ustawienie odstępów między pakietami
                        long timeOut = currentFlow.getRealTimeDelay() * 1_000_000;
                        currentFlowBuf = currentFlow.getfType();

                        // zapis czasu przed przetwarzaniem
                        long timer = System.nanoTime();

                        // pętla przetwarzająca przepływ
                        while (map.get(stateMachine.getmName()).equals(stateName) && Config.simulating) {



                            udpClient.sendThroughLink(); // wysłanie pakietu


                            tempTime = System.nanoTime() - timer;


                            // Jeśli czas przetwarzania był większy niż odstęp między pakietami, to kontynuuj pętlę
                            if (tempTime - timeOut >= 0) {
                                timer = System.nanoTime();
                                continue;
                            } else {

                                long timeToSleepNs;
                                timer = System.nanoTime() - timer; // uwzglednianie czasu przetwarzania
                                timeToSleepNs = timeOut-timer; // odstęp czasowy - czas przetwarzania
                               // busySleep(timeOut - timer);

                                try {
                                    // czekanie
                                    TimeUnit.NANOSECONDS.sleep(timeToSleepNs);
                            } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                // ustaw timer przetwarzania
                                timer = System.nanoTime();
                            }



                        }
                        // zapisz listę czasów do pliku
                        WriteToFileUtil wfUtil = new WriteToFileUtil(handler.getApplicationContext().getFilesDir(),
                                stateFlow.getfName(), udpClient.getSentTimeList());
                        wfUtil.start();

                        // log mówiący o końcu przetwarzania danego Flow
                        String currentTimeEnd = (Long.toString(System.currentTimeMillis()));
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



            System.out.println("MAP IN SERVICE FLOW:" + map);
            while (Config.simulating) {




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
