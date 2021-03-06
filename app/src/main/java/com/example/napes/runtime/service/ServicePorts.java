package com.example.napes.runtime.service;

import android.graphics.Color;

import com.example.napes.MainActivity;
import com.example.napes.clients.StaticClients;
import com.example.napes.clients.TcpClient;
import com.example.napes.clients.UdpClient;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.component.Component;
import com.example.napes.runtime.domains.ports.Port;
import com.example.napes.runtime.domains.ports.PortList;
import com.example.napes.runtime.service.servers.TcpServer;
import com.example.napes.runtime.service.servers.TcpServerNIO;
import com.example.napes.runtime.service.servers.UdpServer;

import java.util.ArrayList;

public class ServicePorts extends Service {
    ServiceFlows serviceFlows;
    ArrayList<ServiceStates> serviceStatesArrayList;

    public ServicePorts(Component component, MainActivity mainActivity,ArrayList<ServiceStates> serviceStatesArrayLis) {
        super(component, mainActivity);
        this.serviceStatesArrayList = serviceStatesArrayLis;
    }

    public void serviceServerPorts(){

        PortList portList = component.getPortList();
        System.out.println("PORTLIST!!!!!!!!!!!!!!!!!!!!!!"+portList);
        for (Port port: portList.getPorts()) {
            System.out.println("FOR PORTLIST : " +port.getpName());
            if (port.getpType().equals("s")){
                startServer(port.getpTransport(), port.getEndPointHere().getPort());
            }
            if (port.getpType().equals("c")){
                //startClient(port.getpTransport(), port.getClientInfo().getEndPoint().getPort(), port.getClientInfo().getEndPoint().getIP());
                startClient(port);
            }

        }
    }
    private void startClient(Port port) {
        System.out.println("STARTING CLIENT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (ServiceStates ss:serviceStatesArrayList) {
            System.out.println("LOOK : "+ss.getStateMachine().getmName()+"  "+ port.getClientInfo().getmName()+" PORT " +port.getpName()+" BOOL "+ ss.getStateMachine().getmName().equals(port.getClientInfo().getmName()));
           if (ss.getStateMachine().getmName().equals(port.getClientInfo().getmName())){
               serviceFlows= new ServiceFlows(port,handler,ss.getMap(),ss.getStateMachine(),component.getFlowList());
               serviceFlows.start();
           }

        }

//        if (pTransport.equals("U")) {
//            StaticClients.setUdpClient(new UdpClient(handler));
//            Config.udpPort = port;
//            Config.ipAddress = ipAdress;
//            StaticClients.getUdpClient().setParams("hello");
//            StaticClients.getUdpClient().start();
//        }
//        if (pTransport.equals("T")) {
//            StaticClients.setTcpClient(new TcpClient(handler));
//            Config.tcpPort = port;
//            StaticClients.getTcpClient().setParams("hello");
//            StaticClients.getTcpClient().start();
//        }
    }
    private void startServer(String pTransport, int port){
        if (pTransport.equals("T")){
            handler.setText("TCP server started on   >>>   "+port+"\n............................................................", Color.rgb(12,75,131));
//            TcpServer tcpServer = new TcpServer(port,handler);
//            tcpServer.start();
            TcpServerNIO tcpServerNIO = new TcpServerNIO(handler,port);
            tcpServerNIO.start();

        }
        else if (pTransport.equals("U")){
            handler.setText("UDP server started on   >>>   "+port+"\n............................................................", Color.rgb(15,129,11));
            udpServer = new UdpServer(port,handler);
            udpServer.start();
        }
    }
}
