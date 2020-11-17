package com.nithin.cybrilla.assignment.bank.common.dto.transaction;

import com.nithin.cybrilla.assignment.bank.common.enums.TransactionType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class EmployeeTransactionRequest {

    @NotNull
    private Long accountNumber;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private String message;
}
