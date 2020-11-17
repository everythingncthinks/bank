package com.nithin.cybrilla.assignment.bank.employee.controller;

import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccount;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountResponse;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.employee.service.EmployeeBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * APIs to manage user account details.
 */
@RestController
@RequestMapping("/employee/account")
public class EmployeeAccountController {

    @Autowired
    @Qualifier("employeeBankAccountServiceImpl")
    private EmployeeBankAccountService employeeBankAccountService;

    /**
     * API to create account.
     *
     * @param bankAccount account information
     * @param modifiedBy user requesting the action
     * @return account information.
     */
    @PostMapping
    public ResponseEntity<BankAccountResponse> createAccount(@RequestBody BankAccount bankAccount,
            @RequestHeader("user_id") String modifiedBy) {
        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(employeeBankAccountService.createAccount(bankAccount), HttpStatus.OK);
    }

    /**
     * API to update account information
     *
     * @param bankAccount updated account information.
     * @param modifiedBy user requesting the action
     * @param accountNumber accountNumber of the account holder whose account need to be updated.
     * @return updated account information.
     */
    @PutMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> updateAccount(@RequestBody BankAccount bankAccount,
            @RequestHeader("user_id") String modifiedBy, @PathVariable("accountNumber") Long accountNumber) {
        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(employeeBankAccountService.updateAccount(bankAccount, accountNumber),
                HttpStatus.OK);
    }

    /**
     * API to get account information.
     *
     * @param modifiedBy user requesting the action
     * @param accountNumber queried accountNumber.
     * @return account information.
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> getBankAccount(@RequestHeader("user_id") String modifiedBy,
            @PathVariable("accountNumber") Long accountNumber) {
        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(employeeBankAccountService.getAccount(accountNumber), HttpStatus.OK);
    }

}
