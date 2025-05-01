package com.softserve.academy.controller;

import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.model.Product;
import com.softserve.academy.repository.ProductRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
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
    public Page<ProductDTO> getProductsByCategory(
            @PathVariable("category-id") Long catId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
        ){
        Pageable pageable = (Pageable) PageRequest.of(page,size);
        Page<Product> prods = prodRepo.findProductByCategory_Id(catId, pageable);
        return prods.map(
                product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setCategoryName(product.getCategory().getName());
                    return dto;
                }
        );
    }

    @PostMapping()
    public Product createProduct(@RequestBody Product product) {
        return prodRepo.save(product);
    }
}
