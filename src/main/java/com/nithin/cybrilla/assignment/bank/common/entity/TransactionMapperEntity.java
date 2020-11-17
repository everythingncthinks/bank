package com.nithin.cybrilla.assignment.bank.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * Entity describing the transaction details of source and destination accounts.
 */
@Entity
@Table(name = "transaction_mapper")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMapperEntity {

    @Id
    @GeneratedValue
    private Long transactionMapperId;

    @OneToOne
    private TransactionEntity transactionFrom;

    @OneToOne
    private TransactionEntity transactionTo;

    private OffsetDateTime createdDateTime;

    private String createdBy;

    private OffsetDateTime updatedDateTime;
}
