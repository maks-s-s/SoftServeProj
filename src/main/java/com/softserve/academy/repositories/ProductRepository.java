package com.softserve.academy.repositories;

import com.softserve.academy.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findProductByCategory_Id(Long categoryId, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
}
