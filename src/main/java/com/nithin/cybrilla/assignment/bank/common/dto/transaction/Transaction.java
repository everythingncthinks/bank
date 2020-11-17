package com.nithin.cybrilla.assignment.bank.common.dto.transaction;

import com.nithin.cybrilla.assignment.bank.common.enums.TransactionType;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Transaction {

    private Long transactionId;

    private Long accountNumber;

    private TransactionType transactionType;

    private String transactionStatus;

    private String message;

    private OffsetDateTime transactionDateTime;

    private String createdBy;
}
