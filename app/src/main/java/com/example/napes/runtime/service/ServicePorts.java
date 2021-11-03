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
import com.example.napes.runtime.service.servers.UdpServer;

public class ServicePorts extends Service {
    public ServicePorts(Component component, MainActivity mainActivity) {
        super(component, mainActivity);
    }

    public void serviceServerPorts(){

        PortList portList = component.getPortList();
        for (Port port: portList.getPorts()) {
            if (port.getpType().equals("s")){
                startServer(port.getpTransport(), port.getEndPointHere().getPort());
            }
            if (port.getpType().equals("c")){
                startClient(port.getpTransport(), port.getClientInfo().getEndPoint().getPort(), port.getClientInfo().getEndPoint().getIP());
            }

        }
    }
    private void startClient(String pTransport, int port, String ipAdress) {
        System.out.println("STARTING CLIENT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (pTransport.equals("U")) {
            StaticClients.setUdpClient(new UdpClient(handler));
            Config.udpPort = port;
            Config.ipAddress = ipAdress;
            StaticClients.getUdpClient().setParams("hello");
            StaticClients.getUdpClient().start();
        }
        if (pTransport.equals("T")) {
            StaticClients.setTcpClient(new TcpClient(handler));
            Config.tcpPort = port;
            StaticClients.getTcpClient().setParams("hello");
            StaticClients.getTcpClient().start();
        }
    }
    private void startServer(String pTransport, int port){
        if (pTransport.equals("T")){
            handler.setText("\nStarting TCP server on port"+port+"...\n", Color.BLACK);
            TcpServer tcpServer = new TcpServer(port,handler);
            tcpServer.start();
        }
        else if (pTransport.equals("U")){
            handler.setText("\nStarting UDP server on port"+port+"...\n", Color.BLACK);
            udpServer = new UdpServer(port,handler);
            udpServer.start();
        }
    }
}
