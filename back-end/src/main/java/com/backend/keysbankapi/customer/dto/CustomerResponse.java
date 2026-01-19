package com.backend.keysbankapi.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "CustomerResponse", description = "Dados de um cliente retornado pela API")
public record CustomerResponse(
  @Schema(description = "ID único do cliente (UUID)", example = "71475965-0ea9-46e7-87c7-ca98320189af")
  UUID id,
  @Schema(description = "Nome completo do cliente", example = "João Silva")
  String name,
  @Schema(description = "Email único do cliente", example = "joao.silva@email.com")
  String email
) {}