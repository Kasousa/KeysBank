package com.backend.keysbankapi.ledger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  List<Transaction> findByAccountIdOrderByCreatedAtDesc(UUID accountId);

  @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId ORDER BY t.createdAt DESC")
  List<Transaction> findByAccountIdOrderByCreatedAtDesc(@Param("accountId") UUID accountId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

  @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId AND t.type = :type ORDER BY t.createdAt DESC")
  List<Transaction> findByAccountIdAndTypeOrderByCreatedAtDesc(@Param("accountId") UUID accountId, @Param("type") String type);

  @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId AND t.createdAt >= :startDate AND t.createdAt <= :endDate ORDER BY t.createdAt DESC")
  List<Transaction> findByAccountIdAndDateRangeOrderByCreatedAtDesc(@Param("accountId") UUID accountId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

  @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId AND t.type = :type AND t.createdAt >= :startDate AND t.createdAt <= :endDate ORDER BY t.createdAt DESC")
  List<Transaction> findByAccountIdAndTypeAndDateRangeOrderByCreatedAtDesc(@Param("accountId") UUID accountId, @Param("type") String type, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

  @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId AND t.type = 'BALANCE' AND t.createdAt >= :startDate AND t.createdAt < :endDate ORDER BY t.createdAt DESC")
  List<Transaction> findBalancesByAccountIdAndDate(@Param("accountId") UUID accountId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

}