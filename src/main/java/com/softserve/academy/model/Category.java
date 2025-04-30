package com.softserve.academy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a category of products.
 * Each category can have multiple products associated with it.
 */
public class Category {
    private Long id;
    private String name;
    private List<Product> products = new ArrayList<>();
}
