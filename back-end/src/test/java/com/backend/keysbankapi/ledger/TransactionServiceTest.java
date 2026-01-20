package com.backend.keysbankapi.ledger;

import com.backend.keysbankapi.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionRepository Tests")
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    private UUID accountId;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should find transactions by account id in descending order")
    void testFindTransactionsByAccountIdDesc() {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        Transaction tx1 = new Transaction();
        tx1.setAccountId(accountId);
        tx1.setType("CREDIT");
        tx1.setAmount(new BigDecimal("500.00"));
        transactions.add(tx1);

        Transaction tx2 = new Transaction();
        tx2.setAccountId(accountId);
        tx2.setType("DEBIT");
        tx2.setAmount(new BigDecimal("100.00"));
        transactions.add(tx2);

        when(transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId))
            .thenReturn(transactions);

        // Act
        List<Transaction> result = transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("CREDIT", result.get(0).getType());
        assertEquals("DEBIT", result.get(1).getType());
        verify(transactionRepository, times(1)).findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    @Test
    @DisplayName("Should return empty list when no transactions found")
    void testFindTransactionsByAccountIdEmpty() {
        // Arrange
        when(transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId))
            .thenReturn(new ArrayList<>());

        // Act
        List<Transaction> result = transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    @Test
    @DisplayName("Should calculate balance from multiple transactions")
    void testCalculateBalanceFromTransactions() {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();

        Transaction credit1 = new Transaction();
        credit1.setType("CREDIT");
        credit1.setAmount(new BigDecimal("1000.00"));
        transactions.add(credit1);

        Transaction debit1 = new Transaction();
        debit1.setType("DEBIT");
        debit1.setAmount(new BigDecimal("300.00"));
        transactions.add(debit1);

        Transaction credit2 = new Transaction();
        credit2.setType("CREDIT");
        credit2.setAmount(new BigDecimal("200.00"));
        transactions.add(credit2);

        // Act
        BigDecimal balance = transactions.stream()
            .map(tx -> "CREDIT".equals(tx.getType()) 
                ? tx.getAmount() 
                : tx.getAmount().negate())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Assert
        assertEquals(new BigDecimal("900.00"), balance);
    }

    @Test
    @DisplayName("Should differentiate credit and debit transactions")
    void testTransactionTypeDistinction() {
        // Arrange
        Transaction credit = new Transaction();
        credit.setType("CREDIT");
        credit.setCategory("DEPOSITO");
        credit.setAmount(new BigDecimal("500.00"));

        Transaction debit = new Transaction();
        debit.setType("DEBIT");
        debit.setCategory("SAQUE");
        debit.setAmount(new BigDecimal("100.00"));

        // Act & Assert
        assertEquals("CREDIT", credit.getType());
        assertEquals("DEBIT", debit.getType());
        assertTrue(credit.getAmount().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(debit.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Should handle transaction with different categories")
    void testTransactionCategories() {
        // Arrange
        String[] categories = {"SALARIO", "DEPOSITO", "SAQUE", "TRANSFERENCIA", "PAGAMENTO"};

        // Act & Assert
        for (String category : categories) {
            Transaction tx = new Transaction();
            tx.setCategory(category);
            assertEquals(category, tx.getCategory());
        }
    }

    @Test
    @DisplayName("Should maintain transaction descriptions")
    void testTransactionDescription() {
        // Arrange
        String description = "Pagamento de conta de água";
        Transaction tx = new Transaction();

        // Act
        tx.setDescription(description);

        // Assert
        assertEquals(description, tx.getDescription());
    }
}
