package com.nithin.cybrilla.assignment.bank.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Entity describing the user details
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    private Long userId;

    @Column
    private String userName;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String userType;

    @Column
    private boolean isActive;

    @Column
    private OffsetDateTime createdDateTime;

    @Column
    private String createdBy;

    private OffsetDateTime updatedDateTime;

    private String updatedBy;
}
