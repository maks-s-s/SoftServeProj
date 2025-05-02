package com.softserve.academy.controllers.rest;


import com.softserve.academy.dto.PurchaseDTO;
import com.softserve.academy.services.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseRestController {

    public final PurchaseService purSrv;

    public PurchaseRestController(PurchaseService purSrv){
        this.purSrv=purSrv;
    }

    @PostMapping()
    public ResponseEntity<Void> addPurchase(@RequestBody PurchaseDTO purchase){
        if (purSrv.addPurchase(purchase)){return ResponseEntity.ok().build();}
        return ResponseEntity.badRequest().build();

    }
}
