package com.softserve.academy.service;


import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.dto.StoreDTO;
import com.softserve.academy.mappers.StoreMapper;
import com.softserve.academy.model.*;
import com.softserve.academy.repository.ProductRepository;
import com.softserve.academy.repository.StoreRepository;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.softserve.academy.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoreService {
    public final StoreRepository storeRepo;
    public final ProductRepository prodRepo;

    @Autowired
    public StoreService(StoreRepository storeRepo, ProductRepository prodRepo) {
        this.storeRepo = storeRepo;
        this.prodRepo = prodRepo;
    }

    public void addStore(Store store){
        storeRepo.save(store);
    }

    public Store getStoreById(Long id){
        return storeRepo.findById(id).orElse(null);
    }

    @Transactional
    public boolean addProductToStore(Long storeId, Long productId){
        Store store = storeRepo.findById(storeId).orElse(null);
        Product prod = prodRepo.findById(productId).orElse(null);
        if (store==null||prod==null){return false;}
        store.getProducts().add(prod);
        prod.getStores().add(store);
        return true;
    }

    public Page<StoreDTO> getStoresByProduct(Long prodId, Pageable pageable){
        Product product = prodRepo.findById(prodId).orElse(null);
        Set<Store> stores = product.getStores();
        if (product==null||stores==null){return null;}
        List<Store> storeList = new ArrayList<>(stores);
        List<StoreDTO> storeDTOList = storeList.stream().map( store -> { return StoreMapper.toStoreDTO(store);}).toList();
        int total = stores.size();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<StoreDTO> pageContent;
        if (start > end) {
            pageContent = Collections.emptyList();
        } else {
            pageContent = storeDTOList.subList(start, end);
        }
        return new PageImpl<>(pageContent, pageable, total);
    }

    public Page<Store> getAllStores(Pageable pageable) {
        return storeRepo.findAll(pageable);
    }

    public List<Store> getAllStoresListType() {
        return storeRepo.findAll();
    }

    public Page<Product> getProductsByStore(Long id, Pageable pageable,boolean sortUp,boolean sortDown) {
        if (sortUp) {
            return prodRepo.findByStoreIdOrderByPriceASC(id,pageable);
        } else if (sortDown) {
            return prodRepo.findByStoreIdOrderByPriceDesc(id,pageable);
        }
        return prodRepo.findByStoreId(id,pageable);
    }

    public boolean deleteStoreById(Long storeId){
        Store store = storeRepo.findById(storeId).orElse(null);
        if (store==null){return false;}
        Set<Product> products = store.getProducts();
        products.stream().forEach(product -> product.getStores().remove(store));
        storeRepo.deleteById(storeId);
        return true;
    }

    public void rename (Long id, String newName) {
        Store store = storeRepo.findById(id).orElse(null);
        if (store != null) {
            store.setName(newName);
            storeRepo.save(store);
        }
    }

    @Transactional
    public boolean updateStore(Long id, StoreDTO store){
        Store storeToUpdate = storeRepo.findById(id).orElse(null);
        if (storeToUpdate==null) return false;
        System.out.println(store);
        storeToUpdate.setName(!store.getName().isEmpty()?store.getName():storeToUpdate.getName());
        storeToUpdate.setEmail(!store.getEmail().isEmpty()?store.getEmail():storeToUpdate.getEmail());
        storeToUpdate.setLocation(!store.getLocation().isEmpty()?store.getLocation():storeToUpdate.getLocation());
        storeToUpdate.setContactNumber(!store.getContactNumber().isEmpty()?store.getContactNumber():storeToUpdate.getContactNumber());
        return true;
    }
}
