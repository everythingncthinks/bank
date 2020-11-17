package com.nithin.cybrilla.assignment.bank.employee.service;

import com.nithin.cybrilla.assignment.bank.common.dto.transaction.EmployeeTransactionRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.transaction.Transaction;
import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import com.nithin.cybrilla.assignment.bank.common.entity.TransactionEntity;
import com.nithin.cybrilla.assignment.bank.common.enums.TransactionType;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.repository.BankAccountRepository;
import com.nithin.cybrilla.assignment.bank.repository.TransactionRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transaction service specifically for employee.
 */
@Service("employeeTransactionServiceImpl")
public class EmployeeTransactionServiceImpl implements EmployeeTransactionService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    /**
     * Method to create a transaction.
     */
    public Transaction createTransaction(EmployeeTransactionRequest customerTransactionRequest) {
        BankAccountEntity accountEntity = bankAccountRepository
                .findById(customerTransactionRequest.getAccountNumber())
                .orElseThrow(IllegalArgumentException::new);
        if (TransactionType.CREDIT.equals(customerTransactionRequest.getTransactionType())) {
            accountEntity.setBalance(accountEntity
                    .getBalance()
                    .add(customerTransactionRequest.getAmount()));
        } else if (TransactionType.DEBIT.equals(customerTransactionRequest.getTransactionType())) {
            accountEntity.setBalance(accountEntity
                    .getBalance()
                    .subtract(customerTransactionRequest.getAmount()));
        }

        String userId = HeaderData
                .getUserHeaderData()
                .get()
                .get("user_id");
        OffsetDateTime dateTime = OffsetDateTime.now();
        TransactionEntity transaction =
                createTransactionEntity(customerTransactionRequest, accountEntity, userId, dateTime);
        TransactionEntity transactionEntity = transactionRepository.saveAndFlush(transaction);
        bankAccountRepository.saveAndFlush(accountEntity);
        Transaction transactionResponse = mapper.map(transactionEntity, Transaction.class);
        transactionResponse.setTransactionDateTime(transactionEntity.getCreatedDateTime());
        transactionResponse.setAccountNumber(transactionEntity
                .getBankAccountEntity()
                .getAccountNumber());
        return transactionResponse;
    }

    /**
     * View the transactions
     */
    @Override
    public List<Transaction> viewTransactions(Long userId) {

        List<TransactionEntity> transactions = transactionRepository.findByBankAccountEntity(bankAccountRepository
                .findById(userId)
                .orElseThrow(IllegalArgumentException::new));
        return transactions
                .stream()
                .map(transactionEntity -> {
                    Transaction transaction = mapper.map(transactionEntity, Transaction.class);
                    transaction.setTransactionDateTime(transactionEntity.getCreatedDateTime());
                    transaction.setAccountNumber(transactionEntity
                            .getBankAccountEntity()
                            .getAccountNumber());
                    return transaction;
                })
                .collect(Collectors.toList());
    }

    /**
     * Create the transaction entity with input data.
     */
    private TransactionEntity createTransactionEntity(EmployeeTransactionRequest employeeTransactionRequest,
            BankAccountEntity accountEntity, String userId, OffsetDateTime dateTime) {
        accountEntity.setUpdatedDateTime(dateTime);
        accountEntity.setUpdatedBy(userId);

        return TransactionEntity
                .builder()
                .bankAccountEntity(accountEntity)
                .message("From Branch -" + employeeTransactionRequest.getMessage())
                .transactionStatus("SUCCESS")
                .transactionType(employeeTransactionRequest.getTransactionType())
                .createdBy(userId)
                .createdDateTime(dateTime)
                .updatedBy(userId)
                .updatedDateTime(dateTime)
                .build();
    }
}