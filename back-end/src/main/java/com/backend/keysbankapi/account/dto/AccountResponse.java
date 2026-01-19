package com.backend.keysbankapi.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "AccountResponse", description = "Dados de uma conta bancária retornado pela API")
public record AccountResponse(
  @Schema(description = "ID único da conta (UUID)", example = "b837e6e2-1b3c-4267-825f-741fb798f066")
  UUID id,
  @Schema(description = "ID do cliente proprietário da conta", example = "71475965-0ea9-46e7-87c7-ca98320189af")
  UUID customerId,
  @Schema(description = "Número da agência bancária", example = "0001")
  String agency,
  @Schema(description = "Número da conta bancária", example = "343316")
  String accountNumber,
  @Schema(description = "Status da conta (ATIVA ou INATIVA)", example = "ATIVA")
  String status
) {}
