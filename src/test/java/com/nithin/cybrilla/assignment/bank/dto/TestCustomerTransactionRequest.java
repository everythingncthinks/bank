package com.nithin.cybrilla.assignment.bank.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TestCustomerTransactionRequest {

    @NotNull
    private Long accountNumber;

    @NotNull
    private Long payeeId;

    @NotNull
    private String message;

    @NotNull
    private BigDecimal amount;

    @NotNull
    protected String password;
}
