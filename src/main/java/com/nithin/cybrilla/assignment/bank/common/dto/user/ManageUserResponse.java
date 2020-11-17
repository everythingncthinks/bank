package com.nithin.cybrilla.assignment.bank.common.dto.user;

import lombok.Data;
import org.apache.tomcat.util.security.MD5Encoder;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class ManageUserResponse {

    private Long userId;

    private boolean isActive = true;

}
