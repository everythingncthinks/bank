package com.nithin.cybrilla.assignment.bank.customer.service;

import com.nithin.cybrilla.assignment.bank.common.dto.transaction.CustomerTransactionRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.transaction.Transaction;
import com.nithin.cybrilla.assignment.bank.common.service.TransactionService;

import java.util.List;

public interface CustomerTransactionService extends TransactionService {

    Transaction createTransaction(CustomerTransactionRequest customerTransactionRequest);
}
