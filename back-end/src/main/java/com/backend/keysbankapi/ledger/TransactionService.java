package com.backend.keysbankapi.ledger;

import com.backend.keysbankapi.account.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;

  public TransactionService(
      TransactionRepository transactionRepository,
      AccountRepository accountRepository
  ) {
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
  }

  public Transaction createTransaction(UUID accountId, String type, String category, BigDecimal amount, String description) {

    if (!accountRepository.existsById(accountId)) {
      throw new IllegalArgumentException("Conta não encontrada");
    }

    // Cria transação
    Transaction transacao = new Transaction();
    transacao.setAccountId(accountId);
    transacao.setType(type);
    transacao.setCategory(category);
    transacao.setAmount(amount);
    transacao.setDescription(description);
    Transaction savedTransaction = transactionRepository.save(transacao);

    // Calcula e atualiza o balance do dia
    updateDayBalance(accountId, savedTransaction.getCreatedAt());

    return savedTransaction;
  }

  private void updateDayBalance(UUID accountId, Instant transactionDate) {
    // Obter o início e fim do dia
    LocalDate today = transactionDate.atZone(ZoneId.systemDefault()).toLocalDate();
    Instant startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
    Instant endOfDay = today.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

    // Buscar todas as transações da conta até o final do dia (incluindo a recém criada)
    List<Transaction> allTransactions = transactionRepository
        .findByAccountIdOrderByCreatedAtDesc(accountId);

    // Calcular o saldo total até esse dia
    BigDecimal balance = BigDecimal.ZERO;
    for (Transaction tx : allTransactions) {
      // Parar quando ultrapassar o dia (transações são ordenadas por data desc)
      if (tx.getCreatedAt().isBefore(startOfDay)) {
        break;
      }
      // Pular transações de balance
      if ("BALANCE".equals(tx.getType())) {
        continue;
      }
      // Adicionar ou subtrair baseado no tipo
      if ("CREDIT".equals(tx.getType())) {
        balance = balance.add(tx.getAmount());
      } else if ("DEBIT".equals(tx.getType())) {
        balance = balance.subtract(tx.getAmount());
      }
    }

    // Deletar balance anterior do mesmo dia (se existir)
    List<Transaction> existingBalances = transactionRepository
        .findBalancesByAccountIdAndDate(accountId, startOfDay, endOfDay);
    for (Transaction balance_tx : existingBalances) {
      transactionRepository.delete(balance_tx);
    }

    // Criar nova transação de balance
    Transaction balanceTransaction = new Transaction();
    balanceTransaction.setAccountId(accountId);
    balanceTransaction.setType("BALANCE");
    balanceTransaction.setCategory("DAILY_BALANCE");
    balanceTransaction.setAmount(balance);
    balanceTransaction.setDescription("Saldo do dia");
    transactionRepository.save(balanceTransaction);
  }
}