package com.nithin.cybrilla.assignment.bank.repository;

import com.nithin.cybrilla.assignment.bank.common.entity.SameBankPayeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for same bank payee management.
 */
@Repository
public interface SameBankPayeeRepository extends JpaRepository<SameBankPayeeEntity, Long> {
}
