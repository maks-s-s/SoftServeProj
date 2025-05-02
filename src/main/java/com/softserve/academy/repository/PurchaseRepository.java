package com.softserve.academy.repository;

import com.softserve.academy.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    // Find all purchases by a specific customer
    List<Purchase> findByCustomer(Customer customer);

    // Find all purchases by a specific product
    List<Purchase> findByProduct(Product product);

    // Find all purchases by a specific customer, ordered by purchase date in descending order
    Page<Purchase> findByCustomerOrderByPurchaseDateDesc(Customer customer, Pageable pageable);

    // Find all purchases by a specific product, ordered by purchase date in descending order
    List<Purchase> findByCustomerAndProduct(Customer customer, Product product);

    Page<Purchase> findByCustomerIdOrderByPurchaseDateDesc(Long customerId, Pageable pageable);
}