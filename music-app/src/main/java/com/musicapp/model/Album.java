package com.musicapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id", nullable = false)
    @JsonIgnoreProperties({"albums", "concerts"})
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