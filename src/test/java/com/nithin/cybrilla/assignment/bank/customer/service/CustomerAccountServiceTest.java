package com.nithin.cybrilla.assignment.bank.customer.service;

import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.common.service.AccountService;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
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
public class CustomerAccountServiceTest {

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @Configuration
    static class Config {
        @Bean
        public AccountService getCustomerBankAccountService() {
            return new CustomerBankAccountServiceImpl();
        }

        @Bean
        public Mapper getDozerBeanMapper() {
            return new DozerBeanMapper();
        }
    }

    @Autowired
    private AccountService customerBankAccountService;

    @Test
    public void testCustomerUserUpdate() {
        Mockito
                .when(bankAccountRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(BankAccountEntity
                        .builder()
                        .accountNumber(12l)
                        .isActive(true)
                        .build()));
        Mockito
                .when(bankAccountRepository.saveAndFlush(Mockito.any()))
                .thenReturn(BankAccountEntity
                        .builder()
                        .accountNumber(12l)
                        .isActive(true)
                        .build());
        BankAccountUpdateRequest user = new BankAccountUpdateRequest();
        HashMap<String, String> value = new HashMap<>();
        value.put("user_id", "11");
        HeaderData
                .getUserHeaderData()
                .set(value);
        BankAccountResponse bankAccountResponse = customerBankAccountService.updateAccount(user, 11l);
        Assert.assertEquals(Long.valueOf(12), bankAccountResponse.getAccountNumber());
    }
}
