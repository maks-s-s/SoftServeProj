package com.softserve.academy.controller;


import com.softserve.academy.model.Category;
import com.softserve.academy.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {
    public final CategoryService catSrv;
    @Autowired
    public CategoryRestController( CategoryService catSrv) {
        this.catSrv = catSrv;
    }

    @PostMapping("")
    public void addCategory(@RequestBody Category category) {
        catSrv.addCategory(category);
    }

    @GetMapping("")
    public ResponseEntity<Page<Category>> getAllCategories(@RequestParam(name = "size", defaultValue = "3") int size,
                                                           @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(catSrv.findAllCategories(pageable));
    }


}
