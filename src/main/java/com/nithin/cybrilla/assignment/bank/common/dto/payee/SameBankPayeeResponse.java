package com.nithin.cybrilla.assignment.bank.common.dto.payee;

import lombok.Data;

@Data
public class SameBankPayeeResponse {

    private Long payeeId;

    private Long accountNumber;

    private String accountHolderName;

    private String bankCode;

    private String displayName;

    private boolean isVerified;
}
