package com.softserve.academy.controller;


import com.softserve.academy.model.Product;
import com.softserve.academy.service.CategoryService;
import com.softserve.academy.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductViewController {
    public final ProductService prodSvc;
    public final CategoryService catSvc;

    public ProductViewController(ProductService productService,
                                 CategoryService categoryService) {
        this.prodSvc = productService;
        this.catSvc = categoryService;
    }

    @GetMapping("/byCategory/{cat-id}")
    public String showProdsByCategory(@PathVariable("cat-id") Long id,
                                      Model model){
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> prods = prodSvc.getProductsByCategory(id, pageable);
        model.addAttribute("prods", prods);
        model.addAttribute("catName", catSvc.getCategoryById(id).getName());
        return "ProdsByCategory";
    }
}
