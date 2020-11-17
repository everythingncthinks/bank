package com.nithin.cybrilla.assignment.bank.common.service;

import com.nithin.cybrilla.assignment.bank.common.dto.user.ManageUserResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.user.UserUpdateRequest;

/**
 * Generic user service.
 */
public interface UserService {

    /**
     * Method to update the user info.
     */
    ManageUserResponse updateUser(UserUpdateRequest user, Long userId);
}
