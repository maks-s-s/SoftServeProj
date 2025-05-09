package com.softserve.academy.service;

import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.model.Product;
import com.softserve.academy.repository.CategoryRepository;
import com.softserve.academy.repository.ProductRepository;
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

    @Transactional
    public boolean addProduct(Product product, Long categoryId){
        if (catRepo.findById(categoryId).isEmpty()) {
            return false;
        }
        try {
            product.setCategory(catRepo.findById(categoryId).get());
            catRepo.findById(categoryId).get().getProducts().add(product);
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

    public ProductDTO getProductDTO(Long id){
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
        if (id==0L){return prodRepo.findAll(pageable);}
        if (catRepo.findById(id).isEmpty()) {return null;}
        return prodRepo.findProductByCategory_Id(id, pageable);
    }

    public void setDiscountById(Long id, double discount) {
        if (discount < 0 || discount > 1) {
            throw new IllegalArgumentException("Discount must be between 0 and 1.");
        }

        Product product = prodRepo.findById(id).orElse(null);

        if (product != null) {
            product.setDiscount(discount);
            prodRepo.save(product);
        }
    }

    public void setDescriptionById(Long id, String description) {
        if (description.length() > 200) {
            throw new IllegalArgumentException("Description cannot exceed 200 characters.");
        }

        Product product = prodRepo.findById(id).orElse(null);

        if (product != null) {
            product.setDescription(description);
            prodRepo.save(product);
        }
    }

    public void updateNullDiscounts() {
        List<Product> products = prodRepo.findAll();
        for (Product product : products) {
            if (product.getDiscount() == null) {
                product.setDiscount(0.0);
                prodRepo.save(product);
            }
        }
    }

    public void updateNullDescriptions() {
        List<Product> products = prodRepo.findAll();
        for (Product product : products) {
            if (product.getDescription() == null) {
                product.setDescription("");
                prodRepo.save(product);
            }
        }
    }

}
