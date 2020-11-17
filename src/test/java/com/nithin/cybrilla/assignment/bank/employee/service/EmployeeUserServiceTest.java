package com.nithin.cybrilla.assignment.bank.employee.service;

import com.nithin.cybrilla.assignment.bank.common.dto.user.CreateUserRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.user.EmployeeUserUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.user.ManageUserResponse;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.common.service.UserService;
import com.nithin.cybrilla.assignment.bank.repository.UserRepository;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class EmployeeUserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Configuration
    static class Config {
        @Bean
        public UserService getCustomerBankAccountService() {
            return new EmployeeUserServiceImpl();
        }

        @Bean
        public Mapper getDozerBeanMapper() {
            return new DozerBeanMapper();
        }
    }

    @Autowired
    private EmployeeUserService userService;

    @Test
    public void testCustomerUserCreate() {
        Mockito
                .when(userRepository.saveAndFlush(Mockito.any()))
                .thenReturn(UserEntity
                        .builder()
                        .userId(12l)
                        .isActive(true)
                        .build());
        CreateUserRequest user = new CreateUserRequest();
        HashMap<String, String> value = new HashMap<>();
        value.put("user_id", "11");
        HeaderData
                .getUserHeaderData()
                .set(value);
        ManageUserResponse manageUserResponse = userService.createUser(user);
        Assert.assertEquals(Long.valueOf(12), manageUserResponse.getUserId());
    }

    @Test
    public void testCustomerUserUpdate() {
        Mockito
                .when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(UserEntity
                        .builder()
                        .userId(12l)
                        .isActive(true)
                        .build()));
        Mockito
                .when(userRepository.saveAndFlush(Mockito.any()))
                .thenReturn(UserEntity
                        .builder()
                        .userId(12l)
                        .isActive(true)
                        .build());
        EmployeeUserUpdateRequest user = new EmployeeUserUpdateRequest();
        HashMap<String, String> value = new HashMap<>();
        value.put("user_id", "11");
        HeaderData
                .getUserHeaderData()
                .set(value);
        ManageUserResponse manageUserResponse = userService.updateUser(user, 12l);
        Assert.assertEquals(Long.valueOf(12), manageUserResponse.getUserId());
    }
}
