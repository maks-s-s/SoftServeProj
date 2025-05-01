package com.softserve.academy.controller;


import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Purchase;
import com.softserve.academy.repository.CustomerRepository;
import com.softserve.academy.repository.ProductRepository;
import com.softserve.academy.repository.PurchaseRepository;
import com.softserve.academy.service.CustomerService;
import com.softserve.academy.service.ProductService;
import com.softserve.academy.service.PurchaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {
    public final CustomerRepository custRepo;
    public final PurchaseRepository purchaseRepo;
    public final ProductRepository prodRepo;
    public final CustomerService customerService;

    @Autowired
    public CustomerController(
            CustomerRepository custRepo,
            PurchaseRepository purchaseRepo,
            ProductRepository prodRepo
    ) {
        this.custRepo = custRepo;
        this.purchaseRepo=purchaseRepo;
        this.prodRepo=prodRepo;
        this.customerService=new CustomerService(custRepo, new PurchaseService(purchaseRepo));
    }

    @PostMapping("/customer")
    public void addCustomer(@RequestBody Customer customer) {
        System.out.println(customer);
        custRepo.save(customer);
    }

    @PatchMapping("/customer/{id}")
    @Transactional
    public void updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer) {
        Customer cust = custRepo.findById(id).get();
        cust.setName(customer.getName()!=null?customer.getName():cust.getName());
        cust.setEmail(customer.getEmail()!=null?customer.getEmail():cust.getEmail());
        cust.setPhoneNumber(customer.getPhoneNumber()!=null?customer.getPhoneNumber():cust.getPhoneNumber());
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomer(@PathVariable("id") Long id) {
        return custRepo.findById(id).orElse(null);
    }

    @DeleteMapping("/customer/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        custRepo.delete(custRepo.findById(id).get());
    }

    @GetMapping("/customer/{id}/purchases")
    public List<Purchase> getCustomerPurchases(@PathVariable("id") Long id) {
        return custRepo.findById(id).get().getPurchases();
    }

    @PostMapping("/customer/{id}/purchases/{product-id}")
    public void addPurchase(@PathVariable("id") Long id, @PathVariable("product-id") Long prodId, @RequestBody Purchase purchase) {
        purchase.setProduct(prodRepo.getById(prodId));
        customerService.addPurchase(id, purchase);
    }

    @DeleteMapping("/customer/{id}/purchases/{purchase-id}")
    public void deleteCustomerPurchases(@PathVariable("id") Long id, @PathVariable("purchase-id") Long purchaseId) {
        customerService.deleteCustomerPurchases(id, purchaseId);
    }


}
