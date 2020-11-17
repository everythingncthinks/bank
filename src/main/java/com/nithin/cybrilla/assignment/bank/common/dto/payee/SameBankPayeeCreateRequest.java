package com.nithin.cybrilla.assignment.bank.common.dto.payee;

import lombok.Data;

@Data
public class SameBankPayeeCreateRequest {

    private Long accountNumber;

    private String accountHolderName;

    private String bankCode;

    private String displayName;
}
