package com.example.napes.runtime.parser;



import android.os.Environment;

import com.example.napes.runtime.domains.component.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class MainParser {

    ComponentParser componentParser;
    String filePath;


    public MainParser(String filePath) throws FileNotFoundException {
        File file = Environment.getExternalStorageDirectory();
        System.out.println("ABSOLUTE PATH: !!!!"+file.getAbsolutePath()+"/Documents/"+filePath);
        Scanner scanner;
      //  scanner = new Scanner(new File(file.getAbsolutePath()+"/Documents/"+filePath));
        scanner = new Scanner(new File(filePath));
        scanner.useDelimiter("");


       LinkedList<String> linkedList = new LinkedList<>();
        while (scanner.hasNext()){
            linkedList.add(scanner.next());
        }
        componentParser = new ComponentParser(linkedList);
    }

    public Component getComponent(){
        return componentParser.parseComponent();
    }


}
