package com.nithin.cybrilla.assignment.bank.customer.service;

import com.nithin.cybrilla.assignment.bank.common.dto.user.ManageUserResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.user.UserUpdateRequest;
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
public class CustomerUserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Configuration
    static class Config {
        @Bean
        public UserService getCustomerBankAccountService() {
            return new CustomerUserServiceImpl();
        }

        @Bean
        public Mapper getDozerBeanMapper() {
            return new DozerBeanMapper();
        }
    }

    @Autowired
    private UserService userService;

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
        UserUpdateRequest user = new UserUpdateRequest();
        HashMap<String, String> value = new HashMap<>();
        value.put("user_id", "11");
        HeaderData
                .getUserHeaderData()
                .set(value);
        ManageUserResponse manageUserResponse = userService.updateUser(user, 12l);
        Assert.assertEquals(Long.valueOf(12), manageUserResponse.getUserId());
    }
}
