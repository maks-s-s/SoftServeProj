package com.softserve.academy.controller;


import com.softserve.academy.model.Customer;
import com.softserve.academy.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    public final CustomerRepository custRepo;

    @Autowired
    public CustomerController(CustomerRepository custRepo) {
        this.custRepo = custRepo;
    }

    @PostMapping("/customer/add")
    public void addCustomer(@RequestBody Customer customer) {
        System.out.println(customer);
        custRepo.save(customer);
    }
}
