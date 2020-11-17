package com.nithin.cybrilla.assignment.bank.common.dto.user;

import lombok.Data;

@Data
public class EmployeeUserUpdateRequest extends UserUpdateRequest {

    private boolean isActive;

}
