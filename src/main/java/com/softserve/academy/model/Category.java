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
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Category {
    @GeneratedValue
    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();

    @Override
    public String toString(){return id+" "+name;}
}
