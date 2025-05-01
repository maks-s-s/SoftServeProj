package com.softserve.academy.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;
import java.util.Set;
import java.util.HashSet;


@Entity
@Setter
@Getter
public class Product {
    @GeneratedValue
    @Id
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
        name = "product_store",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private Set<Store> stores = new HashSet<>();

}
