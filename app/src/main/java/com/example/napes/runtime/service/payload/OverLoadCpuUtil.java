package com.example.napes.runtime.service.payload;

import com.example.napes.config.Config;

import java.math.BigInteger;

public class OverLoadCpuUtil extends Thread {

    private int numOfThreads;
    public OverLoadCpuUtil() {
        super("cpu.load.parent");
        numOfThreads =   Runtime.getRuntime().availableProcessors();
        System.out.println("AVAILABLE PROCESSORS"+ numOfThreads);

    }

    public void ovrld(){


    }
    @Override
    public void run(){
        for (int i = 0 ; i<numOfThreads;i++){
            Thread thread = new Thread("napes.cpu.load"){
                @Override
                public void run(){

                    BigInteger bi = new BigInteger("0");
                    BigInteger bj = new BigInteger("0");
                    while (bi.compareTo(new BigInteger("200000000"))<0){
                        bi = bi.add(new BigInteger("1"));
                        bj = bj.add(bi);
                    }

                }
            };
            thread.start();

        }
    }
}
