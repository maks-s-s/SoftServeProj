package com.softserve.academy.controller;

import com.softserve.academy.model.Purchase;
import com.softserve.academy.repository.PurchaseRepository;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@NoArgsConstructor
public class PurchaseController {
    PurchaseRepository purRepo;

    public PurchaseController(PurchaseRepository purRepo) {
        this.purRepo = purRepo;
    }
}
