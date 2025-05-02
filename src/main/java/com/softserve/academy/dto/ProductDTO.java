package com.softserve.academy.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    public Long id;

    public String name;

    public BigDecimal price;

    public Long categoryId;

    public boolean validate(){
        return name!=null && price!=null && categoryId!=null;
    }
}
