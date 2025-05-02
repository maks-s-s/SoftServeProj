package com.softserve.academy.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the system.
 * Each customer can have multiple purchases associated with them.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Customer {
    @GeneratedValue
    @Id
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Column
    private String password;

    @OneToMany
    private List<Purchase> purchases = new ArrayList<>();

    public Customer (String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}