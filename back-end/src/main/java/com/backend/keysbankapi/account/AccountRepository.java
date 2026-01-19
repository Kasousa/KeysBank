package com.backend.keysbankapi.account;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByCustomerId(UUID customerId);
    Optional<Account> findByAgencyAndAccountNumber(String agency, String accountNumber);
}