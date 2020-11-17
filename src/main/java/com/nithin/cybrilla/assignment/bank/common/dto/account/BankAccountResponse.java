package com.nithin.cybrilla.assignment.bank.common.dto.account;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountResponse extends BankAccount {

    private Long accountNumber;

    private BigDecimal balance;
}
