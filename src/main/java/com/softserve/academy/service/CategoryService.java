package com.softserve.academy.service;

import aj.org.objectweb.asm.ConstantDynamic;
import com.softserve.academy.model.Category;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Product;
import com.softserve.academy.repository.CategoryRepository;
import com.softserve.academy.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService prodSvc;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository,
                           ProductService prodSvc) {
        this.categoryRepository = categoryRepository;
        this.prodSvc = prodSvc;
    }
    public Page<Category> findAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    public List<Category> findAllCategoriesListType() {
        return categoryRepository.findAllByOrderByNameAsc();
    }
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public boolean deleteCategory(Long id) {
        if (categoryRepository.findById(id).isEmpty()) return false;
        Category category = categoryRepository.findById(id).get();
        category.getProducts().forEach(product -> product.setCategory(null));
        categoryRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean addProdToCategory(Long prodId, Long catId){
        Product product = prodSvc.getProductById(prodId);
        Category category = categoryRepository.findById(catId).orElse(null);
        if (product==null||category==null){return false;}
        product.getCategory().getProducts().remove(product);
        category.getProducts().add(product);
        product.setCategory(category);
        return true;
    }
}
