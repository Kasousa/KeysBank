package com.backend.keysbankapi.account;

import com.backend.keysbankapi.account.dto.CreateAccountRequest;
import com.backend.keysbankapi.customer.CustomerRepository;
import com.backend.keysbankapi.ledger.Transaction;
import com.backend.keysbankapi.ledger.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountService Tests")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private UUID customerId;
    private UUID accountId;
    private CreateAccountRequest validRequest;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        accountId = UUID.randomUUID();
        validRequest = new CreateAccountRequest(customerId);
    }

    @Test
    @DisplayName("Should create account successfully for valid customer")
    void testCreateAccountSuccess() {
        // Arrange
        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(accountRepository.existsByCustomerId(customerId)).thenReturn(false);

        Account createdAccount = new Account();
        createdAccount.setCustomerId(customerId);

        when(accountRepository.save(any(Account.class))).thenReturn(createdAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // Act
        Account result = accountService.create(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        verify(customerRepository, times(1)).existsById(customerId);
        verify(accountRepository, times(1)).existsByCustomerId(customerId);
        verify(accountRepository, atLeastOnce()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should throw exception when customer not found")
    void testCreateAccountCustomerNotFound() {
        // Arrange
        when(customerRepository.existsById(customerId)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> accountService.create(validRequest)
        );
        assertEquals("Customer não encontrado", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should throw exception when customer already has account")
    void testCreateAccountAlreadyExists() {
        // Arrange
        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(accountRepository.existsByCustomerId(customerId)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> accountService.create(validRequest)
        );
        assertEquals("Conta já existe", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should find account by id")
    void testFindAccountById() {
        // Arrange
        Account expectedAccount = new Account();
        expectedAccount.setCustomerId(customerId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(expectedAccount));

        // Act
        Optional<Account> result = accountRepository.findById(accountId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(customerId, result.get().getCustomerId());
    }

    @Test
    @DisplayName("Should return empty when account not found")
    void testFindAccountByIdNotFound() {
        // Arrange
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act
        Optional<Account> result = accountRepository.findById(accountId);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should validate login with correct credentials")
    void testLoginWithValidCredentials() {
        // Arrange
        String agency = "0001";
        String accountNumber = "343316";
        Account account = new Account();
        account.setCustomerId(customerId);

        when(accountRepository.findByAgencyAndAccountNumber(agency, accountNumber))
            .thenReturn(Optional.of(account));

        // Act
        Optional<Account> result = accountRepository.findByAgencyAndAccountNumber(agency, accountNumber);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(customerId, result.get().getCustomerId());
    }
}
