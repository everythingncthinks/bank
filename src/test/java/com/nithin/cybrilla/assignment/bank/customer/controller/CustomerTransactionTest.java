package com.nithin.cybrilla.assignment.bank.customer.controller;

import com.nithin.cybrilla.assignment.bank.BankTestBase;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayee;
import com.nithin.cybrilla.assignment.bank.common.dto.transaction.Transaction;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.SameBankPayeeEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.TransactionEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.enums.TransactionType;
import com.nithin.cybrilla.assignment.bank.dto.TestCustomerTransactionRequest;
import com.nithin.cybrilla.assignment.bank.dto.TestSameBankPayeeVerifyRequest;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import com.nithin.cybrilla.assignment.bank.repository.SameBankPayeeRepository;
import com.nithin.cybrilla.assignment.bank.repository.TransactionRepository;
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
import java.util.List;

@SpringBootTest
public class CustomerTransactionTest extends BankTestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private SameBankPayeeRepository sameBankPayeeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private UserEntity userEntity;

    private BankAccountEntity bankAccountEntity;

    private SameBankPayee sameBankPayee;

    private SameBankPayeeEntity payeeEntity;

    @Test
    public void testCreateTransaction() throws Exception {
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
        bankAccountEntity = bankAccountRepository.saveAndFlush(accountEntity);
        this.userEntity = userEntity;
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

        TestCustomerTransactionRequest customerTransactionRequest = new TestCustomerTransactionRequest();
        customerTransactionRequest.setAccountNumber(accountEntity.getAccountNumber());
        customerTransactionRequest.setAmount(new BigDecimal(200));
        String message = "TestTransaction";
        customerTransactionRequest.setMessage(message);
        customerTransactionRequest.setPassword("pass");
        customerTransactionRequest.setPayeeId(payeeEntity.getPayeeId());
        String input = super.objectMapper.writeValueAsString(customerTransactionRequest);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/customer/transaction/")
                        .header("user_id", userEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();
        Transaction transaction = super.objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), Transaction.class);
        Assert.assertEquals(TransactionType.DEBIT, transaction.getTransactionType());
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

        TransactionEntity transactionEntity = transactionRepository.saveAndFlush(TransactionEntity
                .builder()
                .transactionType(TransactionType.DEBIT)
                .transactionStatus("SUCCESS")
                .bankAccountEntity(accountEntity)
                .message("TestMessage")
                .createdBy("1")
                .updatedBy("1")
                .createdDateTime(OffsetDateTime.now())
                .updatedDateTime(OffsetDateTime.now())
                .build());

        TestSameBankPayeeVerifyRequest bankPayeeVerifyRequest = new TestSameBankPayeeVerifyRequest();
        bankPayeeVerifyRequest.setPassword("pass");
        String input = super.objectMapper.writeValueAsString(bankPayeeVerifyRequest);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/customer/transaction/statement/" + accountEntity.getAccountNumber())
                        .header("user_id", userEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(200))
                .andReturn();
        List<Transaction> transactions = super.objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, Transaction.class));
        Assert.assertEquals(1, transactions.size());
    }
}