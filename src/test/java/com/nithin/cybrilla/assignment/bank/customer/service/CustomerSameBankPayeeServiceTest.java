package com.nithin.cybrilla.assignment.bank.customer.service;

import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayee;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayeeCreateRequest;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.SameBankPayeeEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import com.nithin.cybrilla.assignment.bank.repository.SameBankPayeeRepository;
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
public class CustomerSameBankPayeeServiceTest {

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SameBankPayeeRepository sameBankPayeeRepository;

    @Configuration
    static class Config {
        @Bean
        public CustomerPayeeService getCustomerBankAccountService() {
            return new CustomerPayeeServiceImpl();
        }

        @Bean
        public Mapper getDozerBeanMapper() {
            return new DozerBeanMapper();
        }
    }

    @Autowired
    private CustomerPayeeService payeeService;

    @Test
    public void testCustomerUserUpdate() {
        Mockito
                .when(bankAccountRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(BankAccountEntity
                        .builder()
                        .accountNumber(12l)
                        .firstName("test")
                        .bankCode("TEST001")
                        .isActive(true)
                        .build()));
        Mockito
                .when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(UserEntity
                        .builder()
                        .userId(11l)
                        .isActive(true)
                        .build()));
        Mockito
                .when(sameBankPayeeRepository.saveAndFlush(Mockito.any()))
                .thenReturn(SameBankPayeeEntity
                        .builder()
                        .payeeId(11l)
                        .build());

        SameBankPayeeCreateRequest user = new SameBankPayeeCreateRequest();
        user.setDisplayName("testing");
        user.setAccountHolderName("test");
        user.setBankCode("TEST001");
        HashMap<String, String> value = new HashMap<>();
        value.put("user_id", "11");
        HeaderData
                .getUserHeaderData()
                .set(value);
        SameBankPayee bankAccountResponse = payeeService.createSameBankPayee(user);
        Assert.assertEquals(Long.valueOf(11), bankAccountResponse.getPayeeId());
    }
}
