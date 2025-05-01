package com.softserve.academy.controller;

import com.softserve.academy.model.Product;
import com.softserve.academy.repository.ProductRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    public final ProductRepository prodRepo;

    @Autowired
    public ProductController(ProductRepository prodRepo) {
        this.prodRepo = prodRepo;
    }



    @PostMapping()
    public Product createProduct(@RequestBody Product product) {
        return prodRepo.save(product);
    }
}
