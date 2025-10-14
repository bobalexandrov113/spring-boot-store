package com.cba.store.entities;

import jakarta.persistence.*;
import lombok.*;

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
}
