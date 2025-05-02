package com.softserve.academy.repositories;

import com.softserve.academy.models.*;
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
    List<Purchase> findByCustomerOrderByPurchaseDateDesc(Customer customer);

    // Find all purchases by a specific product, ordered by purchase date in descending order
    List<Purchase> findByCustomerAndProduct(Customer customer, Product product);
}