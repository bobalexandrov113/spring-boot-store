package com.cba.store.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="addresses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name =  "street",nullable = false)
    private String street;

    @Column(name="city",nullable = false)
    private String city;

    @Column(name="state",nullable = false)
    private String state;

    @Column(name="zip",nullable = false)
    private String zip;


    @ManyToOne
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private User user;
}
