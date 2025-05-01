package com.softserve.academy.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a category of products.
 * Each category can have multiple products associated with it.
 */
@Entity
@Setter
@Getter
public class Category {
    @GeneratedValue
    @Id
    @Column(unique = true)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();

    public Category() {}
    public Category(Long id,String name) {
        this.name = name;
    }

}

