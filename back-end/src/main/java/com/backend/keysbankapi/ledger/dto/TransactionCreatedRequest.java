package com.backend.keysbankapi.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "TransactionCreatedRequest", description = "Dados para criar uma nova transação bancária")
public record TransactionCreatedRequest(
  @Schema(description = "ID único da conta (UUID) onde a transação será realizada", example = "b837e6e2-1b3c-4267-825f-741fb798f066")
  @NotNull UUID accountId,
  @Schema(description = "Tipo de transação", example = "CREDIT", allowableValues = {"CREDIT", "DEBIT"})
  @NotBlank String type,
  @Schema(description = "Categoria/motivo da transação", example = "SAQUE")
  @NotBlank String category,
  @Schema(description = "Valor da transação em reais (deve ser positivo)", example = "50.00")
  @NotNull @Positive BigDecimal amount,
  @Schema(description = "Descrição da transação", example = "Saque no caixa eletrônico")
  @NotBlank String description
) {}