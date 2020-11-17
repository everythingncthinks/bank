package com.nithin.cybrilla.assignment.bank.common.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.tomcat.util.security.MD5Encoder;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Base64;

@Data
public class CreateUserRequest {

    @NotNull
    protected String userName;

    @NotNull
    protected String email;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    protected String userType;

    public void setPassword(String password) {
        this.password = Base64
                .getEncoder()
                .encodeToString(password.getBytes());
    }
}
