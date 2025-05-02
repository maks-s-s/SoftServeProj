package com.softserve.academy.controller;

import com.softserve.academy.model.Category;
import com.softserve.academy.model.Product;
import com.softserve.academy.service.CategoryService;
import com.softserve.academy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@RequestMapping("/category")
@Controller
public class CategoryViewController {
    public final CategoryService catSvc;
    public final ProductService prodSvc;
    @Autowired
    public CategoryViewController(CategoryService categoryService,
                                  ProductService productService) {
        this.catSvc = categoryService;
        this.prodSvc = productService;
    }

    @GetMapping("/buy")
    public String buyPage() {
        System.out.println("buy psge");
        return "Buy";
    }
    @GetMapping("/goToCategories")
    public String showCategories(
            Model model,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Category> categories = catSvc.findAllCategories(pageable);
        System.out.println(categories);
        model.addAttribute("categories", categories);
        return "AllCategories"; // categories.html
    }

    @GetMapping("/{cat-id}")
    public String showProdsByCategory(@PathVariable("cat-id") Long id,
                                      Model model){
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> prods = prodSvc.getProductsByCategory(id, pageable);
        model.addAttribute("prods", prods);
        model.addAttribute("catName", catSvc.getCategoryById(id).getName());
        return "ProdsByCategory";
    }
}