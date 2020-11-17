package com.nithin.cybrilla.assignment.bank.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TestUserUpdateRequest {

    @NotNull
    protected String email;

    @NotNull
    protected String password;

}
