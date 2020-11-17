package com.nithin.cybrilla.assignment.bank.customer.controller;

import com.nithin.cybrilla.assignment.bank.BankTestBase;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayee;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayeeCreateRequest;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.SameBankPayeeEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.dto.TestSameBankPayeeVerifyRequest;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import com.nithin.cybrilla.assignment.bank.repository.SameBankPayeeRepository;
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
public class CustomerPayeeTest extends BankTestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private SameBankPayeeRepository sameBankPayeeRepository;

    private UserEntity userEntity;

    private BankAccountEntity bankAccountEntity;

    private SameBankPayee sameBankPayee;
    private SameBankPayeeEntity payeeEntity;

    @After
    public void clean() {

        bankAccountRepository.delete(bankAccountEntity);
        userRepository.delete(userEntity);
    }

    @Test
    public void testAddPayee() throws Exception {
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

        SameBankPayeeCreateRequest sameBankPayeeCreateRequest = new SameBankPayeeCreateRequest();
        sameBankPayeeCreateRequest.setBankCode("TEST001");
        sameBankPayeeCreateRequest.setAccountHolderName("new");
        sameBankPayeeCreateRequest.setAccountNumber(bankAccountEntity.getAccountNumber());
        String displayName = "TestingPayee";
        sameBankPayeeCreateRequest.setDisplayName(displayName);
        String input = super.objectMapper.writeValueAsString(sameBankPayeeCreateRequest);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/customer/payee/")
                        .header("user_id", userEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();
        sameBankPayee = super.objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), SameBankPayee.class);
        Assert.assertTrue(sameBankPayee
                .getDisplayName()
                .contains(displayName));
        sameBankPayeeRepository.deleteById(sameBankPayee.getPayeeId());
    }

    @Test
    public void testValidatePayee() throws Exception {

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
        BankAccountEntity accountEntity = BankAccountEntity
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
                .build();
        bankAccountRepository.saveAndFlush(accountEntity);
        payeeEntity = sameBankPayeeRepository.saveAndFlush(SameBankPayeeEntity
                .builder()
                .bankAccountEntity(accountEntity)
                .userEntity(userEntity)
                .isVerified(false)
                .displayName("TestName")
                .createdBy("1")
                .updatedBy("1")
                .createdDateTime(OffsetDateTime.now())
                .updatedDateTime(OffsetDateTime.now())
                .build());
        this.userEntity = userEntity;
        this.bankAccountEntity = accountEntity;

        TestSameBankPayeeVerifyRequest bankPayeeVerifyRequest = new TestSameBankPayeeVerifyRequest();
        bankPayeeVerifyRequest.setPassword("pass");
        String input = super.objectMapper.writeValueAsString(bankPayeeVerifyRequest);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("http://localhost:8080/customer/payee/" + payeeEntity.getPayeeId())
                        .header("user_id", userEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();
        sameBankPayee = super.objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), SameBankPayee.class);
        Assert.assertTrue(sameBankPayee.isVerified());
        sameBankPayeeRepository.deleteById(sameBankPayee.getPayeeId());
    }

    @Test
    public void testDeletePayee() throws Exception {

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
        BankAccountEntity accountEntity = BankAccountEntity
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
                .build();
        bankAccountRepository.saveAndFlush(accountEntity);
        payeeEntity = sameBankPayeeRepository.saveAndFlush(SameBankPayeeEntity
                .builder()
                .userEntity(userEntity)
                .bankAccountEntity(accountEntity)
                .isVerified(false)
                .displayName("TestName")
                .createdBy("1")
                .updatedBy("1")
                .createdDateTime(OffsetDateTime.now())
                .updatedDateTime(OffsetDateTime.now())
                .build());
        this.userEntity = userEntity;
        this.bankAccountEntity = accountEntity;

        TestSameBankPayeeVerifyRequest bankPayeeVerifyRequest = new TestSameBankPayeeVerifyRequest();
        bankPayeeVerifyRequest.setPassword("pass");
        String input = super.objectMapper.writeValueAsString(bankPayeeVerifyRequest);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("http://localhost:8080/customer/payee/" + payeeEntity.getPayeeId())
                        .header("user_id", userEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();
        Assert.assertTrue(sameBankPayeeRepository
                .findById(payeeEntity.getPayeeId())
                .isEmpty());
    }
}


