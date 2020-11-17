package com.nithin.cybrilla.assignment.bank.customer.service;

import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayee;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayeeCreateRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayeeVerifyRequest;

/**
 * Payee service
 */
public interface CustomerPayeeService {

    /**
     * method to create payee information
     */
    SameBankPayee createSameBankPayee(SameBankPayeeCreateRequest sameBankPayee);

    /**
     * Method to verify payee info, currently this is just a simple process to show that this needs to be in place.
     */
    SameBankPayee verifySameBankPayee(SameBankPayeeVerifyRequest sameBankPayeeVerifyRequest, Long payeeId);

    /**
     * Delete payee with given payeeId.
     */
    void deletePayee(Long payeeId);
}
