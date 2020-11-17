package com.nithin.cybrilla.assignment.bank.employee.controller;

import com.nithin.cybrilla.assignment.bank.common.dto.transaction.EmployeeTransactionRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.transaction.Transaction;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.employee.service.EmployeeTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API for employees to do Transaction.
 */
@RestController
@RequestMapping("/employee/transaction")
public class EmployeeTransactionController {

    @Autowired
    @Qualifier("employeeTransactionServiceImpl")
    private EmployeeTransactionService employeeTransactionService;

    /**
     * API to make a transaction.
     *
     * @param customerTransactionRequest transaction information.
     * @param modifiedBy action done by
     * @return transaction data.
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody EmployeeTransactionRequest customerTransactionRequest,
            @RequestHeader("user_id") String modifiedBy) {
        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(employeeTransactionService.createTransaction(customerTransactionRequest),
                HttpStatus.OK);
    }

    /**
     * API to view transactions
     *
     * @param modifiedBy user requesting action.
     * @param accountNumber accountNumber of account holder whose transaction is to be viewed.
     * @return List of all transactions by the user.
     */
    @GetMapping("/statement/{accountNumber}")
    public ResponseEntity<List<Transaction>> viewTransactions(@RequestHeader("user_id") String modifiedBy,
            @PathVariable("accountNumber") Long accountNumber) {
        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(employeeTransactionService.viewTransactions(accountNumber), HttpStatus.OK);
    }
}
