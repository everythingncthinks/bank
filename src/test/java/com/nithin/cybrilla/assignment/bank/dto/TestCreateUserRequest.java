package com.nithin.cybrilla.assignment.bank.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TestCreateUserRequest {

    @NotNull
    protected String userName;

    @NotNull
    protected String email;

    @NotNull
    private String password;

    @NotNull
    protected String userType;
}
