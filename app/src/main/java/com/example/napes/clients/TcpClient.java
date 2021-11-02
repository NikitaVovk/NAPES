package com.example.napes.clients;

import android.graphics.Color;

import com.example.napes.MainActivity;
import com.example.napes.config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TcpClient extends Thread {

    private static Socket s;
    private static InputStream isr;
    private static BufferedReader br;
    private static PrintWriter printWriter;
    String message ;
    String dstAddress ;
    int dstPort ;
    MainActivity handler;

    public TcpClient(MainActivity mainActivity) {
        super();
        handler = mainActivity;
    }

    public void setParams(String message) {

        dstAddress = Config.ipAddress;
        this.message = message;
        dstPort = Config.tcpPort;

    }

    @Override
    public void run() {
        try {
            s= new Socket(dstAddress,dstPort);
            printWriter = new PrintWriter(s.getOutputStream());
            printWriter.write(message);
            printWriter.flush();
            printWriter.close();
            s.close();
            handler.setText("\nTCP/Sent successfully "+ new Date().toString()+"\n", Color.GREEN);
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    }
