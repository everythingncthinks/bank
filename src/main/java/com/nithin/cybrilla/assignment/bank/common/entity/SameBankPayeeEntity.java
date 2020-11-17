package com.nithin.cybrilla.assignment.bank.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Entity describing the payee details for the payees using our bank.
 */
@Entity
@Table(name = "same_bank_payee")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SameBankPayeeEntity {

    @Id
    @GeneratedValue
    private Long payeeId;

    @ManyToOne
    private BankAccountEntity bankAccountEntity;

    @ManyToOne
    private UserEntity userEntity;

    private String displayName;

    private boolean isVerified;

    private OffsetDateTime createdDateTime;

    private String createdBy;

    private OffsetDateTime updatedDateTime;

    private String updatedBy;
}
