package com.nithin.cybrilla.assignment.bank.customer.service;

import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.common.service.AccountService;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;

/**
 * Bank service specific to customer.
 */
@Service("customerBankAccountServiceImpl")
public class CustomerBankAccountServiceImpl implements AccountService {

    @Autowired
    Mapper mapper;

    @Autowired
    BankAccountRepository bankAccountRepository;

    /**
     * Method to update account info.
     */
    @Override
    @Transactional
    public BankAccountResponse updateAccount(BankAccountUpdateRequest accountUpdateRequest, Long userId) {
        BankAccountEntity userEntity = bankAccountRepository
                .findById(userId)
                .orElseThrow(IllegalArgumentException::new);
        setUpdateData(accountUpdateRequest, userEntity);

        BankAccountEntity userEntityInserted = bankAccountRepository.saveAndFlush(userEntity);
        return mapper.map(userEntityInserted, BankAccountResponse.class);
    }

    /**
     * method to get the account.
     */
    @Override
    public BankAccountResponse getAccount(Long accountNumber) {
        BankAccountEntity accountEntity = bankAccountRepository
                .findById(accountNumber)
                .orElseThrow(IllegalArgumentException::new);
        return mapper.map(accountEntity, BankAccountResponse.class);
    }

    /**
     * A helper to set the data to be persisted into the entity.
     */
    private void setUpdateData(BankAccountUpdateRequest bankAccount, BankAccountEntity bankAccountEntity) {
        bankAccountEntity.setUpdatedBy(HeaderData
                .getUserHeaderData()
                .get()
                .get("user_id"));
        bankAccountEntity.setUpdatedDateTime(OffsetDateTime.now());
        bankAccountEntity.setLastName(bankAccount.getLastName());
        bankAccountEntity.setFirstName(bankAccount.getFirstName());
    }

}
