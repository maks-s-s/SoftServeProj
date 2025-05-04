package com.softserve.academy.controller;

import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Purchase;
import com.softserve.academy.repository.CustomerRepository;
import com.softserve.academy.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SignController {

    @Autowired
    private PurchaseRestController purchaseRestController;
    @Autowired
    private CustomerService custSvc;

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
    public String purchaseHistory(  Model model, HttpSession session,
                                    @RequestParam(name="byDate", defaultValue = "true") boolean sortByDate,
                                    @RequestParam(name="byPrice", defaultValue = "false") boolean sortByPrice,
                                    @RequestParam(name="otherCustomer", defaultValue = "null") String otherCustomer,
                                    @RequestParam(name="size", defaultValue = "4") int size,
                                    @RequestParam(name="page", defaultValue = "0") int page) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (!otherCustomer.equals("null")){customer = custSvc.findByEmail(otherCustomer);}
        Page<Purchase> purchases = purchaseRestController.getPurchasesByCustomerId(customer.getId(), size, page, sortByDate, sortByPrice);
        model.addAttribute("customer", customer);
        model.addAttribute("purchases", purchases);
        return "purchaseHistory";
    }
}