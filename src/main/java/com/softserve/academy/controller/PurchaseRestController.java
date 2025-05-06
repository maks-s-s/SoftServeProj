package com.softserve.academy.controller;


import com.softserve.academy.dto.PurchaseDTO;
import com.softserve.academy.model.Purchase;
import com.softserve.academy.repository.PurchaseRepository;
import com.softserve.academy.service.PurchaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseRestController {

    public final PurchaseService purSrv;
    public final PurchaseRepository purRepo;

    public PurchaseRestController(PurchaseService purSrv){
        this.purSrv=purSrv;
        purRepo=purSrv.getPurRepo();
    }

    @PostMapping()
    public ResponseEntity<Void> addPurchase(@RequestBody PurchaseDTO purchase){
        if (purSrv.addPurchase(purchase)){return ResponseEntity.ok().build();}
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{customer-id}")
    public Page<Purchase> getPurchasesByCustomerId(@PathVariable("customer-id") Long custId,
                                                   @RequestParam(name="size", defaultValue = "4") int size,
                                                   @RequestParam(name="page", defaultValue = "0") int page,
                                                   @RequestParam(name="byDate", defaultValue = "true") boolean sortByDate,
                                                   @RequestParam(name="byPrice", defaultValue = "false") boolean sortByPrice) {
        Pageable pageable = PageRequest.of(page, size);
        if (sortByDate) {
            return purRepo.findByCustomerIdOrderByPurchaseDateDesc(custId, pageable);
        } else if (sortByPrice) {
            return purRepo.findByCustomerIdOrderByTotalPriceDesc(custId, pageable);
        }
        return purRepo.findAllByCustomerId(custId, pageable);
    }

    @GetMapping("/getAll")
    public List<Purchase> getAll() {
        return purRepo.findAll();
    }
}
