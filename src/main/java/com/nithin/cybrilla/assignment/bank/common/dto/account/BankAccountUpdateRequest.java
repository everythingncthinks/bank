package com.nithin.cybrilla.assignment.bank.common.dto.account;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class BankAccountUpdateRequest {

    protected String firstName;

    protected String lastName;
}
