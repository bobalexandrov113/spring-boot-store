package com.cba.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "id", nullable = false)
    private Byte id;

    @Column(name = "name")
    private String name;

}