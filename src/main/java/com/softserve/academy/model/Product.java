package com.softserve.academy.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Set;
import java.util.HashSet;


@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @GeneratedValue
    @Id
    private Long id;

    private String name;

    private BigDecimal price;

    @NotNull
    @Min(0)
    @Max(1)
    @Column(nullable = false)
    private Double discount = 0.0;

    @NotNull
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    @ManyToMany
    @JoinTable(
        name = "product_store",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private Set<Store> stores = new HashSet<>();

}
