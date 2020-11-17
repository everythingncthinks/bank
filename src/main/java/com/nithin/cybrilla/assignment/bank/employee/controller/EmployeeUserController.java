package com.nithin.cybrilla.assignment.bank.employee.controller;

import com.nithin.cybrilla.assignment.bank.common.dto.user.CreateUserRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.user.EmployeeUserUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.user.ManageUserResponse;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.employee.service.EmployeeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APIs for employees.
 */
@RestController
@RequestMapping("/employee/user")
public class EmployeeUserController {

    @Autowired
    @Qualifier("employeeUserServiceImpl")
    private EmployeeUserService userService;

    /**
     * API to create user.
     *
     * @param user user data.
     * @param modifiedBy Action done by.
     * @return user id and information.
     */
    @PostMapping
    public ResponseEntity<ManageUserResponse> createUser(@RequestBody CreateUserRequest user,
            @RequestHeader("user_id") String modifiedBy) {
        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
    }

    /**
     * APi to update user data
     *
     * @param user input user data
     * @param modifiedBy action done by
     * @param userId userId of the user whose detail need to be modified.
     * @return Update status.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ManageUserResponse> updateUser(@RequestBody EmployeeUserUpdateRequest user,
            @RequestHeader("user_id") String modifiedBy, @PathVariable("userId") Long userId) {

        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(userService.updateUser(user, userId), HttpStatus.OK);
    }
}
