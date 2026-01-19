package com.backend.keysbankapi.account;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
  @Id
  private UUID id;

  @Column(name = "customer_id", nullable = false)
  private UUID customerId;

  @Column(nullable = false)
  private String agency;

  @Column(name = "account_number", nullable = false, unique = true)
  private String accountNumber;

  @Column(nullable = false)
  private String status; // "ATIVA"

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {
    if (id == null) id = UUID.randomUUID();
    if (createdAt == null) createdAt = Instant.now();
    if (status == null) status = "ATIVA";
    if (agency == null) agency = "0001";
    if (accountNumber == null) accountNumber = generateAccountNumber();
  }

  private String generateAccountNumber() {
    // simples pro MVP (depois você melhora)
    return String.valueOf((int)(Math.random() * 900000) + 100000);
  }

  // getters/setters
  public UUID getId() { return id; }
  public UUID getCustomerId() { return customerId; }
  public void setCustomerId(UUID customerId) { this.customerId = customerId; }
  public String getAgency() { return agency; }
  public String getAccountNumber() { return accountNumber; }
  public String getStatus() { return status; }
  public Instant getCreatedAt() { return createdAt; }
}
