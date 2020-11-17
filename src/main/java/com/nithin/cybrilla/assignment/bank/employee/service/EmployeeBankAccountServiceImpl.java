package com.nithin.cybrilla.assignment.bank.employee.service;

import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccount;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import com.nithin.cybrilla.assignment.bank.repository.UserRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * account service specific for employees.
 */
@Service("employeeBankAccountServiceImpl")
public class EmployeeBankAccountServiceImpl implements EmployeeBankAccountService {

    @Autowired
    Mapper mapper;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * create a bank account with the given information.
     */
    @Override
    @Transactional
    public BankAccountResponse createAccount(BankAccount bankAccount) {
        Optional<UserEntity> userRepositoryById = userRepository.findById(bankAccount.getUserId());
        BankAccountEntity userEntity =
                createUserEntity(bankAccount, userRepositoryById.orElseThrow(IllegalArgumentException::new));
        BankAccountEntity userEntityInserted = bankAccountRepository.saveAndFlush(userEntity);
        return mapper.map(userEntityInserted, BankAccountResponse.class);
    }

    /**
     * Handle to update the account info.
     */
    @Override
    @Transactional
    public BankAccountResponse updateAccount(BankAccountUpdateRequest accountUpdateRequest, Long userId) {
        BankAccountEntity bankAccountEntity = bankAccountRepository
                .findById(userId)
                .orElseThrow(IllegalArgumentException::new);

        BankAccount bankAccount = (BankAccount)accountUpdateRequest;
        UserEntity userEntity = getUserEntity((BankAccount)accountUpdateRequest, bankAccountEntity, bankAccount);
        setUpdateData(bankAccount, bankAccountEntity, userEntity);

        BankAccountEntity userEntityInserted = bankAccountRepository.saveAndFlush(bankAccountEntity);
        return mapper.map(userEntityInserted, BankAccountResponse.class);
    }

    /**
     * way to get the account info.
     */
    @Override
    public BankAccountResponse getAccount(Long accountNumber) {
        BankAccountEntity accountEntity = bankAccountRepository
                .findById(accountNumber)
                .orElseThrow(IllegalArgumentException::new);
        return mapper.map(accountEntity, BankAccountResponse.class);
    }

    /**
     * get the user entity if it matches with input data before creating the account.
     */
    private UserEntity getUserEntity(BankAccount accountUpdateRequest, BankAccountEntity bankAccountEntity,
            BankAccount bankAccount) {
        UserEntity userEntity = bankAccountEntity.getUserEntity();
        if (!bankAccountEntity
                .getUserEntity()
                .getUserId()
                .equals(accountUpdateRequest.getUserId())) {
            Optional<UserEntity> userRepositoryById = userRepository.findById(bankAccount.getUserId());
            userEntity = userRepositoryById.orElseThrow(IllegalArgumentException::new);
        }
        return userEntity;
    }

    /**
     * set the data to be updated.
     */
    private void setUpdateData(BankAccount bankAccount, BankAccountEntity bankAccountEntity, UserEntity userEntity) {
        bankAccountEntity.setUpdatedBy(HeaderData
                .getUserHeaderData()
                .get()
                .get("user_id"));
        bankAccountEntity.setAccountStatus(bankAccount.getAccountStatus());
        bankAccountEntity.setAccountType(bankAccount.getAccountType());
        bankAccountEntity.setBankCode(bankAccount.getBankCode());
        bankAccountEntity.setActive(bankAccount.isActive());
        bankAccountEntity.setFirstName(bankAccount.getFirstName());
        bankAccountEntity.setLastName(bankAccount.getLastName());
        bankAccountEntity.setUserEntity(userEntity);
        bankAccountEntity.setUpdatedDateTime(OffsetDateTime.now());
        bankAccountEntity.setActive(bankAccount.isActive());
    }

    /**
     * create the user entity to be persisted.
     */
    private BankAccountEntity createUserEntity(BankAccount bankAccount, UserEntity userEntity) {
        String userId = HeaderData
                .getUserHeaderData()
                .get()
                .get("user_id");
        OffsetDateTime dateTime = OffsetDateTime.now();

        return BankAccountEntity
                .builder()
                .accountStatus(bankAccount.getAccountStatus())
                .accountType(bankAccount.getAccountType())
                .balance(new BigDecimal("0"))
                .bankCode(bankAccount.getBankCode())
                .isActive(bankAccount.isActive())
                .firstName(bankAccount.getFirstName())
                .lastName(bankAccount.getLastName())
                .userEntity(userEntity)
                .createdBy(userId)
                .createdDateTime(dateTime)
                .updatedBy(userId)
                .updatedDateTime(dateTime)
                .build();
    }

}
