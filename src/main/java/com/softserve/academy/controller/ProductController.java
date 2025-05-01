package com.softserve.academy.controller;

import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.model.Product;
import com.softserve.academy.repository.ProductRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {
    public final ProductRepository prodRepo;

    @Autowired
    public ProductController(ProductRepository prodRepo) {
        this.prodRepo = prodRepo;
    }

    @GetMapping("/{category-id}")
    public List<ProductDTO> getProductsByCategory(@PathVariable("category-id") Long catId){
        List<Product> prods = prodRepo.findProductByCategory_Id(catId);
        return prods.stream().map(
                product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setCategoryName(product.getCategory().getName());
                    return dto;
                }
        ).collect(Collectors.toList());
    }

    @PostMapping()
    public Product createProduct(@RequestBody Product product) {
        return prodRepo.save(product);
    }
}
