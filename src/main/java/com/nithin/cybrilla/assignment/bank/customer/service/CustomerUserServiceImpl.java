package com.nithin.cybrilla.assignment.bank.customer.service;

import com.nithin.cybrilla.assignment.bank.common.dto.user.ManageUserResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.user.UserUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.exception.UnAuthorizedAccessException;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.common.service.UserService;
import com.nithin.cybrilla.assignment.bank.repository.UserRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;

@Service("customerUserServiceImpl")
public class CustomerUserServiceImpl implements UserService {

    @Autowired
    Mapper mapper;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public ManageUserResponse updateUser(UserUpdateRequest user, Long userId) {
        UserEntity userEntity = userRepository
                .findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        if (!userEntity.isActive()) {
            throw new UnAuthorizedAccessException("Your account is disabled by bank.");
        }
        setUpdateData(user, userEntity);

        UserEntity userEntityInserted = userRepository.saveAndFlush(userEntity);
        return mapper.map(userEntityInserted, ManageUserResponse.class);
    }

    private void setUpdateData(UserUpdateRequest user, UserEntity userEntity) {
        userEntity.setUpdatedBy(HeaderData
                .getUserHeaderData()
                .get()
                .get("user_id"));
        userEntity.setUpdatedDateTime(OffsetDateTime.now());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
    }

}
