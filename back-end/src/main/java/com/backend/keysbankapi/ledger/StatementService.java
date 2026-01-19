package com.backend.keysbankapi.ledger;

import com.backend.keysbankapi.account.AccountRepository;
import com.backend.keysbankapi.ledger.dto.StatementItemResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class StatementService {

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;

  public StatementService(
      TransactionRepository transactionRepository,
      AccountRepository accountRepository
  ) {
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
  }

  public List<StatementItemResponse> getStatement(UUID accountId) {

    if (!accountRepository.existsById(accountId)) {
      throw new IllegalArgumentException("Conta não encontrada");
    }

    return transactionRepository
        .findByAccountIdOrderByCreatedAtDesc(accountId)
        .stream()
        .map(tx -> new StatementItemResponse(
            tx.getId(),
            tx.getType(),
            tx.getCategory(),
            tx.getAmount(),
            tx.getDescription(),
            tx.getCreatedAt()
        ))
        .toList();
  }

  public List<StatementItemResponse> getStatementFiltered(UUID accountId, LocalDate startDate, LocalDate endDate, String type) {

    if (!accountRepository.existsById(accountId)) {
      throw new IllegalArgumentException("Conta não encontrada");
    }

    // Converter LocalDate para Instant
    Instant startInstant = null;
    Instant endInstant = null;

    if (startDate != null && endDate != null) {
      startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
      endInstant = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
    }

    List<Transaction> transactions;

    // Aplicar filtros
    if (startInstant != null && type != null) {
      // Ambos filtros
      transactions = transactionRepository.findByAccountIdAndTypeAndDateRangeOrderByCreatedAtDesc(accountId, type, startInstant, endInstant);
    } else if (startInstant != null) {
      // Apenas filtro de data
      transactions = transactionRepository.findByAccountIdAndDateRangeOrderByCreatedAtDesc(accountId, startInstant, endInstant);
    } else if (type != null) {
      // Apenas filtro de tipo
      transactions = transactionRepository.findByAccountIdAndTypeOrderByCreatedAtDesc(accountId, type);
    } else {
      // Sem filtros
      transactions = transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    return transactions
        .stream()
        .map(tx -> new StatementItemResponse(
            tx.getId(),
            tx.getType(),
            tx.getCategory(),
            tx.getAmount(),
            tx.getDescription(),
            tx.getCreatedAt()
        ))
        .toList();
  }
}