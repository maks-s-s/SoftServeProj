package com.softserve.academy.dto;

import com.softserve.academy.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    public Long id;
    public String name;
    public List<Product> products;
}
