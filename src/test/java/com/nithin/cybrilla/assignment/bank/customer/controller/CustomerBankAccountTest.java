package com.nithin.cybrilla.assignment.bank.customer.controller;

import com.nithin.cybrilla.assignment.bank.BankTestBase;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Base64;

@SpringBootTest
public class CustomerBankAccountTest extends BankTestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private BankAccountEntity bankAccountEntity;

    private UserEntity userEntity;

    @After
    public void clean() {
        bankAccountRepository.delete(bankAccountEntity);
        userRepository.delete(userEntity);
    }

    @Test
    public void testUpdateBankAccount() throws Exception {
        UserEntity userEntity = UserEntity
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
                .build();
        bankAccountEntity = bankAccountRepository.saveAndFlush(BankAccountEntity
                .builder()
                .userEntity(userEntity)
                .lastName("test")
                .firstName("new")
                .bankCode("TEST001")
                .accountType("SAVINGS")
                .accountStatus("ACTIVE")
                .balance(new BigDecimal(1000))
                .createdDateTime(OffsetDateTime.now())
                .updatedDateTime(OffsetDateTime.now())
                .isActive(true)
                .updatedBy("1")
                .createdBy("1")
                .build());
        this.userEntity = userEntity;

        BankAccountUpdateRequest bankAccountUpdateRequest = new BankAccountUpdateRequest();
        bankAccountUpdateRequest.setFirstName("first");
        bankAccountUpdateRequest.setLastName("last");
        String input = super.objectMapper.writeValueAsString(bankAccountUpdateRequest);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("http://localhost:8080/customer/account/" + bankAccountEntity.getAccountNumber())
                        .header("user_id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();
        Assert.assertTrue(result
                .getResponse()
                .getContentAsString()
                .contains("first"));
    }
}
