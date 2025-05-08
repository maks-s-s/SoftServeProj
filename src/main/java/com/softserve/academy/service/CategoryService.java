package com.softserve.academy.service;

import com.softserve.academy.model.Category;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Product;
import com.softserve.academy.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public Page<Category> findAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);

    }
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }
}
