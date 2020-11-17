package com.nithin.cybrilla.assignment.bank.customer.service;

import com.nithin.cybrilla.assignment.bank.common.dto.transaction.CustomerTransactionRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.transaction.Transaction;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.SameBankPayeeEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.TransactionEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.TransactionMapperEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import com.nithin.cybrilla.assignment.bank.repository.SameBankPayeeRepository;
import com.nithin.cybrilla.assignment.bank.repository.TransactionMapperRepository;
import com.nithin.cybrilla.assignment.bank.repository.TransactionRepository;
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

import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class CustomerTransactionServiceTest {

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SameBankPayeeRepository sameBankPayeeRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private TransactionMapperRepository transactionMapperRepository;

    @Configuration
    static class Config {
        @Bean
        public CustomerTransactionService customerTransactionService() {
            return new CustomerTransactionServiceImpl();
        }

        @Bean
        public Mapper getDozerBeanMapper() {
            return new DozerBeanMapper();
        }
    }

    @Autowired
    private CustomerTransactionService transactionService;

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
        Mockito
                .when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(UserEntity
                        .builder()
                        .userId(11l)
                        .password(Base64
                                .getEncoder()
                                .encodeToString("qwerty".getBytes()))
                        .isActive(true)
                        .build()));
        Mockito
                .when(sameBankPayeeRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(SameBankPayeeEntity
                        .builder()
                        .bankAccountEntity(accountEntity)
                        .payeeId(11l)
                        .build()));
        TransactionEntity transactionEntity = TransactionEntity
                .builder()
                .transactionId(11l)
                .bankAccountEntity(accountEntity)
                .build();
        Mockito
                .when(transactionRepository.saveAndFlush(Mockito.any()))
                .thenReturn(transactionEntity);
        Mockito
                .when(transactionMapperRepository.saveAndFlush(Mockito.any()))
                .thenReturn(TransactionMapperEntity
                        .builder()
                        .transactionFrom(transactionEntity)
                        .transactionMapperId(11l)
                        .build());

        CustomerTransactionRequest customerTransactionRequest = new CustomerTransactionRequest();
        customerTransactionRequest.setPayeeId(11l);
        customerTransactionRequest.setPassword("qwerty");
        customerTransactionRequest.setAmount(new BigDecimal(120));
        HashMap<String, String> value = new HashMap<>();
        value.put("user_id", "11");
        HeaderData
                .getUserHeaderData()
                .set(value);
        Transaction bankAccountResponse = transactionService.createTransaction(customerTransactionRequest);
        Assert.assertEquals(Long.valueOf(11), bankAccountResponse.getTransactionId());
    }
}
