package com.backend.keysbankapi.account;

import com.backend.keysbankapi.account.dto.AccountResponse;
import com.backend.keysbankapi.account.dto.CreateAccountRequest;
import com.backend.keysbankapi.account.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Endpoints para gerenciamento de contas bancárias")
public class AccountController {

  private final AccountService service;

  public AccountController(AccountService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Criar uma nova conta bancária",
      description = "Cria uma nova conta vinculada a um cliente existente. A conta recebe automaticamente um bônus de abertura de R$ 100,00"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Conta criada com sucesso",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = AccountResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Erro: Cliente não encontrado ou cliente já possui conta"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Erro interno do servidor"
      )
  })
  public AccountResponse create(@Valid @RequestBody CreateAccountRequest req) {
    Account a = service.create(req);
    return new AccountResponse(a.getId(), a.getCustomerId(), a.getAgency(), a.getAccountNumber(), a.getStatus());
  }

  @GetMapping("/login")
  @Operation(
      summary = "Validar e fazer login em uma conta",
      description = "Valida as credenciais da conta (agência e número) e retorna o ID da conta para usar nos demais endpoints. " +
          "Este é o primeiro passo do fluxo de autenticação."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Conta validada com sucesso",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = LoginResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Conta não encontrada com os dados fornecidos"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Erro interno do servidor"
      )
  })
  public LoginResponse validateAccount(
      @RequestParam String agency,
      @RequestParam String accountNumber) {
    return service.validateAndGetAccount(agency, accountNumber);
  }
}