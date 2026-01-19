package com.backend.keysbankapi.ledger;

import com.backend.keysbankapi.ledger.dto.TransactionCreatedRequest;
import com.backend.keysbankapi.ledger.dto.TransactionCreatedResponse;
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
@RequestMapping("/transaction")
@Tag(name = "Transactions", description = "Endpoints para criar e gerenciar transações bancárias")
public class TransactionController {

  private final TransactionService service;

  public TransactionController(TransactionService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Criar uma nova transação",
      description = "Cria uma nova transação (depósito ou saque) em uma conta. Após criar a transação, o saldo diário é automaticamente recalculado. " +
          "Os tipos suportados são CREDIT (crédito/depósito) e DEBIT (débito/saque)."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Transação criada com sucesso",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = TransactionCreatedResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Erro na validação: conta não encontrada, dados inválidos ou saldo insuficiente"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Erro interno do servidor"
      )
  })
  public TransactionCreatedResponse create(@Valid @RequestBody TransactionCreatedRequest req) {
    Transaction a = service.createTransaction(req.accountId(), req.type(), req.category(), req.amount(), req.description());
    return new TransactionCreatedResponse(a.getId(), a.getType(), a.getCategory(), a.getAmount(), a.getDescription(), a.getCreatedAt());
 }
}