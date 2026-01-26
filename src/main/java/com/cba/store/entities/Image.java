package com.cba.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "images", schema = "dataexplorer1")
public class Image {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 56)
    @NotNull
    @Column(name = "filename", nullable = false, length = 56)
    private String filename;

    @Size(max = 60)
    @Column(name = "alt", length = 60)
    private String alt;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

}