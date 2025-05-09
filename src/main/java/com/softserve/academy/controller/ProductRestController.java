package com.softserve.academy.controller;


import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.model.Product;
import com.softserve.academy.service.ProductService;
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
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(  @PathVariable("category-id") Long catId,
                                                    @RequestParam(name="size", defaultValue = "3") int size,
                                                    @RequestParam(name="page", defaultValue = "0") int page,
                                                    @RequestParam(name="mostExpensiveFirst", defaultValue="true") boolean mostExpensiveFirst){
        Pageable pageable;
        if (mostExpensiveFirst){pageable = PageRequest.of(page, size, Sort.by("price").descending());}
        else {pageable = PageRequest.of(page, size, Sort.by("price").ascending());}
        Page<Product> products = prodSrv.getProductsByCategory(catId, pageable);
        if (products==null){return ResponseEntity.notFound().build();}
        return ResponseEntity.ok(products.map(ProductMapper::toProductDTO));
    }

    @PutMapping("/setDiscount/{prodId}")
    public void setDiscount(@PathVariable("prodId") Long id, @RequestParam double discount) {
        prodSrv.setDiscountById(id, discount);
    }

    @PutMapping("/setDescription/{prodId}")
    public void setDescription(@PathVariable("prodId") Long id, @RequestParam String description) {
        prodSrv.setDescriptionById(id, description);
    }

        @PutMapping("/updateNullDiscounts")
    public ResponseEntity<String> updateAllNullDiscounts() {
        prodSrv.updateNullDiscounts();
        return ResponseEntity.ok("All products with null discount have been updated to 0.");
    }

    @PutMapping("/updateNullDescriptions")
    public ResponseEntity<String> updateAllNullDescriptions() {
        prodSrv.updateNullDescriptions();
        return ResponseEntity.ok("All products with null description have been updated to an empty string.");
    }
}
