package com.softserve.academy.repository;

import com.softserve.academy.model.Product;
import com.softserve.academy.model.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findProductByCategory_Id(Long id, Pageable pageable);
    @Query("SELECT p FROM Product p JOIN p.stores s WHERE s.id = :storeId")
    Page<Product> findByStoreId(@Param("storeId") Long storeId, Pageable pageable);
    @Query("SELECT p FROM Product p ORDER BY p.price DESC")
    Page<Product> findAllOrderByPriceDesc( Pageable pageable);
    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    Page<Product> findAllOrderByPriceAsc( Pageable pageable);
    @Query("SELECT p FROM Product p ORDER BY p.price DESC")
    Page<Product> findByStoreIdOrderByPriceDesc(Long id, Pageable pageable);
    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    Page<Product> findByStoreIdOrderByPriceASC(Long id, Pageable pageable);
}
