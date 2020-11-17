package com.nithin.cybrilla.assignment.bank.customer.service;

import com.nithin.cybrilla.assignment.bank.common.dto.transaction.CustomerTransactionRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.transaction.Transaction;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.SameBankPayeeEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.TransactionEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.TransactionMapperEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import com.nithin.cybrilla.assignment.bank.common.enums.TransactionType;
import com.nithin.cybrilla.assignment.bank.common.exception.InSufficientBalanceException;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import com.nithin.cybrilla.assignment.bank.repository.SameBankPayeeRepository;
import com.nithin.cybrilla.assignment.bank.repository.TransactionMapperRepository;
import com.nithin.cybrilla.assignment.bank.repository.TransactionRepository;
import com.nithin.cybrilla.assignment.bank.repository.UserRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Customer specific implementation for transaction.
 */
@Service("customerTransactionServiceImpl")
public class CustomerTransactionServiceImpl implements CustomerTransactionService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private SameBankPayeeRepository sameBankPayeeRepository;

    @Autowired
    private TransactionMapperRepository transactionMapperRepository;

    /**
     * Method to create transaction.
     */
    @Override
    public Transaction createTransaction(CustomerTransactionRequest customerTransactionRequest) {
        verifyUser(customerTransactionRequest.getPassword());
        BankAccountEntity sourceAccount = bankAccountRepository
                .findById(customerTransactionRequest.getAccountNumber())
                .orElseThrow(IllegalArgumentException::new);
        validateBalance(customerTransactionRequest, sourceAccount);
        SameBankPayeeEntity payeeEntity = sameBankPayeeRepository
                .findById(customerTransactionRequest.getPayeeId())
                .orElseThrow(IllegalArgumentException::new);
        BankAccountEntity destinationAccount = payeeEntity.getBankAccountEntity();
        sourceAccount.setBalance(sourceAccount
                .getBalance()
                .subtract(customerTransactionRequest.getAmount()));
        destinationAccount.setBalance(destinationAccount
                .getBalance()
                .add(customerTransactionRequest.getAmount()));

        String userId = HeaderData
                .getUserHeaderData()
                .get()
                .get("user_id");
        OffsetDateTime dateTime = OffsetDateTime.now();
        TransactionEntity sourceTransaction = createTransactionEntity(customerTransactionRequest, sourceAccount,
                destinationAccount.getAccountNumber(), userId, dateTime, TransactionType.DEBIT);
        TransactionEntity destinationTransaction =
                createTransactionEntity(customerTransactionRequest, destinationAccount,
                        sourceAccount.getAccountNumber(), userId, dateTime, TransactionType.CREDIT);
        sourceTransaction = transactionRepository.saveAndFlush(sourceTransaction);
        destinationTransaction = transactionRepository.saveAndFlush(destinationTransaction);
        TransactionMapperEntity transactionMapperEntity =
                createTransactionMapper(userId, dateTime, sourceTransaction, destinationTransaction);
        transactionMapperRepository.saveAndFlush(transactionMapperEntity);

        return getTransaction(sourceAccount, dateTime, sourceTransaction);
    }

    /**
     * Creates a transaction object to be returned to front end.
     */
    private Transaction getTransaction(BankAccountEntity sourceAccount, OffsetDateTime dateTime,
            TransactionEntity sourceTransaction) {
        Transaction transaction = mapper.map(sourceAccount, Transaction.class);
        transaction.setTransactionDateTime(dateTime);
        transaction.setTransactionType(sourceTransaction.getTransactionType());
        transaction.setTransactionStatus(sourceTransaction.getTransactionStatus());
        transaction.setTransactionId(sourceTransaction.getTransactionId());
        transaction.setMessage(sourceTransaction.getMessage());
        transaction.setAccountNumber(sourceTransaction
                .getBankAccountEntity()
                .getAccountNumber());
        return transaction;
    }

    /**
     * transaction viewer.
     */
    @Override
    public List<Transaction> viewTransactions(Long userId) {
        List<TransactionEntity> transactions = transactionRepository.findByBankAccountEntity(bankAccountRepository
                .findById(userId)
                .orElseThrow(IllegalArgumentException::new));
        return transactions
                .stream()
                .map(transactionEntity -> getTransaction(transactionEntity.getBankAccountEntity(),
                        transactionEntity.getCreatedDateTime(), transactionEntity))
                .collect(Collectors.toList());
    }

    /**
     * creates a transaction detail mapping debited account and credited account.
     */
    private TransactionMapperEntity createTransactionMapper(String userId, OffsetDateTime dateTime,
            TransactionEntity sourceTransaction, TransactionEntity destinationTransaction) {
        return TransactionMapperEntity
                .builder()
                .transactionFrom(sourceTransaction)
                .transactionTo(destinationTransaction)
                .createdBy(userId)
                .createdDateTime(dateTime)
                .updatedDateTime(dateTime)
                .build();
    }

    /**
     * creates a transaction entity to be persisted.
     */
    private TransactionEntity createTransactionEntity(CustomerTransactionRequest customerTransactionRequest,
            BankAccountEntity accountEntity, Long accountNumber, String userId, OffsetDateTime dateTime,
            TransactionType transactionType) {
        accountEntity.setUpdatedDateTime(dateTime);
        accountEntity.setUpdatedBy(userId);

        return TransactionEntity
                .builder()
                .bankAccountEntity(accountEntity)
                .message(accountNumber + "-" + customerTransactionRequest.getMessage())
                .transactionStatus("SUCCESS")
                .transactionType(transactionType)
                .createdBy(userId)
                .createdDateTime(dateTime)
                .updatedBy(userId)
                .updatedDateTime(dateTime)
                .build();
    }

    /**
     * validates the balance before updating creating the transaction.
     */
    private void validateBalance(CustomerTransactionRequest customerTransactionRequest,
            BankAccountEntity sourceAccount) {
        if (sourceAccount
                .getBalance()
                .compareTo(customerTransactionRequest.getAmount()) < 0) {
            throw new InSufficientBalanceException(sourceAccount
                    .getBalance()
                    .toString());
        }
    }

    /**
     * verify the user before transaction.
     */
    private void verifyUser(String password) {
        UserEntity userEntity = userRepository
                .findById(Long.parseLong(HeaderData
                        .getUserHeaderData()
                        .get()
                        .get("user_id")))
                .orElseThrow(IllegalArgumentException::new);
        if (!password.equals(userEntity.getPassword())) {
            throw new ValidationException();
        }
    }
}