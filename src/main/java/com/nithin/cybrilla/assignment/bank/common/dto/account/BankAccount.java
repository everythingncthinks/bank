package com.nithin.cybrilla.assignment.bank.common.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class BankAccount extends BankAccountUpdateRequest {

    protected Long userId;

    protected String accountStatus;

    protected String accountType;

    protected String bankCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected BigDecimal balance;

    protected boolean isActive = true;
}
