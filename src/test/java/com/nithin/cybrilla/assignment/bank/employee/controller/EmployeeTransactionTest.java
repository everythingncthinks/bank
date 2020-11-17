package com.nithin.cybrilla.assignment.bank.employee.controller;

import com.nithin.cybrilla.assignment.bank.BankTestBase;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayee;
import com.nithin.cybrilla.assignment.bank.common.dto.transaction.EmployeeTransactionRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.transaction.Transaction;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.SameBankPayeeEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.enums.TransactionType;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
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

@SpringBootTest
public class EmployeeTransactionTest extends BankTestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

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

        EmployeeTransactionRequest employeeTransactionRequest = new EmployeeTransactionRequest();
        employeeTransactionRequest.setAccountNumber(accountEntity.getAccountNumber());
        employeeTransactionRequest.setAmount(new BigDecimal(200));
        String message = "TestTransaction";
        employeeTransactionRequest.setMessage(message);
        employeeTransactionRequest.setTransactionType(TransactionType.CREDIT);
        String input = super.objectMapper.writeValueAsString(employeeTransactionRequest);

        MvcResult result = super.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/employee/transaction/")
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
        Assert.assertEquals(TransactionType.CREDIT, transaction.getTransactionType());
    }

}