package com.softserve.academy.controller;

import com.softserve.academy.model.Category;
import com.softserve.academy.model.Customer;
import com.softserve.academy.repository.CustomerRepository;
import com.softserve.academy.service.CategoryService;
import com.softserve.academy.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/category")
@Controller
public class CategoryViewController {
    public final CategoryService catSvc;
    public final ProductService prodSvc;
    CustomerRepository customerRepository;

    @Autowired
    public CategoryViewController(CategoryService categoryService,
                                  ProductService productService, CustomerRepository customerRepository) {
        this.catSvc = categoryService;
        this.prodSvc = productService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/goToCategories")
    public String showCategories(HttpSession session,
                                 Model model,
                                 @RequestParam(name = "size", defaultValue = "5") int size,
                                 @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = catSvc.findAllCategories(pageable);
        System.out.println(categories);
        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("categories", categories);
        model.addAttribute("customer", customer);
        return "AllCategories"; // categories.html
    }
}