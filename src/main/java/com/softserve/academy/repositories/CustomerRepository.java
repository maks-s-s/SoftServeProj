package com.softserve.academy.repositories;

import com.softserve.academy.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c.userName FROM Customer c WHERE c.userName = :userName")
    String getCustomersByUserName(String userName);

    Customer findByUserName(String username);
}
