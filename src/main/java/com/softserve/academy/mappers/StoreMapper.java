package com.softserve.academy.mappers;

import com.softserve.academy.dto.StoreDTO;
import com.softserve.academy.model.Store;

public class StoreMapper {
    public static StoreDTO toStoreDTO (Store store){
        return StoreDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .location(store.getLocation())
                .email(store.getEmail())
                .contactNumber(store.getContactNumber())
                .build();
    }
}

