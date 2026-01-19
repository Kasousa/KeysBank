package com.backend.keysbankapi.customer;

import com.backend.keysbankapi.customer.dto.CreateCustomerRequest;
import com.backend.keysbankapi.customer.dto.CustomerResponse;
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
@RequestMapping("/customers")
@Tag(name = "Customers", description = "Endpoints para gerenciamento de clientes do banco")
public class CustomerController {

  private final CustomerService service;

  public CustomerController(CustomerService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Criar um novo cliente",
      description = "Cria um novo cliente no sistema bancário com nome e email únicos"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Cliente criado com sucesso",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = CustomerResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Erro na validação: email já existe ou dados inválidos"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Erro interno do servidor"
      )
  })
  public CustomerResponse create(@Valid @RequestBody CreateCustomerRequest req) {
    Customer c = service.create(req);
    return new CustomerResponse(c.getId(), c.getName(), c.getEmail());
  }
}