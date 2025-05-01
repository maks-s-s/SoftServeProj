package com.softserve.academy.service;

import com.softserve.academy.model.Category;
import com.softserve.academy.model.Customer;
import com.softserve.academy.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

}
