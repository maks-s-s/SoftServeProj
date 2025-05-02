package com.softserve.academy.controller;


import com.softserve.academy.model.Customer;
import com.softserve.academy.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CustomerController {
    public final CustomerRepository custRepo;

    @Autowired
    public CustomerController(CustomerRepository custRepo) {
        this.custRepo = custRepo;
    }

    @PostMapping("/customer/add")
    public String addCustomer(@RequestParam("name") String name, @RequestParam("email") String email,
                              @RequestParam("password") String password, @RequestParam("phoneNumber") String phoneNumber, Model model) {
        Customer customer = new Customer(name, email, phoneNumber, password);
        System.out.println(customer);
        custRepo.save(customer);

        model.addAttribute("customer", customer);

        return "redirect:/";
    }

    @PostMapping("/customer/verify")
    public String verify (@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model) {
        Customer customer = custRepo.findByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            session.setAttribute("name", customer.getName());
            return "redirect:/home";
        }
        else {
            model.addAttribute("error", "Wrong password");
            return "signIn";
        }
    }

    @GetMapping("/customer/logout")
    public String logout (HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/customer/get/{id}")
    public Customer getCustomer(@PathVariable("id") Long id) {
        return custRepo.findById(id).get();
    }

    @GetMapping("/customer/getAll")
    public List<Customer> getAllCustomer() {return custRepo.findAll();}
}
