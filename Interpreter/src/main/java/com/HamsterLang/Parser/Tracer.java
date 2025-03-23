package com.HamsterLang.Parser;

public class Tracer {
    private int traceLevel = 0;
    private static final String TRACE_IDENT_PLACEHOLDER = "\t";

    public String indentLevel(){
        return TRACE_IDENT_PLACEHOLDER.repeat(traceLevel-1);
    }

    public void tracePrint(String message){
        System.out.println(indentLevel() + message);
    }

    private void incIdent(){
        traceLevel++;
    }

    private void decIdent(){
        traceLevel--;
    }

    public String trace(String message){
        incIdent();
        tracePrint("BEGIN " + message);
        return message;
    }

    public void untrace(String message){
        tracePrint("END " + message);
        decIdent();
    }

}
