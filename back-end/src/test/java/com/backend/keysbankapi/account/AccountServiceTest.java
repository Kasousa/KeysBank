package com.backend.keysbankapi.account;

import com.backend.keysbankapi.customer.Customer;
import com.backend.keysbankapi.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes de AccountService")
@ActiveProfiles("test")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService;

    private Customer customer;
    private Account account;
    private CreateAccountRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID customerId = UUID.randomUUID();
        customer = new Customer(
            customerId,
            "João Silva",
            "joao@email.com",
            java.time.LocalDateTime.now()
        );

        request = new CreateAccountRequest(customerId);

        account = new Account(
            UUID.randomUUID(),
            customerId,
            "0001",
            "343316",
            "ATIVA",
            java.time.LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Deve criar conta com sucesso")
    void testCreateAccountSuccess() {
        when(customerRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(customer));

        when(accountRepository.save(any(Account.class)))
            .thenReturn(account);

        Account result = accountService.createAccount(request);

        assertThat(result)
            .isNotNull()
            .hasFieldOrPropertyWithValue("agency", "0001")
            .hasFieldOrPropertyWithValue("status", "ATIVA");

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Deve retornar erro se cliente não existe")
    void testCreateAccountCustomerNotFound() {
        when(customerRepository.findById(any(UUID.class)))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.createAccount(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Cliente não encontrado");

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar erro se cliente já tem conta")
    void testCreateAccountAlreadyExists() {
        when(customerRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(customer));

        when(accountRepository.findByCustomerId(any(UUID.class)))
            .thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.createAccount(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Cliente já possui conta");

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar conta por ID com sucesso")
    void testFindAccountByIdSuccess() {
        UUID accountId = account.getId();

        when(accountRepository.findById(accountId))
            .thenReturn(Optional.of(account));

        Optional<Account> result = accountRepository.findById(accountId);

        assertThat(result)
            .isPresent()
            .contains(account);
    }

    @Test
    @DisplayName("Deve validar agência e número da conta no login")
    void testLoginValidation() {
        when(accountRepository.findByAgencyAndAccountNumber("0001", "343316"))
            .thenReturn(Optional.of(account));

        Optional<Account> result = accountRepository
            .findByAgencyAndAccountNumber("0001", "343316");

        assertThat(result)
            .isPresent()
            .hasValueSatisfying(acc -> {
                assertThat(acc.getAgency()).isEqualTo("0001");
                assertThat(acc.getAccountNumber()).isEqualTo("343316");
            });
    }

    @Test
    @DisplayName("Deve retornar vazio se conta não existe no login")
    void testLoginNotFound() {
        when(accountRepository.findByAgencyAndAccountNumber("9999", "999999"))
            .thenReturn(Optional.empty());

        Optional<Account> result = accountRepository
            .findByAgencyAndAccountNumber("9999", "999999");

        assertThat(result).isEmpty();
    }
}
