package com.softserve.academy.controllers.rest;


import com.softserve.academy.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {
    public final CategoryService catSrv;

    @Autowired
    public CategoryRestController(CategoryService catSrv){
        this.catSrv=catSrv;
    }




}
