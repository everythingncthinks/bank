package com.nithin.cybrilla.assignment.bank.common.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Base64;

@Data
public class CustomerTransactionRequest {

    @NotNull
    private Long accountNumber;

    @NotNull
    private Long payeeId;

    @NotNull
    private String message;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String password;

    public void setPassword(String password) {
        this.password = Base64
                .getEncoder()
                .encodeToString(password.getBytes());
    }
}
