package com.nithin.cybrilla.assignment.bank.employee.service;

import com.nithin.cybrilla.assignment.bank.common.dto.transaction.EmployeeTransactionRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.transaction.Transaction;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.TransactionEntity;
import com.nithin.cybrilla.assignment.bank.common.enums.TransactionType;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import com.nithin.cybrilla.assignment.bank.repository.TransactionRepository;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class EmployeeTransactionServiceTest {

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @Configuration
    static class Config {
        @Bean
        public EmployeeTransactionService customerTransactionService() {
            return new EmployeeTransactionServiceImpl();
        }

        @Bean
        public Mapper getDozerBeanMapper() {
            return new DozerBeanMapper();
        }
    }

    @Autowired
    private EmployeeTransactionService transactionService;

    @Test
    public void testCustomerUserUpdate() {
        BankAccountEntity accountEntity = BankAccountEntity
                .builder()
                .accountNumber(12l)
                .firstName("test")
                .bankCode("TEST001")
                .balance(new BigDecimal(10000))
                .isActive(true)
                .build();
        Mockito
                .when(bankAccountRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(accountEntity));
        TransactionEntity transactionEntity = TransactionEntity
                .builder()
                .transactionId(11l)
                .bankAccountEntity(accountEntity)
                .build();
        Mockito
                .when(transactionRepository.saveAndFlush(Mockito.any()))
                .thenReturn(transactionEntity);

        EmployeeTransactionRequest employeeTransactionRequest = new EmployeeTransactionRequest();
        employeeTransactionRequest.setAmount(new BigDecimal(120));
        employeeTransactionRequest.setMessage("myMessage");
        employeeTransactionRequest.setAccountNumber(11l);
        employeeTransactionRequest.setTransactionType(TransactionType.CREDIT);
        HashMap<String, String> value = new HashMap<>();
        value.put("user_id", "11");
        HeaderData
                .getUserHeaderData()
                .set(value);
        Transaction bankAccountResponse = transactionService.createTransaction(employeeTransactionRequest);
        Assert.assertEquals(Long.valueOf(11), bankAccountResponse.getTransactionId());
    }
}
