package com.nithin.cybrilla.assignment.bank.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Entity describing the bank account details
 */
@Entity
@Table(name = "bank_account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountEntity {

    @Id
    @GeneratedValue
    private Long accountNumber;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserEntity userEntity;

    @Column
    private String accountStatus;

    @Column
    private String accountType;

    @Column
    private String bankCode;

    @Column
    private BigDecimal balance;

    @Column
    private boolean isActive = true;

    @Column
    private OffsetDateTime createdDateTime;

    @Column
    private String createdBy;

    @Column
    private OffsetDateTime updatedDateTime;

    @Column
    private String updatedBy;

}
