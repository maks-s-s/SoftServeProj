package com.softserve.academy.controller;


import com.softserve.academy.model.Category;
import com.softserve.academy.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {
    public final CategoryRepository catRepo;

    @Autowired
    public CategoryController(CategoryRepository catRepo) {
        this.catRepo = catRepo;
    }

    @PostMapping()
    public void addCategory(@RequestBody Category category){
        System.out.println(category);
        catRepo.save(category);
    }
}

