package com.backend.keysbankapi.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "LoginResponse", description = "Resposta do endpoint de validação/login de conta")
public class LoginResponse {
  @Schema(description = "ID único da conta (UUID) para usar nos demais endpoints", example = "b837e6e2-1b3c-4267-825f-741fb798f066")
  private UUID accountId;
  @Schema(description = "Número da agência bancária", example = "0001")
  private String agency;
  @Schema(description = "Número da conta bancária", example = "343316")
  private String accountNumber;
  @Schema(description = "Nome do cliente proprietário da conta", example = "João Silva")
  private String customerName;

  public LoginResponse(UUID accountId, String agency, String accountNumber, String customerName) {
    this.accountId = accountId;
    this.agency = agency;
    this.accountNumber = accountNumber;
    this.customerName = customerName;
  }

  public UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(UUID accountId) {
    this.accountId = accountId;
  }

  public String getAgency() {
    return agency;
  }

  public void setAgency(String agency) {
    this.agency = agency;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }
}
