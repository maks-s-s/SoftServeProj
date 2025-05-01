package com.softserve.academy.controller;

import com.softserve.academy.model.Category;
import com.softserve.academy.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    public final CategoryRepository categoryRepo;
    @Autowired
    public CategoryController(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @PostMapping("/category/add")
    public void addCategory(@RequestBody Category category) {
        System.out.println(category);
        categoryRepo.save(category);
    }
}
