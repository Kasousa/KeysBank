package com.backend.keysbankapi.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CreateCustomerRequest", description = "Dados para criação de um novo cliente")
public record CreateCustomerRequest(
  @Schema(description = "Nome completo do cliente", example = "João Silva", minLength = 1)
  @NotBlank String name,
  @Schema(description = "Email único do cliente", example = "joao.silva@email.com", format = "email")
  @Email @NotBlank String email
) {}
