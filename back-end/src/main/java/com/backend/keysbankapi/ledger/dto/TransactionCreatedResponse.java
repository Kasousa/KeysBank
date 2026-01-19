package com.backend.keysbankapi.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(name = "TransactionCreatedResponse", description = "Dados de uma transação retornado pela API após sua criação")
public record TransactionCreatedResponse(
    @Schema(description = "ID único da transação (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,
    @Schema(description = "Tipo de transação (CREDIT ou DEBIT)", example = "DEBIT")
    String type,
    @Schema(description = "Categoria da transação", example = "SAQUE")
    String category,
    @Schema(description = "Valor da transação em reais", example = "50.00")
    BigDecimal amount,
    @Schema(description = "Descrição da transação", example = "Saque no caixa eletrônico")
    String description,
    @Schema(description = "Data e hora da criação da transação (ISO 8601)", example = "2026-01-15T22:45:34.000Z")
    Instant createdAt
) {}
