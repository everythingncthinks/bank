package com.nithin.cybrilla.assignment.bank.common.service;

import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountUpdateRequest;

/**
 * Account service
 */
public interface AccountService {

    /**
     * Method to update the account information.
     */
    BankAccountResponse updateAccount(BankAccountUpdateRequest accountUpdateRequest, Long userId);

    /**
     * Method to get the account information.
     */
    BankAccountResponse getAccount(Long accountNumber);
}
