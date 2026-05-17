package com.musicapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String title;

    @NotNull
    @Column(nullable = false)
    private LocalDate releaseDate;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer trackCount;

    @NotNull
    @Column(nullable = false)
    private Double durationMinutes;

    @Size(max = 100)
    @Column(length = 100)
    private String recordLabel;

    @NotNull
    @Column(nullable = false)
    private Double price;
}