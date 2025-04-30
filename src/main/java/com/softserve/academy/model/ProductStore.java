package com.softserve.academy.model;

import jakarta.persistence.*;
import lombok.Data;


public class ProductStore {

    private Long id;

    private Product product;

    private Store store;

    private Integer stock;

    private Double price;
}