package com.cba.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Column(name = "id")
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

     public void  removeAddress(Address address)
     {
        this.addresses.remove(address);
        address.setUser(null);
     }
     @ManyToMany
     @JoinTable(
             name="user_tags",
             joinColumns = @JoinColumn(name="user_id"),
             inverseJoinColumns = @JoinColumn(name = "tag_id")
     )

     @Builder.Default
    private Set<Tag> tags = new HashSet<>();
    @OneToOne(mappedBy = "user")
    private Profile profile;

}
