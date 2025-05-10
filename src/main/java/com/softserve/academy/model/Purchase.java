package com.softserve.academy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Represents a purchase made by a customer for a specific product.
 */

@Entity
@Table(name = "purchase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"customer", "product"})
@Builder

public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @NotNull(message = "Customer cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private int quantity;

    @Column(nullable = false)
    @PositiveOrZero(message = "Total price must be zero or positive")
    @Digits(integer = 19, fraction = 2)
    @NotNull(message = "Total price cannot be null")
    private BigDecimal totalPrice;

    @Column(nullable = false)
    @FutureOrPresent(message = "Purchase date must be in the present or future")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime purchaseDate;

    public void setTotalPrice(){
        if (this.product.getDiscount() == 0) {
            this.totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
        else {
            this.totalPrice = product.getPrice().multiply(BigDecimal.valueOf(1 - this.product.getDiscount()))
                    .multiply(BigDecimal.valueOf(quantity));
        }
    }
}