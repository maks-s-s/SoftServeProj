package com.softserve.academy.mappers;

import com.softserve.academy.dto.CategoryDTO;
import com.softserve.academy.model.Category;

public class CategoryMapper {
    public static Category toCategory(CategoryDTO categoryDTO){
        return Category.builder()
                .name(categoryDTO.getName()).build();
    }
}
