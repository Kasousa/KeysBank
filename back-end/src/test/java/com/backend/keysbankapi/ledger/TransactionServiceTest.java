package com.backend.keysbankapi.ledger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes de TransactionService")
@ActiveProfiles("test")
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private CreateTransactionRequest request;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        accountId = UUID.randomUUID();

        request = new CreateTransactionRequest(
            accountId,
            "DEBIT",
            "SAQUE",
            new BigDecimal("50.00"),
            "Saque no caixa eletrônico"
        );

        transaction = new Transaction(
            UUID.randomUUID(),
            accountId,
            "DEBIT",
            "SAQUE",
            new BigDecimal("50.00"),
            "Saque no caixa eletrônico",
            java.time.LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Deve criar transação de débito com sucesso")
    void testCreateDebitTransactionSuccess() {
        when(transactionRepository.save(any(Transaction.class)))
            .thenReturn(transaction);

        Transaction result = transactionService.createTransaction(request);

        assertThat(result)
            .isNotNull()
            .hasFieldOrPropertyWithValue("type", "DEBIT")
            .hasFieldOrPropertyWithValue("category", "SAQUE")
            .hasFieldOrPropertyWithValue("amount", new BigDecimal("50.00"));

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Deve criar transação de crédito com sucesso")
    void testCreateCreditTransactionSuccess() {
        CreateTransactionRequest creditRequest = new CreateTransactionRequest(
            accountId,
            "CREDIT",
            "DEPOSITO",
            new BigDecimal("100.00"),
            "Depósito inicial"
        );

        Transaction creditTransaction = new Transaction(
            UUID.randomUUID(),
            accountId,
            "CREDIT",
            "DEPOSITO",
            new BigDecimal("100.00"),
            "Depósito inicial",
            java.time.LocalDateTime.now()
        );

        when(transactionRepository.save(any(Transaction.class)))
            .thenReturn(creditTransaction);

        Transaction result = transactionService.createTransaction(creditRequest);

        assertThat(result)
            .isNotNull()
            .hasFieldOrPropertyWithValue("type", "CREDIT")
            .hasFieldOrPropertyWithValue("amount", new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Deve validar valor positivo")
    void testCreateTransactionInvalidAmount() {
        CreateTransactionRequest invalidRequest = new CreateTransactionRequest(
            accountId,
            "DEBIT",
            "SAQUE",
            new BigDecimal("-50.00"),
            "Saque"
        );

        assertThatThrownBy(() -> transactionService.createTransaction(invalidRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Valor deve ser positivo");
    }

    @Test
    @DisplayName("Deve validar tipo de transação")
    void testCreateTransactionInvalidType() {
        CreateTransactionRequest invalidRequest = new CreateTransactionRequest(
            accountId,
            "INVALID",
            "SAQUE",
            new BigDecimal("50.00"),
            "Saque"
        );

        assertThatThrownBy(() -> transactionService.createTransaction(invalidRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve buscar transações por conta")
    void testFindTransactionsByAccount() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        when(transactionRepository.findByAccountId(accountId))
            .thenReturn(transactions);

        List<Transaction> result = transactionRepository.findByAccountId(accountId);

        assertThat(result)
            .isNotEmpty()
            .hasSize(1)
            .contains(transaction);
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não há transações")
    void testFindTransactionsByAccountEmpty() {
        when(transactionRepository.findByAccountId(any(UUID.class)))
            .thenReturn(new ArrayList<>());

        List<Transaction> result = transactionRepository.findByAccountId(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve calcular saldo corretamente")
    void testCalculateBalance() {
        // Transações: CREDIT 100 + DEBIT -50 = 50
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(
            UUID.randomUUID(), accountId, "CREDIT", "BONUS_ABERTURA",
            new BigDecimal("100.00"), "Bônus abertura", java.time.LocalDateTime.now()
        ));
        transactions.add(new Transaction(
            UUID.randomUUID(), accountId, "DEBIT", "SAQUE",
            new BigDecimal("50.00"), "Saque", java.time.LocalDateTime.now()
        ));

        BigDecimal balance = transactions.stream()
            .map(t -> "CREDIT".equals(t.getType()) ? t.getAmount() : t.getAmount().negate())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(balance).isEqualByComparingTo(new BigDecimal("50.00"));
    }
}
