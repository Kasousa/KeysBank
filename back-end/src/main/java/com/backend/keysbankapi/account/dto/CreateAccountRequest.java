package com.backend.keysbankapi.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(name = "CreateAccountRequest", description = "Dados para criação de uma nova conta bancária")
public record CreateAccountRequest(
  @Schema(description = "ID único do cliente (UUID) que será proprietário da conta", example = "71475965-0ea9-46e7-87c7-ca98320189af")
  @NotNull UUID customerId
) {}