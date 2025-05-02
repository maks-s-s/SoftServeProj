package com.softserve.academy.mappers;

import com.softserve.academy.dto.ProductDTO;
import com.softserve.academy.model.Product;

public class ProductMapper {
    public static ProductDTO toProductDTO(Product product){
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .build();
    }
}
