package com.example.billservice.exception;

public class BillAlreadyPaidException extends RuntimeException{
    public BillAlreadyPaidException(String message){
        super(message);
    }
}
