package com.backend.keysbankapi.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(name = "StatementItemResponse", description = "Item individual de uma transação no extrato")
public record StatementItemResponse(
    @Schema(description = "ID único da transação (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID transactionId,
    @Schema(description = "Tipo da transação (CREDIT, DEBIT ou BALANCE)", example = "CREDIT", allowableValues = {"CREDIT", "DEBIT", "BALANCE"})
    String type,
    @Schema(description = "Categoria da transação", example = "BONUS_ABERTURA")
    String category,
    @Schema(description = "Valor da transação em reais", example = "100.00")
    BigDecimal amount,
    @Schema(description = "Descrição da transação", example = "Bônus de abertura de conta")
    String description,
    @Schema(description = "Data e hora da transação (ISO 8601)", example = "2026-01-15T22:45:34.000Z")
    Instant createdAt
) {}
