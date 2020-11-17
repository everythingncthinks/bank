package com.nithin.cybrilla.assignment.bank.common.entity;

import com.nithin.cybrilla.assignment.bank.common.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Entity describing the transaction details
 */
@Entity
@Table(name = "transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "accountNumber")
    private BankAccountEntity bankAccountEntity;

    private TransactionType transactionType;

    private String message;

    private String transactionStatus;

    private OffsetDateTime createdDateTime;

    private String createdBy;

    private OffsetDateTime updatedDateTime;

    private String updatedBy;
}
