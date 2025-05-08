package com.softserve.academy.service;


import com.softserve.academy.dto.PurchaseDTO;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Purchase;
import com.softserve.academy.repository.ProductRepository;
import com.softserve.academy.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PurchaseService {

    @Getter
    public final PurchaseRepository purRepo;
    public final CustomerService custSrv;
    public final ProductRepository prodRepo;
    public final ProductService prodSrv;


    @Autowired
    public PurchaseService( PurchaseRepository purRepo,
                            CustomerService custSrv,
                            ProductRepository prodRepo,
                            ProductService prodSrv) {
        this.purRepo = purRepo;
        this.custSrv=custSrv;
        this.prodRepo=prodRepo;
        this.prodSrv=prodSrv;
    }

    @Transactional
    public boolean addPurchase(PurchaseDTO purchase){
        if (!purchase.validate()){return false;}
        Customer cust = custSrv.getCustomerById(purchase.customerId);
        if (cust==null){return false;}
        if (prodRepo.findById(purchase.productId).orElse(null)==null){return false;}
        Purchase newPurchase = Purchase.builder(). quantity(purchase.getQuantity()).
                            customer(custSrv.getCustomerById(purchase.getCustomerId())).
                            purchaseDate(LocalDateTime.now()).
                            product(prodSrv.getProductById(purchase.getProductId())).build();
        newPurchase.setTotalPrice();
        purRepo.save(newPurchase);
        cust.addPurchase(newPurchase);
        return true;
    }
}
