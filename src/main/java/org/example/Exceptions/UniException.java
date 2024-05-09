package org.example.Exceptions;

public class UniException extends Exception {
    public UniException(String message, Exception reason){
        super(message,reason);
    }
    public UniException(String message){
        super(message);
    }
}
