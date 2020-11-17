package com.nithin.cybrilla.assignment.bank.customer.controller;

import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayee;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayeeCreateRequest;
import com.nithin.cybrilla.assignment.bank.common.dto.payee.SameBankPayeeVerifyRequest;
import com.nithin.cybrilla.assignment.bank.common.helper.HeaderData;
import com.nithin.cybrilla.assignment.bank.customer.service.CustomerPayeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APIs for payee management.
 */
@RestController
@RequestMapping("/customer/payee")
public class CustomerPayeeController {

    @Autowired
    private CustomerPayeeService customerPayeeService;

    /**
     * API to create a payee having account in same bank.
     *
     * @param sameBankPayee input information.
     * @param modifiedBy user requesting the action
     * @return created payee information
     */
    @PostMapping
    public ResponseEntity<SameBankPayee> createSameBankPayee(@RequestBody SameBankPayeeCreateRequest sameBankPayee,
            @RequestHeader("user_id") String modifiedBy) {
        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(customerPayeeService.createSameBankPayee(sameBankPayee), HttpStatus.OK);
    }

    /**
     * API to verify payee
     *
     * @param sameBankPayeeVerifyRequest payee info for verification.
     * @param modifiedBy user requesting the action
     * @param payeeId payeeId of the payee to verify
     * @return payee information.
     */
    @PutMapping("/{payeeId}")
    public ResponseEntity<SameBankPayee> verifySameBankPayee(
            @RequestBody SameBankPayeeVerifyRequest sameBankPayeeVerifyRequest,
            @RequestHeader("user_id") String modifiedBy, @PathVariable("payeeId") Long payeeId) {
        HeaderData.setUserHeaderData(modifiedBy);
        return new ResponseEntity<>(customerPayeeService.verifySameBankPayee(sameBankPayeeVerifyRequest, payeeId),
                HttpStatus.OK);
    }

    /**
     * API to delete payee.
     *
     * @param modifiedBy user requesting the action
     * @param payeeId payee to be deleted.
     * @return status.
     */
    @DeleteMapping("/{payeeId}")
    public ResponseEntity<Void> deletePayee(@RequestHeader("user_id") String modifiedBy,
            @PathVariable("payeeId") Long payeeId) {
        HeaderData.setUserHeaderData(modifiedBy);
        customerPayeeService.deletePayee(payeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
