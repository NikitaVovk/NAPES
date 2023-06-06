package com.example.napes.runtime.payloads;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WriteToFileUtil extends Thread{
    private File filesDir;
    private String fileName;
    private List<Long> valueList;

    public WriteToFileUtil(File filesDir, String fileName, List<Long> value) {
        this.filesDir = filesDir;
        this.fileName = fileName;
        this.valueList = value;
        System.out.println("VALUE LIST SIZE "+valueList.size());
    }

    @Override
    public void run() {
        FileOutputStream fOut = null;

        // handler.setText("UDP/Sent successfully packet:\n@     Flow     >>>     {"+ this.flow.getfType()+"}\n", Colors.udpColor);
        File file = Environment.getExternalStorageDirectory();
        System.out.println("############: DIRECTORY:" + filesDir+"\t"+valueList.size());
       // System.out.println("############: DIRECTORY:" +file.getAbsolutePath()+Environment.DIRECTORY_DOWNLOADS+"/time.txt" );

        try {
            // openFileInput()

            // fOut = openFileOutput("savedData.txt",  MODE_APPEND);
            fOut = new FileOutputStream(new File(filesDir, fileName), true); // bought samsung
            // fOut = new FileOutputStream(new File( file.getAbsolutePath(),Environment.DIRECTORY_DOWNLOADS+"/time.txt"), true); //teplokram

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {

            for (Long value:valueList) {
                System.out.println(value);
                value = value/1000;
                fOut.write((value.toString() + "\n").getBytes());
            }
            System.out.println("###########: !MAYBE SAVED ");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                    System.out.println("CLOSEEEEE");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
