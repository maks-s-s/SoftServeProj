package com.softserve.academy.repositories;

import com.softserve.academy.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findProductByCategory_Id(Long categoryId, Pageable pageable);
}
