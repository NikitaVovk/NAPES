package com.example.napes.runtime.parser;



import com.example.napes.runtime.domains.flows.Flow;

import java.util.LinkedList;

public class PayLoadParser {
    public LinkedList<String> linkedList;

    public PayLoadParser(LinkedList<String> linkedList) {
        this.linkedList = linkedList;
    }
    public  String readTillTo(char from, char to){

        while (!linkedList.getFirst().equals(String.valueOf(from)))
            linkedList.pop();
        linkedList.pop();
        String text="";
        while (!linkedList.getFirst().equals(String.valueOf(to))){

            text= text+linkedList.pop();
        }
        return text;
    }
    public  String readTillTo2(char from, char to,char to2){

        while (!linkedList.getFirst().equals(String.valueOf(from)))
            linkedList.pop();
        linkedList.pop();
        String text="";
        while (!linkedList.getFirst().equals(String.valueOf(to))&&
                !linkedList.getFirst().equals(String.valueOf(to2))){
            text= text+linkedList.pop();
        }
        return text;
    }
    public static void divideParamUnit(Flow flow, String textToDivide){
        String time;
        String unit;
        flow.setTimeParam( Integer.parseInt(textToDivide.replaceAll("[^0-9]", "")));
        flow.setUnit(textToDivide.replaceAll("[^a-z]", ""));

    }
}
