package com.softserve.academy.controller;


import com.softserve.academy.dto.StoreDTO;
import com.softserve.academy.mappers.StoreMapper;
import com.softserve.academy.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
            return ResponseEntity.accepted().build();
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

    @PutMapping("/rename")
    public void rename (@RequestParam("id") Long id, @RequestParam("newName") String newName) {
        storeSvc.rename(id, newName);
    }
}
