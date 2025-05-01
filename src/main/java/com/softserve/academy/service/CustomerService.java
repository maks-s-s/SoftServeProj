package com.softserve.academy.service;

import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Purchase;
import com.softserve.academy.repository.CustomerRepository;
import com.softserve.academy.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;



@Transactional
@Service
@AllArgsConstructor
public class CustomerService {
    public final CustomerRepository custRepo;
    public final PurchaseService purchaseService;
    

    @Transactional
    public void addPurchase(Long id, Purchase purchase) {
        purchase.setCustomer(custRepo.findById(id).get());
        Customer cust = custRepo.findById(id).get();
        purchaseService.createPurchase(purchase);
        cust.getPurchases().add(purchase);
    }
    @Transactional
    public void deleteCustomerPurchases(Long id, Long purchaseId) {
        Customer cust = custRepo.findById(id).get();
        cust.getPurchases().remove(purchaseId);
    }
}
