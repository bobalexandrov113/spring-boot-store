package com.cba.store.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "addresses", schema = "store")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "zip", nullable = false)
    private String zip;

    @Column(name = "state")
    private String state;

}