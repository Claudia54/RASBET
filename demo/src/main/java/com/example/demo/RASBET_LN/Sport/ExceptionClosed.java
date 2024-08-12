package com.example.demo.RASBET_LN.Sport;

public class ExceptionClosed extends Exception{

    private String message;

    public ExceptionClosed(){
        this.message = "This game is closed. Its state can't be changed.";
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}







