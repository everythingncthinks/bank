package com.nithin.cybrilla.assignment.bank.employee.service;

import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccount;
import com.nithin.cybrilla.assignment.bank.common.dto.account.BankAccountResponse;
import com.nithin.cybrilla.assignment.bank.common.dto.user.CreateUserRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.user.ManageUserResponse;
import com.nithin.cybrilla.assignment.bank.common.service.AccountService;
import com.nithin.cybrilla.assignment.bank.common.service.UserService;

/**
 * account service specific for employees.
 */
public interface EmployeeBankAccountService extends AccountService {

    /**
     * A provision to create account.
     */
    BankAccountResponse createAccount(BankAccount user);
}
