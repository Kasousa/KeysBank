package com.backend.keysbankapi.account;

import com.backend.keysbankapi.account.dto.CreateAccountRequest;
import com.backend.keysbankapi.account.dto.LoginResponse;
import com.backend.keysbankapi.customer.Customer;
import com.backend.keysbankapi.customer.CustomerRepository;
import com.backend.keysbankapi.ledger.Transaction;
import com.backend.keysbankapi.ledger.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class AccountService {
  private final AccountRepository accountRepo;
  private final CustomerRepository customerRepo;
  private final TransactionRepository txRepo;

  public AccountService(AccountRepository accountRepo, CustomerRepository customerRepo, TransactionRepository txRepo) {
    this.accountRepo = accountRepo;
    this.customerRepo = customerRepo;
    this.txRepo = txRepo;
  }

  @Transactional
  public Account create(CreateAccountRequest req) {
    if (!customerRepo.existsById(req.customerId())) {
      throw new IllegalArgumentException("Customer não encontrado");
    }

    if (accountRepo.existsByCustomerId(req.customerId())) {
      throw new IllegalArgumentException("Conta já existe");
    }

    Account account = new Account();
    account.setCustomerId(req.customerId());
    account = accountRepo.save(account);

    // bônus de abertura (ledger)
    Transaction bonus = new Transaction();
    bonus.setAccountId(account.getId());
    bonus.setType("CREDIT");
    bonus.setCategory("BONUS_ABERTURA");
    bonus.setAmount(new BigDecimal("100.00"));
    bonus.setDescription("Bônus de abertura de conta");
    txRepo.save(bonus);
    
    return account;
  }

  public LoginResponse validateAndGetAccount(String agency, String accountNumber) {
    Account account = accountRepo.findByAgencyAndAccountNumber(agency, accountNumber)
        .orElseThrow(() -> new NoSuchElementException("Conta inválida"));
    
    Customer customer = customerRepo.findById(account.getCustomerId())
        .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado"));
    
    return new LoginResponse(account.getId(), account.getAgency(), account.getAccountNumber(), customer.getName());
  }
}
