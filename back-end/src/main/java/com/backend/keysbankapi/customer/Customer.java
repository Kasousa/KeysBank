package com.backend.keysbankapi.customer;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {
  @Id
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {
    if (id == null) id = UUID.randomUUID();
    if (createdAt == null) createdAt = Instant.now();
  }

  // getters/setters
  public UUID getId() { return id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public Instant getCreatedAt() { return createdAt; }
}
