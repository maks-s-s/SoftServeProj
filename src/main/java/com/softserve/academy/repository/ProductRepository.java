package com.softserve.academy.repository;

import com.softserve.academy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductByCategory_Id(Long categoryId);
}
