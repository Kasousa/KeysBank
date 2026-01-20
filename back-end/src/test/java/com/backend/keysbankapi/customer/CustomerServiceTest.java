package com.backend.keysbankapi.customer;

import com.backend.keysbankapi.customer.dto.CreateCustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private CreateCustomerRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new CreateCustomerRequest("João Silva", "joao@email.com");
    }

    @Test
    @DisplayName("Should create customer successfully with valid data")
    void testCreateCustomerSuccess() {
        // Arrange
        when(customerRepository.existsByEmail(validRequest.email())).thenReturn(false);
        Customer expectedCustomer = new Customer();
        expectedCustomer.setName(validRequest.name());
        expectedCustomer.setEmail(validRequest.email());
        when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);

        // Act
        Customer result = customerService.create(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals(validRequest.name(), result.getName());
        assertEquals(validRequest.email(), result.getEmail());
        verify(customerRepository, times(1)).existsByEmail(validRequest.email());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testCreateCustomerWithDuplicateEmail() {
        // Arrange
        when(customerRepository.existsByEmail(validRequest.email())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> customerService.create(validRequest)
        );
        assertEquals("Email já cadastrado", exception.getMessage());
        verify(customerRepository, times(1)).existsByEmail(validRequest.email());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should prevent duplicate email registration")
    void testDuplicateEmailValidation() {
        // Arrange
        CreateCustomerRequest req1 = new CreateCustomerRequest("User 1", "test@email.com");
        CreateCustomerRequest req2 = new CreateCustomerRequest("User 2", "test@email.com");

        when(customerRepository.existsByEmail("test@email.com")).thenReturn(false).thenReturn(true);
        Customer customer = new Customer();
        customer.setName(req1.name());
        customer.setEmail(req1.email());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        customerService.create(req1);
        
        // Assert - Second creation should fail
        assertThrows(IllegalArgumentException.class, () -> customerService.create(req2));
    }

    @Test
    @DisplayName("Should call repository save method")
    void testSaveIsCalledOnCreate() {
        // Arrange
        when(customerRepository.existsByEmail(validRequest.email())).thenReturn(false);
        Customer mockCustomer = new Customer();
        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        // Act
        customerService.create(validRequest);

        // Assert
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}
