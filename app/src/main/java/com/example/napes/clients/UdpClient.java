package com.example.napes.clients;

import android.app.NativeActivity;
import android.graphics.Color;
import android.os.Message;

import com.example.napes.MainActivity;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.flows.Flow;
import com.example.napes.runtime.domains.ports.Port;
import com.example.napes.runtime.service.payload.Colors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class UdpClient {

    // Parametry klienta
    String dstAddress, message = "";
    int dstPort;
    Flow flow;
    InetAddress address;

    List<Long> sentTimeList; // lista czasów wysłanych pakietów


    DatagramSocket socket; // Obiekt portu
    DatagramPacket packet; // pakiet


    MainActivity handler; // handler dla logów



    public List<Long> getSentTimeList() {
        return sentTimeList;
    } // getter dla listy czasów



    // Konstruktor
    public UdpClient(MainActivity mainActivity, Port port, Flow flow) {
      //  super();
        // ustawienia parametrów klienta
        sentTimeList = new ArrayList<>();
        handler = mainActivity;
        dstPort = port.getClientInfo().getEndPoint().getPort();
        dstAddress = port.getClientInfo().getEndPoint().getIP();
        this.flow = flow;



        // send request
       // int max = 1;
        // int max = 256;
      //  int max = 512;
      // int max = 1024;
        //int max = 2048;
        //int max = 4096;
      //   int max = 8192;
        // int max = 16384;
        // int max = 32768;
        // int max =65507;
        //  int max = 32500;
        // System.out.println("rozmiar pakietu: "+flow.getfParametr());

        byte[] buf = new byte[flow.getfParametr()]; // alokacja pamięci dla wysyłających pakietów



        try {
            address = InetAddress.getByName(dstAddress);
            packet = new DatagramPacket(buf, buf.length, address, dstPort); // tworzenie pakietu (tutaj podają się paramtry węzła docelowego)
            socket = new DatagramSocket(port.getEndPointHere().getPort()); // otwarcie podanego portu na komponencie

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }






    synchronized public void sendThroughLink() {
        try {




//            System.out.println("SENDING UDP TO :" + address + ":" + dstPort);
//            System.out.println("SIZE :" + sentTimeList.size());
//            System.out.println("ANDROID PRIORITY :" + Process.getThreadPriority(Process.myTid()));
//            System.out.println("JAVA PRIORITY :" + Thread.currentThread().getPriority());

            socket.send(packet); // wysyłanie pakietu
            // long sentTimel = System.currentTimeMillis();
            long sentTimel = System.nanoTime() ; //notacja czasu

            sentTimeList.add(sentTimel); // dodanie czasu do listy

            //JSON LOGS
//            handler.addLog("{\"pid\":\"Node1\",\"tid\":\"packetSend\",\"ts\":"+sentTime+",\"ph\":\"E\",\"cat\":\"sequence_manager\",\"name\":\""+
//                    (payLoader?"k":"k+1")
//                    +"\",\"args\":{}},",handler);
//            handler.addLog("{\"pid\":\"Node1\",\"tid\":\"packetSend\",\"ts\":"+sentTime+",\"ph\":\"B\",\"cat\":\"sequence_manager\",\"name\":\""+
//                    (payLoader?"k+1":"k")
//                    +"\",\"args\":{}},",handler);

            //skomentowano 21_01
            //   handler.addLogTime(sentTime,handler,Thread.currentThread().getId()+"___"+flow.getfName()+"___"+flow.getRealTimeDelay());




        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }


    @Override
    public void finalize() {
        if (socket != null) {
            socket.close();
            // System.out.println("KONIEEC");
        }
    }


//    @Override
//    public void run() {
//
//       // running = true;
//        this.sendThroughLink();
//
//    }
}