package com.cba.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,name = "name")
    private String name;
    @Column(nullable = false,name = "email")
    private String email;
    @Column(nullable = false,name = "password")
    private String password;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    public void addAddress(Address address)
     {
        this.addresses.add(address);
        address.setUser(this);

     }
}
