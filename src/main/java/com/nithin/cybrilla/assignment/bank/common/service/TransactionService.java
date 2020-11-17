package com.nithin.cybrilla.assignment.bank.common.service;

import com.nithin.cybrilla.assignment.bank.common.dto.transaction.Transaction;

import java.util.List;

/**
 * Transaction service.
 */
public interface TransactionService {

    /**
     * view the transactions done by a specific user
     * @param userId
     * @return
     */
    List<Transaction> viewTransactions(Long userId);
}
