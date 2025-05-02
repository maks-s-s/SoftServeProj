package com.softserve.academy.services;

import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.models.Category;
import com.softserve.academy.models.Product;
import com.softserve.academy.repositories.CategoryRepository;
import com.softserve.academy.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    public final ProductRepository prodRepo;
    public final CategoryRepository catRepo;

    public ProductService(ProductRepository prodRepo, CategoryRepository catRepo) {
        this.prodRepo = prodRepo;
        this.catRepo = catRepo;
    }


    public boolean addProduct(Product product, Long categoryId){
        if (catRepo.findById(categoryId).isEmpty()) {
            return false;
        }
        try {
            product.setCategory(catRepo.findById(categoryId).get());
            prodRepo.save(product);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    @Transactional
    public void updateProduct(ProductDTO prodDetails, Product product){
        if (prodDetails.categoryId==null) {prodDetails.categoryId=product.getCategory().getId();}
        product.setName(prodDetails.getName()!=null? prodDetails.getName() : product.getName());
        product.setPrice(prodDetails.getPrice()!=null? prodDetails.getPrice():product.getPrice());
        product.setCategory(product.getCategory());
    }

    public Product getProductById(Long id){
        return prodRepo.findById(id).orElse(null);
    }

    public boolean deleteProduct(Long id){
        if (prodRepo.findById(id).isEmpty()) return false;
        prodRepo.deleteById(id);
        return true;
    }

    public ProductDTO getProductDetails(Long id){
        Product product = prodRepo.findById(id).orElse(null);
        if (product==null) return null;
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .build();
    }


    public Page<Product> getProductsByCategory(Long id, Pageable pageable){
        if (catRepo.findById(id).isEmpty()) return null;
        return prodRepo.findProductByCategory_Id(id, pageable);
    }

}
