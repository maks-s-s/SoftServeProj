package com.softserve.academy.controller;

import com.softserve.academy.model.Category;
import com.softserve.academy.repository.CategoryRepository;
import com.softserve.academy.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/categories")
public class CategoryController {
    public final CategoryService categoryService;
    @Autowired
    public CategoryController( CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories/add")
    public void addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
    }

    @GetMapping("/categories/findAll")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());

    }
}
