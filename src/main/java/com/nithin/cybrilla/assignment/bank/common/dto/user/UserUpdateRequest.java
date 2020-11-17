package com.nithin.cybrilla.assignment.bank.common.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Base64;

@Data
public class UserUpdateRequest {

    @NotNull
    protected String email;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String password;

    public void setPassword(String password) {
        this.password = Base64
                .getEncoder()
                .encodeToString(password.getBytes());
    }
}
