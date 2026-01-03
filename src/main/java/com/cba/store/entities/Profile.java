package com.cba.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class Profile {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private User users;

    @Column(name = "bio", nullable = false)
    private String bio;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @ColumnDefault("0")
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;

}