package com.softserve.academy.controllers.rest;


import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.models.Product;
import com.softserve.academy.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.softserve.academy.mappers.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    public final ProductService prodSrv;

    @Autowired
    public ProductRestController(ProductService prodSrv) {
        this.prodSrv = prodSrv;
    }


    @PostMapping()
    public ResponseEntity<Void> addProduct(@RequestBody ProductDTO product){
        if (product.validate()){
            if(prodSrv.addProduct(
                    Product.builder()
                            .name(product.getName())
                            .price(product.getPrice())
                            .build(), product.categoryId)){
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO product){
        prodSrv.updateProduct(product, prodSrv.getProductById(id));
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id){
        if(prodSrv.deleteProduct(id)) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("id") Long id){
        ProductDTO prodDetails = prodSrv.getProductDTO(id);
        if (prodDetails==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(prodDetails);
    }


    @GetMapping("/category/{category-id}")
    public Page<ProductDTO> getProductsByCategory( @PathVariable("category-id") Long catId,
                                                @RequestParam(name="size", defaultValue = "3") int size,
                                                @RequestParam(name="page", defaultValue = "0") int page,
                                                @RequestParam(name="mostExpensiveFirst", defaultValue="true") boolean mostExpensiveFirst){
        Pageable pageable;
        if (mostExpensiveFirst){pageable = PageRequest.of(page, size, Sort.by("price").descending());}
        else {pageable = PageRequest.of(page, size, Sort.by("price").ascending());}
        return prodSrv.getProductsByCategory(catId, pageable).map(
                product -> ProductMapper.toProductDTO(product));
    }
}
