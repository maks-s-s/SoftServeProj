package com.softserve.academy.controller;


import com.softserve.academy.dto.StoreDTO;
import com.softserve.academy.mappers.StoreMapper;
import com.softserve.academy.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
public class StoreRestController {
    public final StoreService storeSvc;

    @Autowired
    public StoreRestController(StoreService storeSvc){
        this.storeSvc=storeSvc;
    }


    @PostMapping("")
    public ResponseEntity<Void> addStore(@RequestBody StoreDTO storeDTO){
        if (storeDTO.validate()){
            storeSvc.addStore(StoreMapper.toStore(storeDTO));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{id}/products/{productId}")
    public ResponseEntity<Void> addProductToStore(@PathVariable("id") Long storeId,
                                                  @PathVariable("productId") Long productId){
        if (storeSvc.addProductToStore(storeId, productId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/byProduct/{prod-id}")
    public ResponseEntity<Page<StoreDTO>> getStoresByProduct(@PathVariable("prod-id") Long prodId,
                                                             @RequestParam(name = "size", defaultValue = "3") int size,
                                                             @RequestParam(name = "page", defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(0, 3);
        Page<StoreDTO> stores = storeSvc.getStoresByProduct(prodId, pageable);
        if (stores==null){return ResponseEntity.notFound().build();}
        return ResponseEntity.ok(stores);
    }
    @PatchMapping("/updateStore/{id}")
    public ResponseEntity<Void> updateStore(@PathVariable("id") Long storeId , @Valid @RequestBody StoreDTO store) {
        if(storeSvc.changeStoreById(storeId, store)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/deleteStore/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable("id") Long storeId){
        if(storeSvc.deleteStoreById(storeId)){
            return ResponseEntity.ok().build();
        };
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("{store-id}/{prod-id}")
    public ResponseEntity<Void> deleteProductFromStore(@PathVariable("store-id") Long storeId, @PathVariable("prod-id") Long prodId){
        if(storeSvc.deleteProductFromStoreById(storeId,prodId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();

    }

}
