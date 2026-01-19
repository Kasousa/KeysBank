package com.backend.keysbankapi.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
  boolean existsByEmail(String email);
}