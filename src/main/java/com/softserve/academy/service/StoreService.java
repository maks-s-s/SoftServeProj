package com.softserve.academy.service;

import com.softserve.academy.model.Store;
import com.softserve.academy.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }
    public Page<Store> getAllStores(Pageable pageable) {
        return storeRepository.findAll(pageable);
    }
}
