package com.nithin.cybrilla.assignment.bank.employee.service;

import com.nithin.cybrilla.assignment.bank.common.dto.user.CreateUserRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.user.ManageUserResponse;
import com.nithin.cybrilla.assignment.bank.common.service.UserService;

/**
 * user service for employees
 */
public interface EmployeeUserService extends UserService {

    /**
     * method to create the user info.
     */
    ManageUserResponse createUser(CreateUserRequest user);}
