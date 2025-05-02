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
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class Customer {
    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @OneToMany( cascade = CascadeType.ALL,
                mappedBy="customer")
    private List<Purchase> purchases = new ArrayList<>();

    public Customer (String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public void addPurchase(Purchase purchase){purchases.add(purchase);}

    @Override
    public String toString() {return name+" "+email+" "+phoneNumber;}
}
