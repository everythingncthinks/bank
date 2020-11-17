package com.nithin.cybrilla.assignment.bank.repository;

import com.nithin.cybrilla.assignment.bank.common.entity.TransactionMapperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for transaction detail management.
 */
@Repository
public interface TransactionMapperRepository extends JpaRepository<TransactionMapperEntity, Long> {

}
