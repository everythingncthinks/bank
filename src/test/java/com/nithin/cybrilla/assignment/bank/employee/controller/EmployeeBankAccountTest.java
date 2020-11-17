package com.nithin.cybrilla.assignment.bank.employee.controller;

import com.nithin.cybrilla.assignment.bank.BankTestBase;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccount;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountResponse;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import com.nithin.cybrilla.assignment.bank.repository.UserRepository;
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
public class EmployeeBankAccountTest extends BankTestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private BankAccountEntity bankAccountEntity;

    private UserEntity userEntity;

    @Test
    public void testCreateBankAccount() throws Exception {
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
        this.userEntity = userRepository.saveAndFlush(userEntity);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setActive(true);
        bankAccount.setAccountStatus("OPERATIVE");
        bankAccount.setAccountType("SAVINGS");
        bankAccount.setBankCode("TEST001");
        bankAccount.setUserId(userEntity.getUserId());
        bankAccount.setBalance(new BigDecimal(0));
        bankAccount.setFirstName("first");
        bankAccount.setLastName("last");
        String input = super.objectMapper.writeValueAsString(bankAccount);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/employee/account/")
                        .header("user_id", userEntity.getUserId())
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
        //        this.userEntity = userRepository.saveAndFlush(userEntity);
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

        BankAccount bankAccount = new BankAccount();
        bankAccount.setFirstName("first");
        bankAccount.setLastName("last");
        bankAccount.setUserId(userEntity.getUserId());
        bankAccount.setBalance(new BigDecimal(0));
        bankAccount.setAccountType("SAVINGS");
        bankAccount.setAccountStatus("OPERATIVE");
        bankAccount.setActive(false);
        String input = super.objectMapper.writeValueAsString(bankAccount);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("http://localhost:8080/employee/account/" + bankAccountEntity.getAccountNumber())
                        .header("user_id", userEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();
        BankAccountResponse bankAccountResponse = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), BankAccountResponse.class);
        Assert.assertFalse(bankAccountResponse.isActive());
        Assert.assertTrue(result
                .getResponse()
                .getContentAsString()
                .contains("first"));
    }
}
