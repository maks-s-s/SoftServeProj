package com.softserve.academy.repositories;


import com.softserve.academy.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
