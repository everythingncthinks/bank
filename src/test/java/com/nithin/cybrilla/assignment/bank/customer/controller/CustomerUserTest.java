package com.nithin.cybrilla.assignment.bank.customer.controller;

import com.nithin.cybrilla.assignment.bank.BankTestBase;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.dto.TestUserUpdateRequest;
import com.nithin.cybrilla.assignment.bank.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.OffsetDateTime;
import java.util.Base64;

@SpringBootTest
public class CustomerUserTest extends BankTestBase {

    @Autowired
    private UserRepository userRepository;

    private UserEntity userEntity;

    @After
    public void clean() {
        userRepository.delete(userEntity);
    }

    @Test
    public void testUpdateUser() throws Exception {
        userEntity = userRepository.saveAndFlush(UserEntity
                .builder()
                .userType("CUSTOMER")
                .userName("user")
                .createdDateTime(OffsetDateTime.now())
                .updatedDateTime(OffsetDateTime.now())
                .email("test@test.com")
                .password(Base64
                        .getEncoder()
                        .encodeToString("pass".getBytes()))
                .isActive(true)
                .updatedBy("1")
                .createdBy("1")
                .build());

        TestUserUpdateRequest userUpdateRequest = new TestUserUpdateRequest();
        userUpdateRequest.setEmail("newTest@new.com");
        String pwd = "newpass";
        String password = Base64
                .getEncoder()
                .encodeToString(pwd.getBytes());
        userUpdateRequest.setPassword(pwd);
        String input = super.objectMapper.writeValueAsString(userUpdateRequest);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("http://localhost:8080/customer/user/" + userEntity.getUserId())
                        .header("user_id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();
        Assert.assertNotNull(result);
        Assert.assertEquals(password, userRepository
                .findById(userEntity.getUserId())
                .get()
                .getPassword());
    }
}
