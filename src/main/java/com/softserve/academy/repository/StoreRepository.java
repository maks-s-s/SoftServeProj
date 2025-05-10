package com.softserve.academy.repository;

import com.softserve.academy.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    // Find a store by its name
    Store findByName(String name);

    // Find all stores by name containing a specific string, ignoring case
    List<Store> findByNameContainingIgnoreCase(String name);

    // Find all stores by location
    List<Store> findByLocation(String location);

    // Find all stores that contain a specific product
    Page<Store> findByProductsContaining(Product product, Pageable pageable);

}