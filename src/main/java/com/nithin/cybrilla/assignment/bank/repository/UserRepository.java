package com.nithin.cybrilla.assignment.bank.repository;

import com.nithin.cybrilla.assignment.bank.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for user management.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
