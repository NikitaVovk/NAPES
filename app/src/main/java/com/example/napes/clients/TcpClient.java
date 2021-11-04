package com.example.napes.clients;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.example.napes.MainActivity;
import com.example.napes.config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;

public class TcpClient extends Thread {

    private  Socket s;
    private static InputStream isr;
    private static BufferedReader br;
    private static PrintWriter printWriter;
    String message ;
    String dstAddress ;
    int dstPort ;
    MainActivity handler;

    public boolean isConnected(){
        return s.isConnected();
    }
    public void closeSocket(){
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public TcpClient(MainActivity mainActivity) {
        super();
        handler = mainActivity;
        dstAddress = Config.ipAddressTcp;
        dstPort = Config.tcpPort;

        s = new Socket();
        try {
            s.setReuseAddress(true);
             s.bind(new InetSocketAddress(56000));
            SocketAddress sockaddr = new InetSocketAddress(dstAddress, dstPort);
            s.connect(sockaddr);
            s.setKeepAlive(true);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void setParams(String message) {


        try {
            printWriter = new PrintWriter(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.message = message;


    }


    @Override
    public void run() {
  //      try {



            System.out.println("TCP SENDING "+dstAddress+":"+dstPort);
            //Socket s = new Socket();

//        s = new Socket();
//        try {
//            s.setReuseAddress(true);
//            // s.bind(new InetSocketAddress(56000));
//            SocketAddress sockaddr = new InetSocketAddress(dstAddress, dstPort);
//            s.connect(sockaddr);
//            s.setKeepAlive(true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //   s= new Socket(dstAddress,dstPort);


//             s.setReuseAddress(true);
//             s.setTcpNoDelay(true);
//
//            SocketAddress sockaddr = new InetSocketAddress(dstAddress, dstPort);
//                //s.bind(new InetSocketAddress(8004));
//                s.setSoTimeout(10);
//                s.connect(sockaddr);
//                s.setKeepAlive(false);
//
//            System.out.println( s.getLocalPort());


//замутити в  окремий потік або замутити обєкт з тсп клієнтом де сокєт буде біндитись один раз

//        try {
//            printWriter = new PrintWriter(s.getOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
            printWriter.write(message);
            printWriter.flush();
          //  printWriter.close();
            System.out.println("CLOSING SOCKET");
           //s.shutdownOutput();
           // s.bind(null);


//        try {
//            s.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        handler.setText("\nTCP/Sent successfully "+ new Date().toString()+"\n", Color.GREEN);
//        }catch (IOException e){
//            e.printStackTrace();
//        }

    }


    }
