package com.softserve.academy.service;

import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.dto.StoreDTO;
import com.softserve.academy.mappers.StoreMapper;
import com.softserve.academy.model.Product;
import com.softserve.academy.model.Store;
import com.softserve.academy.repository.ProductRepository;
import com.softserve.academy.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.softserve.academy.mappers.StoreMapper.toStoreDTO;

@Service
public class StoreService {
    public final StoreRepository storeRepo;
    public final ProductRepository prodRepo;

    @Autowired
    public StoreService(StoreRepository storeRepo, ProductRepository prodRepo) {
        this.storeRepo = storeRepo;
        this.prodRepo = prodRepo;
    }

    public Page<StoreDTO> getAllStores(Pageable pageable) {
        return storeRepo.findAll(pageable).map(store -> toStoreDTO(store));
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

    @Transactional
    public boolean changeStoreById(Long storeId,StoreDTO storeDTO){
        Store store = storeRepo.findById(storeId).orElse(null);
        if (store!=null){
            store.setName(storeDTO.getName()!=null?storeDTO.getName():store.getName());
            store.setEmail(storeDTO.getEmail()!=null?storeDTO.getEmail():store.getEmail());
            store.setLocation(storeDTO.getLocation()!=null?storeDTO.getLocation():store.getLocation());
            store.setContactNumber(storeDTO.getContactNumber()!=null?storeDTO.getContactNumber():store.getContactNumber());
            return true;

        }
        return false;

    }

    public boolean deleteStoreById(Long storeId){
        Store store = storeRepo.findById(storeId).orElse(null);
        if (store==null){
            return false;
        }

        Set<Product> productsSET = store.getProducts();
        Product tempProduct;
        for (Product prodset: productsSET) {
            tempProduct = prodset;
            Set<Store> storesSET = tempProduct.getStores();
            storesSET.remove(store);

        }
        storeRepo.delete(store);
        return true;
    }
    @Transactional
    public boolean deleteProductFromStoreById( Long storeId , Long productId){
        Store store = storeRepo.findById(storeId).orElse(null);
        Product product = prodRepo.findById(productId).orElse(null);
        if (store==null||product==null){return false;}
        Set<Store> storesSET = product.getStores();
        Set<Product> productsSET = store.getProducts();
        if(!productsSET.contains(product)){return false;}
        storesSET.remove(store);
        productsSET.remove(product);
        return true;
    }
    public Page<Product> getProductsByStore(Long id, Pageable pageable,boolean sortUp,boolean sortDown) {
        if (sortUp) {
            return prodRepo.findByStoreIdOrderByPriceASC(id,pageable);
        } else if (sortDown) {
            return prodRepo.findByStoreIdOrderByPriceDesc(id,pageable);
        }
        return prodRepo.findByStoreId(id,pageable);
    }

}
