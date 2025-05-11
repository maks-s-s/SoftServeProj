package com.softserve.academy.controller;


import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Product;
import com.softserve.academy.repository.CustomerRepository;
import com.softserve.academy.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.unbescape.html.HtmlEscape;

import java.util.List;

@Controller
public class HomeViewController {
    CustomerRepository customerRepository;
    ProductRepository productRepository;
    @Autowired
    public HomeViewController(CustomerRepository customerRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }
    @GetMapping("/buy")
    public String buyPage(Model model, HttpSession session) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}
        model.addAttribute("customer", session.getAttribute("customer"));
        return "Buy";
    }
    @GetMapping("/goToSearchStore")
    public String SearchStorePage(Model model, HttpSession session) {
        if (session.getAttribute("customer") == null) {return "redirect:/";}
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("customer", session.getAttribute("customer"));
        return "SearchStore";
    }
}
