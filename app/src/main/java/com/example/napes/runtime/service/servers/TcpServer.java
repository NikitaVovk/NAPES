package com.example.napes.runtime.service.servers;

import com.example.napes.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Thread {
    private  ServerSocket ss;
    private  Socket s;
    private  BufferedReader br;
    private  InputStreamReader isr;
    private  String message = "";
    private  PrintWriter printWriter;
    private int port;
    private MainActivity handler;

    public TcpServer(int port,MainActivity mainActivity) {
        super();
        this.port = port;
        handler = mainActivity;

    }

    @Override
    public void run() {
        try {
            while (true){
                ss = new ServerSocket(port);
                s=ss.accept();
                isr = new InputStreamReader(s.getInputStream());
                br = new BufferedReader(isr);
                message = br.readLine();

                System.out.println("Request from: " + s.getInetAddress() + ":" + s.getPort());
                System.out.println("Received data: "+ message);
                isr.close();
                br.close();
                ss.close();
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
