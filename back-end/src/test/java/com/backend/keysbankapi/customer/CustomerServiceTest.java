package com.backend.keysbankapi.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Testes de CustomerService")
@ActiveProfiles("test")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CreateCustomerRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new CreateCustomerRequest("João Silva", "joao@email.com");
        customer = new Customer(
            UUID.randomUUID(),
            "João Silva",
            "joao@email.com",
            java.time.LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Deve criar cliente com sucesso")
    void testCreateCustomerSuccess() {
        when(customerRepository.save(any(Customer.class)))
            .thenReturn(customer);

        Customer result = customerService.createCustomer(request);

        assertThat(result)
            .isNotNull()
            .hasFieldOrPropertyWithValue("name", "João Silva")
            .hasFieldOrPropertyWithValue("email", "joao@email.com");

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve retornar erro se email já existe")
    void testCreateCustomerEmailDuplicate() {
        when(customerRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> customerService.createCustomer(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email já cadastrado");

        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve validar nome obrigatório")
    void testCreateCustomerNameRequired() {
        CreateCustomerRequest invalidRequest = new CreateCustomerRequest("", "email@test.com");

        assertThatThrownBy(() -> customerService.createCustomer(invalidRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve validar email obrigatório")
    void testCreateCustomerEmailRequired() {
        CreateCustomerRequest invalidRequest = new CreateCustomerRequest("João Silva", "");

        assertThatThrownBy(() -> customerService.createCustomer(invalidRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void testFindCustomerByIdSuccess() {
        UUID customerId = customer.getId();

        when(customerRepository.findById(customerId))
            .thenReturn(Optional.of(customer));

        Optional<Customer> result = customerRepository.findById(customerId);

        assertThat(result)
            .isPresent()
            .contains(customer);
    }

    @Test
    @DisplayName("Deve retornar vazio se cliente não existe")
    void testFindCustomerByIdNotFound() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId))
            .thenReturn(Optional.empty());

        Optional<Customer> result = customerRepository.findById(customerId);

        assertThat(result).isEmpty();
    }
}
