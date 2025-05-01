package com.softserve.academy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString(exclude = {"stores", "category"})

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 75, message = "Name cannot have more than 75 characters")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "price cannot be null")
    @PositiveOrZero(message = "Price cannot be negative")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal price;

    @NotNull(message = "category cannot be null")
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "product_store",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private Set<Store> stores = new HashSet<>();

}
