package com.softserve.academy.controller;

import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Purchase;
import com.softserve.academy.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignController {

    @Autowired
    private PurchaseRestController purchaseRestController;

    @GetMapping("/")
    public String signInHome() {
        return "signIn";
    }

    @GetMapping("/SignUp")
    public String signUp() {
        return "signUp";
    }

    @GetMapping("/home")
    public String goHome(Model model, HttpSession session) {
        model.addAttribute("customer", session.getAttribute("customer"));
        return "home";
    }

    @GetMapping("/purchaseHistory")
    public String purchaseHistory(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer != null) {
            Page<Purchase> purchases = purchaseRestController.getPurchasesByCustomerId(customer.getId(), 4, 0);

            model.addAttribute("customer", customer);
            model.addAttribute("purchases", purchases);
        }
        return "purchaseHistory";
    }
}