package com.nithin.cybrilla.assignment.bank.employee.service;

import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccount;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountResponse;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
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
public class EmployeeAccountServiceTest {

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @MockBean
    private UserRepository userRepository;

    @Configuration
    static class Config {
        @Bean
        public EmployeeBankAccountService getCustomerBankAccountService() {
            return new EmployeeBankAccountServiceImpl();
        }

        @Bean
        public Mapper getDozerBeanMapper() {
            return new DozerBeanMapper();
        }
    }

    @Autowired
    private EmployeeBankAccountService employeeBankAccountService;

    @Test
    public void testEmployeeAccountCreate() {

        Mockito
                .when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(UserEntity
                        .builder()
                        .userId(12l)
                        .isActive(true)
                        .build()));
        Mockito
                .when(bankAccountRepository.saveAndFlush(Mockito.any()))
                .thenReturn(BankAccountEntity
                        .builder()
                        .accountNumber(12l)
                        .isActive(true)
                        .build());
        BankAccount user = new BankAccount();
        HashMap<String, String> value = new HashMap<>();
        value.put("user_id", "11");
        HeaderData
                .getUserHeaderData()
                .set(value);
        BankAccountResponse bankAccountResponse = employeeBankAccountService.createAccount(user);
        Assert.assertEquals(Long.valueOf(12), bankAccountResponse.getAccountNumber());
    }

    @Test
    public void testCustomerUserUpdate() {
        UserEntity userEntity = UserEntity
                .builder()
                .userId(12l)
                .isActive(true)
                .build();
        Mockito
                .when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(userEntity));
        BankAccountEntity accountEntity = BankAccountEntity
                .builder()
                .userEntity(userEntity)
                .accountNumber(12l)
                .isActive(true)
                .build();
        Mockito
                .when(bankAccountRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(accountEntity));
        Mockito
                .when(bankAccountRepository.saveAndFlush(Mockito.any()))
                .thenReturn(BankAccountEntity
                        .builder()
                        .accountNumber(12l)
                        .isActive(true)
                        .build());
        BankAccount user = new BankAccount();
        HashMap<String, String> value = new HashMap<>();
        value.put("user_id", "11");
        HeaderData
                .getUserHeaderData()
                .set(value);
        BankAccountResponse bankAccountResponse = employeeBankAccountService.updateAccount(user, 11l);
        Assert.assertEquals(Long.valueOf(12), bankAccountResponse.getAccountNumber());
    }
}
