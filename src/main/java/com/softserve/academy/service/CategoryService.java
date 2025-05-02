package com.softserve.academy.service;

import com.softserve.academy.model.Category;
import com.softserve.academy.repository.CategoryRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository catRepo;
    @Autowired
    public CategoryService(CategoryRepository catRepo) {
        this.catRepo = catRepo;
    }

    public Page<Category> findAllCategories(Pageable pageable) {
        return catRepo.findAll(pageable);
    }
    public void addCategory(Category category) {
        catRepo.save(category);
    }

}
