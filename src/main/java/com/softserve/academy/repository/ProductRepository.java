package com.softserve.academy.repository;

import com.softserve.academy.model.Category;
import com.softserve.academy.model.Product;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> getProductsByCategory(Category category, Pageable pageable);
}

/*
Отримати список продуктів певної категорії +

Додати фільтрацію та сортування: Список продуктів за ціною (від дешевих до дорогих і навпаки).
Список покупок покупця за датою або загальною сумою.
 */