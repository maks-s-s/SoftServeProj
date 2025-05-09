package com.softserve.academy.controller;


import com.softserve.academy.model.Customer;
import com.softserve.academy.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeViewController {
    CustomerRepository customerRepository;
    @Autowired
    public HomeViewController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @GetMapping("/buy")
    public String buyPage(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", customer);
        return "Buy";
    }
}
