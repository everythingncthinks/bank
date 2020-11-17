package com.nithin.cybrilla.assignment.bank.customer.controller;

import com.nithin.cybrilla.assignment.bank.common.dto.user.ManageUserResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.user.UserUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.common.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APIs for users to manage editable data.
 */
@RestController
@RequestMapping("/customer/user")
public class CustomerUserController {

    @Autowired
    @Qualifier("customerUserServiceImpl")
    private UserService userService;

    /**
     * APi to update user data
     *
     * @param user input user data
     * @param modifiedBy action done by
     * @param userId userId of the user whose detail need to be modified.
     * @return Update status.
     */
    @PutMapping("/{userId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "userId", type = "long", paramType = "path", dataType = "int64")
    )
    public ResponseEntity<ManageUserResponse> updateUser(@RequestBody UserUpdateRequest user, @RequestHeader("user_id")
            String modifiedBy, @PathVariable("userId") Long userId){

        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(userService.updateUser(user, userId), HttpStatus.OK);
    }
}
