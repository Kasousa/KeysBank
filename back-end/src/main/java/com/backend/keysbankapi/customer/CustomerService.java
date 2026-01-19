package com.backend.keysbankapi.customer;

import com.backend.keysbankapi.customer.dto.CreateCustomerRequest;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
  private final CustomerRepository repo;

  public CustomerService(CustomerRepository repo) {
    this.repo = repo;
  }

  public Customer create(CreateCustomerRequest req) {
    if (repo.existsByEmail(req.email())) {
      throw new IllegalArgumentException("Email já cadastrado");
    }
    Customer c = new Customer();
    c.setName(req.name());
    c.setEmail(req.email());
    return repo.save(c);
  }
}
