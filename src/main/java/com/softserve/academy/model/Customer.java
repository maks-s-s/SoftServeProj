package com.softserve.academy.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the system.
 * Each customer can have multiple purchases associated with them.
 */
public class Customer {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private List<Purchase> purchases = new ArrayList<>();

}