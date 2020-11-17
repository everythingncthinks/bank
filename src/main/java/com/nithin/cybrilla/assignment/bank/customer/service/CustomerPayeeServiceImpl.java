package com.nithin.cybrilla.assignment.bank.customer.service;

import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayee;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayeeCreateRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayeeVerifyRequest;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.SameBankPayeeEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.exception.PayeeDetailsNotMatchingException;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import com.nithin.cybrilla.assignment.bank.repository.SameBankPayeeRepository;
import com.nithin.cybrilla.assignment.bank.repository.UserRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.OffsetDateTime;

/**
 * Payee service for customer.
 */
@Service
public class CustomerPayeeServiceImpl implements CustomerPayeeService {

    @Autowired
    Mapper mapper;

    @Autowired
    private SameBankPayeeRepository sameBankPayeeRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * creates a payee.
     */
    @Override
    public SameBankPayee createSameBankPayee(SameBankPayeeCreateRequest sameBankPayee) {

        BankAccountEntity accountEntity = bankAccountRepository
                .findById(sameBankPayee.getAccountNumber())
                .orElseThrow(IllegalArgumentException::new);

        UserEntity userEntity = userRepository
                .findById(Long.valueOf(HeaderData
                        .getUserHeaderData()
                        .get()
                        .get("user_id")))
                .orElseThrow(IllegalArgumentException::new);

        if (!(sameBankPayee
                .getAccountHolderName()
                .equals(accountEntity.getFirstName()) && sameBankPayee
                .getBankCode()
                .equals(accountEntity.getBankCode()))) {
            throw new PayeeDetailsNotMatchingException();
        }
        SameBankPayeeEntity payeeEntity = createPayeeEntity(sameBankPayee, accountEntity, userEntity);
        SameBankPayeeEntity sameBankPayeeEntity = sameBankPayeeRepository.saveAndFlush(payeeEntity);

        return getSameBankPayee(sameBankPayee, sameBankPayeeEntity);
    }

    /**
     * creates the response object to be returned to front end.
     */
    private SameBankPayee getSameBankPayee(SameBankPayeeCreateRequest sameBankPayee,
            SameBankPayeeEntity sameBankPayeeEntity) {
        SameBankPayee bankPayee = mapper.map(sameBankPayeeEntity, SameBankPayee.class);
        bankPayee.setAccountNumber(sameBankPayee.getAccountNumber());
        bankPayee.setAccountHolderName(sameBankPayee.getAccountHolderName());
        bankPayee.setBankCode(sameBankPayee.getBankCode());
        return bankPayee;
    }

    /**
     * method to verify basket payee.
     */
    @Override
    public SameBankPayee verifySameBankPayee(SameBankPayeeVerifyRequest sameBankPayee, Long payeeId) {

        verifyUser(sameBankPayee);
        SameBankPayeeEntity payeeEntity = sameBankPayeeRepository
                .findById(payeeId)
                .orElseThrow(IllegalArgumentException::new);
        payeeEntity.setVerified(true);
        SameBankPayeeEntity sameBankPayeeEntity = sameBankPayeeRepository.saveAndFlush(payeeEntity);
        SameBankPayee bankPayee = mapper.map(sameBankPayeeEntity, SameBankPayee.class);
        bankPayee.setAccountNumber(sameBankPayeeEntity
                .getBankAccountEntity()
                .getAccountNumber());
        bankPayee.setAccountHolderName(sameBankPayeeEntity
                .getBankAccountEntity()
                .getFirstName());
        bankPayee.setBankCode(sameBankPayeeEntity
                .getBankAccountEntity()
                .getBankCode());
        return bankPayee;
    }

    public void deletePayee(Long payeeId) {
        sameBankPayeeRepository.deleteById(payeeId);
    }

    /**
     * Method to verify user before payee is created.
     */
    private void verifyUser(SameBankPayeeVerifyRequest sameBankPayee) {
        UserEntity userEntity = userRepository
                .findById(Long.parseLong(HeaderData
                        .getUserHeaderData()
                        .get()
                        .get("user_id")))
                .orElseThrow(IllegalArgumentException::new);
        if (!sameBankPayee
                .getPassword()
                .equals(userEntity.getPassword())) {
            throw new ValidationException("Password does not match");
        }
    }

    /**
     * create payee entity to be persisted.
     */
    private SameBankPayeeEntity createPayeeEntity(SameBankPayeeCreateRequest sameBankPayee,
            BankAccountEntity accountEntity, UserEntity userEntity) {

        OffsetDateTime dateTime = OffsetDateTime.now();
        String userId = userEntity
                .getUserId()
                .toString();
        return SameBankPayeeEntity
                .builder()
                .bankAccountEntity(accountEntity)
                .displayName(sameBankPayee.getDisplayName())
                .isVerified(false)
                .userEntity(userEntity)
                .createdBy(userId)
                .createdDateTime(dateTime)
                .updatedBy(userId)
                .updatedDateTime(dateTime)
                .build();
    }
}
