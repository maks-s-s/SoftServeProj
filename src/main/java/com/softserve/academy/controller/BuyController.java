package com.softserve.academy.controller;

import com.softserve.academy.model.Category;
import com.softserve.academy.model.Product;
import com.softserve.academy.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BuyController {
    public final CategoryService categoryService;
    @Autowired
    public BuyController( CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/buy")
    public String buyPage() {
        System.out.println("buy psge");
        return "Buy";
    }
    @GetMapping("/goToCategories")
    public String showCategories(Model model) {
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "AllCategories"; // categories.html
    }
}