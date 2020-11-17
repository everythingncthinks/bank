package com.nithin.cybrilla.assignment.bank.employee.service;

import com.nithin.cybrilla.assignment.bank.common.dto.user.CreateUserRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.user.EmployeeUserUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.user.ManageUserResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.user.UserUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.repository.UserRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;

/**
 * user service for employees
 */
@Service("employeeUserServiceImpl")
public class EmployeeUserServiceImpl implements EmployeeUserService {

    @Autowired
    Mapper mapper;

    @Autowired
    UserRepository userRepository;

    /**
     * method to create the user info.
     */
    @Override
    @Transactional
    public ManageUserResponse createUser(CreateUserRequest user) {
        UserEntity userEntity = createUserEntity(user);
        UserEntity userEntityInserted = userRepository.saveAndFlush(userEntity);
        return mapper.map(userEntityInserted, ManageUserResponse.class);
    }

    /**
     * method to update the user info.
     */
    @Override
    @Transactional
    public ManageUserResponse updateUser(UserUpdateRequest user, Long userId) {
        UserEntity userEntity = userRepository
                .findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        setUpdateData((EmployeeUserUpdateRequest)user, userEntity);

        UserEntity userEntityInserted = userRepository.saveAndFlush(userEntity);
        return mapper.map(userEntityInserted, ManageUserResponse.class);
    }

    /**
     * method to update the user info.
     */
    private void setUpdateData(EmployeeUserUpdateRequest user, UserEntity userEntity) {
        userEntity.setUpdatedBy(HeaderData
                .getUserHeaderData()
                .get()
                .get("user_id"));
        userEntity.setUpdatedDateTime(OffsetDateTime.now());
        userEntity.setActive(user.isActive());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
    }

    /**
     * method to create the entity to be persisted.
     */
    private UserEntity createUserEntity(CreateUserRequest user) {
        String userId = HeaderData
                .getUserHeaderData()
                .get()
                .get("user_id");
        OffsetDateTime dateTime = OffsetDateTime.now();

        return UserEntity
                .builder()
                .createdBy(userId)
                .createdDateTime(dateTime)
                .updatedBy(userId)
                .email(user.getEmail())
                .isActive(true)
                .password(user.getPassword())
                .updatedDateTime(dateTime)
                .userName(user.getUserName())
                .userType(user.getUserType())
                .build();
    }

}
