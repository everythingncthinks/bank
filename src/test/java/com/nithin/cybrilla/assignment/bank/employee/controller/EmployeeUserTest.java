package com.nithin.cybrilla.assignment.bank.employee.controller;

import com.nithin.cybrilla.assignment.bank.BankTestBase;
import com.nithin.cybrilla.assignment.bank.common.dto.user.ManageUserResponse;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.dto.TestCreateUserRequest;
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
public class EmployeeUserTest extends BankTestBase {

    @Autowired
    private UserRepository userRepository;

    private UserEntity userEntity;

    @After
    public void clean() {
        userRepository.delete(userEntity);
    }

    @Test
    public void testCreateUser() throws Exception {

        TestCreateUserRequest createUserRequest = new TestCreateUserRequest();
        createUserRequest.setEmail("newTest@new.com");
        createUserRequest.setUserName("tester");
        createUserRequest.setUserType("CUSTOMER");
        String pwd = "newpass";
        String password = Base64
                .getEncoder()
                .encodeToString(pwd.getBytes());
        createUserRequest.setPassword(pwd);
        String input = super.objectMapper.writeValueAsString(createUserRequest);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/employee/user/")
                        .header("user_id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();
        Assert.assertNotNull(result);
        ManageUserResponse userResponse = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), ManageUserResponse.class);
        userEntity = userRepository
                .findById(userResponse.getUserId())
                .get();
        Assert.assertEquals(password, userEntity.getPassword());
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
                        .put("http://localhost:8080/employee/user/" + userEntity.getUserId())
                        .header("user_id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();
        Assert.assertNotNull(result);
        userEntity = userRepository
                .findById(this.userEntity.getUserId())
                .get();
        Assert.assertEquals(password, userEntity.getPassword());
    }
}
