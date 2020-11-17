package com.nithin.cybrilla.assignment.bank.employee.service;

import com.nithin.cybrilla.assignment.bank.common.dto.transaction.EmployeeTransactionRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.transaction.Transaction;
import com.nithin.cybrilla.assignment.bank.common.service.TransactionService;

/**
 * Transaction service specifically for employee.
 */
public interface EmployeeTransactionService extends TransactionService {

    /**
     * Method to create a transaction.
     */
    Transaction createTransaction(EmployeeTransactionRequest employeeTransactionRequest);
}
