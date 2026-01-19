package com.backend.keysbankapi.ledger;

import com.backend.keysbankapi.ledger.dto.StatementItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Statement", description = "Endpoints para consulta de extratos e transações")
public class StatementController {

  private final StatementService service;

  public StatementController(StatementService service) {
    this.service = service;
  }

  @GetMapping("/{accountId}/statement")
  @Operation(
      summary = "Recuperar extrato da conta",
      description = "Retorna o extrato (lista de transações) de uma conta com suporte a filtros opcionais por data e tipo de transação. " +
          "O resultado inclui todas as transações (CREDIT, DEBIT) e o saldo diário (BALANCE)."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Extrato recuperado com sucesso",
          content = @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = StatementItemResponse.class))
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Conta não encontrada"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Parâmetros inválidos"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Erro interno do servidor"
      )
  })
  public List<StatementItemResponse> getStatement(
      @PathVariable UUID accountId,
      @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate,
      @RequestParam(required = false) String type) {
    
    if (startDate != null || endDate != null || type != null) {
      return service.getStatementFiltered(accountId, startDate, endDate, type);
    }
    return service.getStatement(accountId);
  }
}
