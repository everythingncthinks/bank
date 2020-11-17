package com.nithin.cybrilla.assignment.bank.common.dto.payee;

import lombok.Data;

import java.util.Base64;

@Data
public class SameBankPayeeVerifyRequest {

    private String password;

    public void setPassword(String password) {
        this.password = Base64
                .getEncoder()
                .encodeToString(password.getBytes());
    }
}
