package com.nithin.cybrilla.assignment.bank.common.exception;

public class PayeeDetailsNotMatchingException extends RuntimeException {

    public PayeeDetailsNotMatchingException(){
        super("Given Payee details do not match with the payee account number.");
    }
}
