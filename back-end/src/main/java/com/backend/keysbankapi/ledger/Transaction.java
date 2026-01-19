package com.backend.keysbankapi.ledger;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {
  @Id
  private UUID id;

  @Column(name = "account_id", nullable = false)
  private UUID accountId;

  @Column(nullable = false)
  private String type; // "CREDIT" / "DEBIT"

  @Column(nullable = false)
  private String category; // "BONUS_ABERTURA"

  @Column(nullable = false, precision = 18, scale = 2)
  private BigDecimal amount;

  private String description;

  @Column(name = "correlation_id")
  private UUID correlationId;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {
    if (id == null) id = UUID.randomUUID();
    if (createdAt == null) createdAt = Instant.now();
  }

  // getters/setters
  public UUID getId() { return id; }
  public UUID getAccountId() { return accountId; }
  public void setAccountId(UUID accountId) { this.accountId = accountId; }
  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
  public String getCategory() { return category; }
  public void setCategory(String category) { this.category = category; }
  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public UUID getCorrelationId() { return correlationId; }
  public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }
  public Instant getCreatedAt() { return createdAt; }
}