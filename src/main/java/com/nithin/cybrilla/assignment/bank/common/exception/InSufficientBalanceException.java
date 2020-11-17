package com.nithin.cybrilla.assignment.bank.common.exception;

public class InSufficientBalanceException extends RuntimeException {

    public InSufficientBalanceException(String currentBalance) {
        super(String.format("Balance in your account is insufficient to make this transaction. Current Balance is %s",
                currentBalance));
    }
}
