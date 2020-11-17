package com.nithin.cybrilla.assignment.bank.repository;

import com.nithin.cybrilla.assignment.bank.common.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for bank account data management.
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
}
