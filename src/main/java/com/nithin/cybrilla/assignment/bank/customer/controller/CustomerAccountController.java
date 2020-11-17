package com.nithin.cybrilla.assignment.bank.customer.controller;

import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountUpdateRequest;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.common.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APIs to manage user account details.
 */
@RestController
@RequestMapping("/customer/account")
public class CustomerAccountController {

    @Autowired
    @Qualifier("customerBankAccountServiceImpl")
    private AccountService accountService;

    /**
     * API to update account information
     *
     * @param bankAccount updated account information.
     * @param modifiedBy user requesting the action
     * @param accountNumber accountNumber of the account holder whose account need to be updated.
     * @return updated account information.
     */
    @PutMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> updateAccount(@RequestBody BankAccountUpdateRequest bankAccount,
            @RequestHeader("user_id") String modifiedBy, @PathVariable("accountNumber") Long accountNumber) {
        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(accountService.updateAccount(bankAccount, accountNumber), HttpStatus.OK);
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
        return new ResponseEntity<>(accountService.getAccount(accountNumber), HttpStatus.OK);
    }

}
