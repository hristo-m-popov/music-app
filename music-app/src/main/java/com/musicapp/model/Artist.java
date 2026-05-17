package com.musicapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String type;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String genre;

    @NotBlank
    @Size(max = 60)
    @Column(nullable = false, length = 60)
    private String country;

    @NotNull
    @Column(nullable = false)
    private Integer formedYear;

    @Size(max = 500)
    @Column(length = 500)
    private String bio;

    @NotNull
    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now();
}