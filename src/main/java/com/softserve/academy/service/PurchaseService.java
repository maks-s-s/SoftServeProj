package com.softserve.academy.service;

import com.softserve.academy.model.Purchase;
import com.softserve.academy.repository.PurchaseRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PurchaseService {
    public final PurchaseRepository purRepo;

    @Autowired
    public PurchaseService(PurchaseRepository purRepo) {
        this.purRepo = purRepo;
    }

    public void createPurchase(Purchase purchase) {
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setTotalPrice();
        purRepo.save(purchase);
    }
}
